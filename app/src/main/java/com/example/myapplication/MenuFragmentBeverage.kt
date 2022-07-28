package com.example.myapplication

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MenuFragmentBeverage: Fragment() {
    // 리스트 등록
    lateinit var categoryName : String
    var menuList = ArrayList<MenuDataObj>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceStage: Bundle?): View? {
        val view = inflater.inflate(R.layout.menu_fragment_beverage, container, false)

        initRetrofit()
        setMenu(view)

        btnEventShowOtherMenu(view)

        return view
    }

    //메뉴 가져오기
    lateinit var retrofit: Retrofit // -> connection
    lateinit var retrofitHttp: RetrofitService

    fun initRetrofit() {
        retrofit = RetrofitClient.initRetrofit()
        retrofitHttp = retrofit!!.create(RetrofitService::class.java)
    }

    fun setMenu(view: View) {
        val lang = "kr"
        retrofitHttp.getResponseFromCategoryData(lang).enqueue(object:
            Callback<ResponseFromBringCategory> {
            override fun onResponse(call: Call<ResponseFromBringCategory>, response: Response<ResponseFromBringCategory>)
            {
                categoryName = response.body()!!.data[1].category_name
                CoroutineScope(Dispatchers.Main).launch {
                    CoroutineScope(Dispatchers.IO).async {

                    }
                }
                retrofitHttp.getResponseFromMenuData(categoryName, lang).enqueue(object :
                    Callback<ResponseFromMenu> {
                    override fun onResponse(call: Call<ResponseFromMenu>, response: Response<ResponseFromMenu>) {
                        for(index in 0 until response.body()!!.data.size) {
                            menuList.add(MenuDataObj(response.body()!!.data[index].menu_name,response.body()!!.data[index].menu_price,"http://3.39.66.6:3000"+response.body()!!.data[index].menu_image,))
                        }
                        setTableRowLayout(view)
                    }
                    override fun onFailure(call: Call<ResponseFromMenu>, t: Throwable) {
                        Log.d("response","fail")
                    }
                })
            }

            override fun onFailure(call: Call<ResponseFromBringCategory>, t: Throwable) {
                Log.d("response","fail")
            }
        })
    }

    fun setTableRowLayout(view: View) {
        val tableRowLayoutOne = view.findViewById<TableRow>(R.id.beveragemenu_tablerow_one)

        for (index in 0 until 3) {
            val customView = layoutInflater.inflate(R.layout.view_menu,tableRowLayoutOne,false)
            val customViewImage = customView.findViewById<ImageView>(R.id.menu_image)
            Glide.with(this).load(menuList[index].menu_image).into(customViewImage)
            customView.findViewById<TextView>(R.id.menu_text).text = menuList[index].menu_name + "\n" + menuList[index].menu_price
            customView.findViewById<Button>(R.id.menu_btn).setOnClickListener {
                val dataInterFace = context as InterfaceBringBasketObjLiquid
                dataInterFace.bringBasketObjLiquid(menuList[index])
            }
            tableRowLayoutOne.addView(customView)
            Log.d("zz","zz")
        }
    }

    fun btnEventShowOtherMenu(myView: View) {
        val dataInterface = context as InterfaceChangeFragmentLiquid

        val btnTteokbokki = myView?.findViewById<Button>(R.id.btnCoffee)
        btnTteokbokki.setOnClickListener {dataInterface.changeFragmentLogicLiquid(1)}
    }
}