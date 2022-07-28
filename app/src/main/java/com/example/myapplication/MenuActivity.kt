package com.example.myapplication
import android.content.*
import com.example.myapplication.BasketObj as BasketObj

import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

interface InterfaceBringBasketObj {
    fun bringBasketObj(basketObj: BasketObj)
}

interface InterfaceBringBasketObjLiquid {
    fun bringBasketObjLiquid(basketObjLiquid: MenuDataObj)
}

interface InterfaceChangeFragment {
    fun changeFragmentLogic(index: Int)
}

interface InterfaceChangeFragmentLiquid {
    fun changeFragmentLogicLiquid(index: Int)
}

class MenuActivity : AppCompatActivity(), InterfaceBringBasketObj, InterfaceChangeFragment, InterfaceBringBasketObjLiquid, InterfaceChangeFragmentLiquid {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val setFragmentObj = MenuFragmentKimbap()
        supportFragmentManager.beginTransaction().replace(R.id.category, setFragmentObj).commit()

        btnEventBack()
        btnEventGoPayment()
        btnEventEntireBasketDelete()
        btnEventShowOtherCategory()

        initRetrofit()
        serviceStop()
        serviceBind()
    }

    override fun onStop() {
        super.onStop()
        serviceStart()
    }

    lateinit var retrofit: Retrofit // -> connection
    lateinit var retrofitHttp: RetrofitService

    fun initRetrofit() {
        retrofit = RetrofitClient.initRetrofit()
        retrofitHttp = retrofit!!.create(RetrofitService::class.java)
    }

    fun btnEventBack() {
        val backBtn = findViewById<Button>(R.id.btnBack)
        backBtn.setOnClickListener { finish() }
    }

    fun btnEventShowOtherCategory() {
        val btn = findViewById<Button>(R.id.btn_show_other_category)
        btn.setOnClickListener {
            if(btn.text == "커피/음료"){
                btn.text = "음식"
                setBasketLiquid()
                val fragment = MenuFragmentCoffee()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
            else if(btn.text == "음식") {
                btn.text = "커피/음료"
                setBasket()
                val fragment = MenuFragmentKimbap()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
        }
    }

    fun btnEventGoPayment() {
        val cardBtn = findViewById<Button>(R.id.btnGoCardPayment)
        val cashBtn = findViewById<Button>(R.id.btnGoCashPayment)

        cardBtn.setOnClickListener {
            val id = myService?.sendUserId().toString()
            val order_list_array = ArrayList<OrderObj>(myService?.sendBasketData()!!.size + myService?.sendBasketDataLiquid()!!.size)
            for(index in 0 until myService?.sendBasketData()!!.size) {
                order_list_array.add(OrderObj(
                    myService?.sendBasketData()!![index].basketObj.name,
                    myService?.sendBasketData()!![index].count,
                    myService?.sendBasketData()!![index].basketObj.price*myService?.sendBasketData()!![index].count))
            }
            for(index in 0 until myService?.sendBasketDataLiquid()!!.size) {
                order_list_array.add(
                    OrderObj(
                        myService?.sendBasketDataLiquid()!![index].basketObjLiquid.menu_name,
                        myService?.sendBasketDataLiquid()!![index].count,
                        myService?.sendBasketDataLiquid()!![index].basketObjLiquid.menu_price*myService?.sendBasketDataLiquid()!![index].count)
                )
            }
            val order_list = order_list_array.toList()
            val total_price = myService?.sendTotalAmountData() as Int
            val order = Order(id,order_list,total_price)

            var request: HashMap<String,Any> = HashMap()
            request["id"] = order.id
            request["order_list"] = order.order_list
            request["total_price"] = order.total_price

            retrofitHttp.getResponseFromSendOrder(request).enqueue(object : Callback<ResponseFromSendOrder> {
                override fun onResponse(call: Call<ResponseFromSendOrder>, response: Response<ResponseFromSendOrder>) {
                    var dialog = AlertDialog.Builder(this@MenuActivity)
                    dialog.setTitle("영수증")
                    dialog.setMessage("사용자 이름 : " + order.id + "\n"
                    + "주문 내역 : " + order.order_list.toString() + "\n"
                    + "총 금액 : " + order.total_price)
                    dialog.show()
                    retrofitHttp.getResponseFromBringOrderList(myService?.sendUserId().toString()).enqueue(object : Callback<ResponseFromBringOrderList> {
                        override fun onResponse(call: Call<ResponseFromBringOrderList>, response: Response<ResponseFromBringOrderList>) {
                            Log.d("지금까지의 주문기록",response.body()!!.data.toString())
                            myService?.entireBasketDelete()
                            setBasket()
                            setBasketLiquid()
                        }

                        override fun onFailure(call: Call<ResponseFromBringOrderList>, t: Throwable) {
                            Log.d("response","fail")
                        }
                    })
                }

                override fun onFailure(call: Call<ResponseFromSendOrder>, t: Throwable) {
                    Log.d("response","fail")
                }
            })
        }
        cashBtn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("PaymentMethod", "cash")
            startActivity(intent)
        }
    }

    fun setBasketInf() {
        val numInf = findViewById<TextView>(R.id.basket_num)
        numInf.text = myService?.sendTotalCountData().toString() + "개"
        val totalAmountInf = findViewById<TextView>(R.id.basket_total_amount)
        totalAmountInf.text = myService?.sendTotalAmountData().toString() + "원"
    }

    fun setBasket() {
        val basketLayout = findViewById<LinearLayout>(R.id.basketList)
        basketLayout.removeAllViews()

        if(myService?.sendBasketData()!!.size > 0) {
            for(index in 0 until myService?.sendBasketData()!!.size) {
                val customView = layoutInflater.inflate(R.layout.view_basket, basketLayout, false)
                customView.findViewById<ImageView>(R.id.menu_image).setImageResource(myService?.sendBasketData()!![index].basketObj.image)
                customView.findViewById<TextView>(R.id.menu_name).text = myService?.sendBasketData()!![index].basketObj.name
                customView.findViewById<TextView>(R.id.menu_price).text = myService?.sendBasketData()!![index].basketObj.price.toString() + "원"
                customView.findViewById<TextView>(R.id.menu_num).text = myService?.sendBasketData()!![index].count.toString()
                customView.findViewById<Button>(R.id.menu_btn_delete).setOnClickListener {
                    deleteBasketLogic(myService?.sendBasketData()!![index].basketObj)
                }
                basketLayout.addView(customView)
            }
        }
    }

    fun setBasketLiquid() {
        val basketLayout = findViewById<LinearLayout>(R.id.basketList)
        basketLayout.removeAllViews()

        if(myService?.sendBasketDataLiquid()!!.size > 0) {
            for(index in 0 until myService?.sendBasketDataLiquid()!!.size) {
                val customView = layoutInflater.inflate(R.layout.view_basket, basketLayout, false)
                val viewImage = customView.findViewById<ImageView>(R.id.menu_image)
                Glide.with(this).load(myService?.sendBasketDataLiquid()!![index].basketObjLiquid.menu_image).into(viewImage)
                customView.findViewById<TextView>(R.id.menu_name).text = myService?.sendBasketDataLiquid()!![index].basketObjLiquid.menu_name
                customView.findViewById<TextView>(R.id.menu_price).text = myService?.sendBasketDataLiquid()!![index].basketObjLiquid.menu_price.toString() + "원"
                customView.findViewById<TextView>(R.id.menu_num).text = myService?.sendBasketDataLiquid()!![index].count.toString()
                customView.findViewById<Button>(R.id.menu_btn_delete).setOnClickListener {
                    deleteBasketLogicLiquid(myService?.sendBasketDataLiquid()!![index].basketObjLiquid)
                }
                basketLayout.addView(customView)
            }
        }

    }

    fun addBasketLogic(basketObj: BasketObj) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                myService?.addBasketObj(basketObj)
            }.await()

            setBasket()
            setBasketInf()
        }
    }

    fun addBasketLogicLiquid(basketObjLiquid: MenuDataObj) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                myService?.addBasketObjLiquid(basketObjLiquid)
            }.await()

            setBasketLiquid()
            setBasketInf()
        }
    }

    fun deleteBasketLogic(basketObj: BasketObj) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                myService?.deleteBasketObj(basketObj)
            }.await()

            setBasket()
            setBasketInf()
        }
    }

    fun deleteBasketLogicLiquid(basketObjLiquid: MenuDataObj) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                myService?.deleteBasketObjLiquid(basketObjLiquid)
            }.await()

            setBasketLiquid()
            setBasketInf()
        }
    }

    fun btnEventEntireBasketDelete() {
        val btnEntireBasketDelete = findViewById<Button>(R.id.btn_entire_basket_delete)
        btnEntireBasketDelete.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.IO).async {
                    myService?.entireBasketDelete()
                }.await()

                val basketLayout = findViewById<LinearLayout>(R.id.basketList)
                basketLayout.removeAllViews()
                setBasketInf()
            }

        }
    }

    //인터페이스 함수들
    override fun bringBasketObj(basketObj: BasketObj) {
        addBasketLogic(basketObj)
    }

    override fun bringBasketObjLiquid(basketObjLiquid: MenuDataObj) {
        addBasketLogicLiquid(basketObjLiquid)
    }

    override fun changeFragmentLogic(index: Int) {
        when (index) {
            1 -> {
                val fragment = MenuFragmentKimbap()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
            2 -> {
                val fragment = MenuFragmentTteokbokki()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
            3 -> {
                val fragment = MenuFragmentDonkkaseu()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
            4 -> {
                val fragment = MenuFragmentLamen()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
        }
    }

    override fun changeFragmentLogicLiquid(index: Int) {
        when(index) {
            1 -> {
                val fragment = MenuFragmentCoffee()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
            2 -> {
                val fragment = MenuFragmentBeverage()
                supportFragmentManager.beginTransaction().replace(R.id.category, fragment).commit()
            }
        }
    }

    // service 등록
    fun serviceStart() {
        val intent = Intent(this, MyService::class.java)
        startForegroundService(intent)
    }

    fun serviceStop() {
        val intent = Intent(this,MyService::class.java)
        stopService(intent)
    }

    var myService: MyService? = null
    var isService = false
    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            isService = true
            val binder = iBinder as MyService.MyBinder
            myService = binder.getService()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isService = false
        }
    }
    fun serviceBind() {
        val intent = Intent(this, MyService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

}