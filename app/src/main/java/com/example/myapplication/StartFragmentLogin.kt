package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class StartFragmentLogin : Fragment() {

    lateinit var retrofit: Retrofit
    lateinit var retrofitHttp: RetrofitService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.start_fragment_login, container, false)

        initRetrofit()
        initEvent(view)

        return view
    }

    fun initEvent(view : View) {
        btnEventLoginEnd(view)
        btnEventGoSignupPage(view)
    }

    fun initRetrofit() {
        retrofit = RetrofitClient.initRetrofit()
        retrofitHttp = retrofit!!.create(RetrofitService::class.java)
    }

    fun btnEventLoginEnd(view: View) {
        val btnLoginEnd = view.findViewById<Button>(R.id.btn_login_end)
        btnLoginEnd.setOnClickListener {

            val valueId = view.findViewById<EditText>(R.id.value_id)!!.text.toString()
            val valuePw = view.findViewById<EditText>(R.id.value_pw)!!.text.toString()

            retrofitHttp.getResponseFromLogin(valueId,valuePw).enqueue(object: Callback<ResponseFromStartData> {
                override fun onFailure(call: Call<ResponseFromStartData>, t: Throwable) {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("로그인 실패")
                        .setMessage("실패 이유 : 통신 오류")
                    dialog.show()
                }
                // response의 응답값에 따라 어떤 action을 취할 지 적어줘야한다.
                override fun onResponse(call: Call<ResponseFromStartData>, response: Response<ResponseFromStartData>) {
                    if(response.body()!!.success){
                        val dataInterFace = context as InterfaceResultLogin
                        dataInterFace.resultLogin("success",valueId)
                    }
                    else {
                        val dialog = AlertDialog.Builder(requireContext())
                        dialog.setTitle("로그인 실패")
                            .setMessage("실패 이유 : 아이디 혹은 비밀번호가 일치하지 않습니다.")
                        dialog.show()
                    }
                }
            })

        }
    }

    fun btnEventGoSignupPage(view : View) {
        val btnGoSignup = view.findViewById<Button>(R.id.btn_go_signup)
        btnGoSignup.setOnClickListener {
            val dataInterface = context as InterfaceChangePage
            dataInterface.changePage("signup")
        }
    }

}