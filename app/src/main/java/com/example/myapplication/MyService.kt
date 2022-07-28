package com.example.myapplication

import android.app.*
import com.example.myapplication.BasketObj as BasketObj

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.lang.StringBuilder

data class ServiceBasketObj(val basketObj: BasketObj, var count: Int)
data class ServiceBasketObjLiquid(val basketObjLiquid: MenuDataObj, var count: Int)

class MyService : Service() {
    //
    lateinit var valueUserId: String //로그인 성공 시 아이디 저장

    lateinit var howToEat: String //먹을 방법

    var basket = arrayListOf<ServiceBasketObj>() //장바구니

    var basketLiquid = arrayListOf<ServiceBasketObjLiquid>()

    var totalCount = 0 //장바구니에 들어간 메뉴 갯수

    var totalAmount = 0 //총 가격

    // BoundService
    inner class MyBinder : Binder() {
        fun getService():MyService {
            Log.d("service", "BindBind")
            return this@MyService
        }
    }
    val binder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    //notification 구현
    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null

    private val mNotificationId = 123
    private fun generateForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //안드로이드 버전이 일정이상일 시 channel 필요
            val intentMainLanding = Intent(this, MenuActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, 0)
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup("chats_group", "Chats")
                )
                val notificationChannel =
                    NotificationChannel("service_channel", "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN)
                notificationChannel.enableLights(false) //알림표시등 표시여부
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET //잠금화면 표시여부
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" 총 금액은 : ${totalAmount}").toString())
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
                .setContentText("Touch to open") //
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent) //누를시 이동할 액티비티
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = resources.getColor(R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        generateForegroundNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //Show user information
    fun bringUserId(valueId: String) {
        valueUserId = valueId
        Log.d("ididid",valueUserId)
    }

    fun sendUserId(): String {
        Log.d("id","send")
        return valueUserId
    }

    //Bring HowToEat
    fun bringHowToEat(value: String) {
        howToEat = value
    }

    //Menu
    fun addBasketObj(basketObj: BasketObj) {

        var num = 0
        for(index in 0 until basket.size) {
            if(basket[index].basketObj == basketObj) {
                basket[index].count += 1

                totalCount += 1
                totalAmount += basket[index].basketObj.price

                break
            }
            else {num+=1}
        }
        if(num == basket.size) {
            val serviceBasketObj = ServiceBasketObj(basketObj,1)
            basket.add(serviceBasketObj)

            totalCount += 1
            totalAmount += basketObj.price
        }
        Log.d("addBsket","Ok")
    }

    fun addBasketObjLiquid(basketObjLiquid: MenuDataObj) {
        var num = 0
        for(index in 0 until basketLiquid.size) {
            if(basketLiquid[index].basketObjLiquid == basketObjLiquid) {
                basketLiquid[index].count += 1

                totalCount += 1
                totalAmount += basketLiquid[index].basketObjLiquid.menu_price

                break
            }
            else {num+=1}
        }
        if(num == basketLiquid.size) {
            val serviceBasketObjLiquid = ServiceBasketObjLiquid(basketObjLiquid,1)
            basketLiquid.add(serviceBasketObjLiquid)

            totalCount += 1
            totalAmount += basketObjLiquid.menu_price
        }
    }

    fun deleteBasketObj(basketObj: BasketObj) {
        for(index in 0 until basket.size-1 ) {
            if(basket[index].basketObj == basketObj) {
                totalCount -= basket[index].count
                totalAmount -= basket[index].basketObj.price*basket[index].count
                basket.removeAt(index)
            }
        }
    }

    fun deleteBasketObjLiquid(basketObjLiquid: MenuDataObj) {
        for(index in 0 until basketLiquid.size-1 ) {
            if(basketLiquid[index].basketObjLiquid == basketObjLiquid) {
                totalCount -= basketLiquid[index].count
                totalAmount -= basketLiquid[index].basketObjLiquid.menu_price*basketLiquid[index].count
                basketLiquid.removeAt(index)
            }
        }
    }

    fun entireBasketDelete() {
        basket.clear()
        basketLiquid.clear()
        totalCount = 0
        totalAmount = 0
    }

    fun sendBasketData(): ArrayList<ServiceBasketObj> {
        Log.d("sendBasketData",basket.toString())
        return basket
    }

    fun sendBasketDataLiquid(): ArrayList<ServiceBasketObjLiquid> {
        Log.d("sendBasketData",basketLiquid.toString())
        return basketLiquid
    }

    fun sendTotalCountData(): Int {
        return totalCount
    }

    fun sendTotalAmountData(): Int {
        return totalAmount
    }

    // started service할 때 접근하는 명령어를 적는 곳이다.
    companion object {
        val ACTION_START = "start"
        val ACTION_RUN  = "run"
        val ACTION_STOP = "STOP"
    }
}