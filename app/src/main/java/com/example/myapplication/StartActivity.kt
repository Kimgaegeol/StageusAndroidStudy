package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

interface InterfaceChangePage {
    fun changePage(sign : String)
}
interface InterfaceResultLogin {
    fun resultLogin(sign : String, userId: String)
}

class StartActivity : AppCompatActivity(), InterfaceChangePage, InterfaceResultLogin{

    override fun onCreate(savedInstanceState: Bundle?) { // 생명주기
        setContentView(R.layout.start)

        super.onCreate(savedInstanceState)

        serviceBind()

        initevent()

    }

    override fun onStop() {
        super.onStop()
        serviceStart()
    }

    fun initevent() {
        val fragment = StartFragmentLogin()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_box, fragment).commit()

    }

    override fun changePage(sign: String) {
        if(sign == "signup") {
            val fragment = StartFragmentSignup()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_box, fragment).commit()
        }
        else if(sign == "login") {
            val fragment = StartFragmentLogin()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_box, fragment).commit()
        }
    }

    override fun resultLogin(sign: String, userId: String) {
        if (sign == "success") {
            myService?.bringUserId(userId)
            val intent = Intent(this, AskHowToEatActivity::class.java)
            startActivity(intent)

        }
    }

    fun serviceStart() {
        val intent = Intent(this, MyService::class.java)
        startForegroundService(intent)
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