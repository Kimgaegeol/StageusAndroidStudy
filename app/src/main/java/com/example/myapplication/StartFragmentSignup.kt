package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class StartFragmentSignup : Fragment() {

    lateinit var retrofit : Retrofit //레트로핏 등록
    lateinit var retrofitHttp : RetrofitService

    var valueIdOverlapCheck = false //아이디 중복체크 값

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.start_fragment_signup, container, false)

        initRetrofit()
        initEvent(view)

        return view
    }

    fun initEvent(view : View) {
        btnEventIdOverlap(view)
        btnEventSignupEnd(view)
        btnEventGoLoginPage(view)
    }

    fun initRetrofit() {
        retrofit = RetrofitClient.initRetrofit()
        retrofitHttp = retrofit!!.create(RetrofitService::class.java)
    }

    fun btnEventIdOverlap(view: View) {
        val btnIdOverlap = view.findViewById<Button>(R.id.btn_check_overlap)
        btnIdOverlap.setOnClickListener {

            val idValue = view.findViewById<EditText>(R.id.value_id).text.toString()

            retrofitHttp.getResponseFromIdOverlap(idValue).enqueue(object: Callback<ResponseFromStartData> {
                override fun onFailure(call: Call<ResponseFromStartData>, t: Throwable) {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("회원가입 실패")
                        .setMessage("실패 이유 : 통신 오류")
                    dialog.show()
                }

                override fun onResponse(call: Call<ResponseFromStartData>, response: Response<ResponseFromStartData>) {
                    if(response.body()!!.success) {
                        val textIdOverlap = view.findViewById<TextView>(R.id.text_id_overlap)
                        textIdOverlap.text = "중복된 아이디가 존재하지 않습니다!"
                        textIdOverlap.setTextColor(getResources().getColor(R.color.lawngreen))
                        valueIdOverlapCheck = true
                    }
                    else {
                        val textIdOverlap = view.findViewById<TextView>(R.id.text_id_overlap)
                        textIdOverlap.setTextColor(getResources().getColor(R.color.red))
                        textIdOverlap.text = "이미 중복된 아이디가 존재합니다!"
                        valueIdOverlapCheck = false
                    }
                }

            })
        }
    }

    fun btnEventSignupEnd(view: View) {
        val btnSignupEnd = view.findViewById<Button>(R.id.btn_signup_end)
        btnSignupEnd.setOnClickListener {

            if (valueIdOverlapCheck) {
                var requestData: HashMap<String, String> = HashMap()
                requestData["id"] = view.findViewById<EditText>(R.id.value_id).text.toString()
                requestData["pw"] = view.findViewById<EditText>(R.id.value_pw).text.toString()
                requestData["name"] = view.findViewById<EditText>(R.id.value_name).text.toString()
                requestData["contact"] = view.findViewById<EditText>(R.id.value_contact).text.toString()

                retrofitHttp.getResponseFromSignup(requestData).enqueue(object: Callback<ResponseFromStartData> {
                    override fun onFailure(call: Call<ResponseFromStartData>, t: Throwable) {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.setTitle("회원가입 실패")
                            .setMessage("실패 이유 : 통신 오류")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<ResponseFromStartData>, response: Response<ResponseFromStartData>) {
                        if(response.body()!!.success){
                            val dialog = AlertDialog.Builder(requireContext())
                            dialog.setTitle("회원가입 성공")
                                .setMessage("가입에 성공하였습니다! \n 저희 앱을 사용해주셔서 감사합니다!")
                            dialog.show()
                            val dataInterface = context as InterfaceChangePage
                            dataInterface.changePage("login")
                        }
                        else {
                            val dialog = AlertDialog.Builder(requireContext())
                            dialog.setTitle("회원가입 실패")
                                .setMessage("실패 이유 : 회원가입 조건을 만족하지 못했습니다.")
                            dialog.show()
                        }
                    }
                })
            }
            else {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("회원가입 실패")
                    .setMessage("실패 사유 : 아이디 중복확인 미체크")
                dialog.show()
            }

        }
    }

    fun btnEventGoLoginPage(view : View) {
        val btnGoSignup = view.findViewById<Button>(R.id.btn_go_login)
        btnGoSignup.setOnClickListener {
            val dataInterface = context as InterfaceChangePage
            dataInterface.changePage("login")
        }
    }

}