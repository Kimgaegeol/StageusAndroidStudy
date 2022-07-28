package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AskHowToEatActivity : AppCompatActivity() {
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) { // 생명주기
        setContentView(R.layout.here_or_go)

        super.onCreate(savedInstanceState)

        serviceBind()
        showWelcomMessage()
        btnEventAskUsingStore()
    }

    override fun onStop() {
        super.onStop()
        serviceStart()
    }

    fun btnEventAskUsingStore() {
        val btnHere = findViewById<Button>(R.id.btnHere)
//        startService(serviceIntent)
        btnHere.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            myService?.bringHowToEat("Here")
            startActivity(intent)
        }

        val btnToGo = findViewById<Button>(R.id.btnToGo)
        btnToGo.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            myService?.bringHowToEat("ToGo")
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

    fun showWelcomMessage() {
        CoroutineScope(Dispatchers.Main).launch {
            userId = CoroutineScope(Dispatchers.IO).async { myService?.sendUserId().toString() }.await()

            Toast.makeText(this@AskHowToEatActivity, "환영합니다! " + userId + "님", Toast.LENGTH_LONG).show()
        }
    }
}