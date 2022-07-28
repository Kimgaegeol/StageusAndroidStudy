package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.sql.Time

interface DataFromCardFragment {
    fun sendDataCard(saving: Boolean, cardChoice: Boolean, receipt: Boolean)
}

interface DataFromCashFragment {
    fun sendDataCash(saving: Boolean, receipt: Boolean)
}

class PaymentActivity : AppCompatActivity(), DataFromCardFragment, DataFromCashFragment {
    lateinit var howToEat : String
    var basketNum : Int = 0
    var totalAmount : Int = 0
    lateinit var paymentMethond : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)

        howToEat = intent.getStringExtra("HowToEat").toString()
        basketNum = intent.getIntExtra("BasketNum", 0)
        totalAmount = intent.getIntExtra("TotalAmount",0)
        paymentMethond = intent.getStringExtra("PaymentMethod").toString()

        if(paymentMethond == "card") {
            val fragment = PaymentFragmentCard()
            supportFragmentManager.beginTransaction().replace(R.id.option, fragment).commit()
        }
        else if(paymentMethond == "cash") {
            val fragment = PaymentFragmentCash()
            supportFragmentManager.beginTransaction().replace(R.id.option, fragment).commit()
        }

        btnEventBack()

    }

    fun btnEventBack() {
        val backBtn = findViewById<Button>(R.id.btnPaymentCancel)
        backBtn.setOnClickListener { finish() }
    }

    override fun sendDataCard(saving: Boolean, cardChoice: Boolean, receipt: Boolean) {
        if(receipt) {
            lateinit var savingInf : String
            lateinit var cardChoiceInf : String

            if(saving)
            {
                savingInf = "적립 o"
            }
            else {
                savingInf = "적립 x"
            }
            if(cardChoice)
            {
                cardChoiceInf = "할인 o"
            }
            else {
                cardChoiceInf = "할인 x"
            }

            val paymentBtn = findViewById<Button>(R.id.btnPayment)
            paymentBtn.setOnClickListener {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("영수증")
                    .setMessage("식사 방법 : " + howToEat + "\n"
                            + "음식 갯수 : " + basketNum.toString() + "\n"
                            + "총 금액 : " + totalAmount.toString() + "\n"
                    + savingInf + "\n" + cardChoiceInf)
                dialog.show()
            }
        }
        else{
            finish()
        }
    }
    override fun sendDataCash(saving: Boolean,receipt: Boolean) {
        if(receipt) {
            lateinit var savingInf : String

            if(saving)
            {
                savingInf = "적립 o"
            }
            else {
                savingInf = "적립 x"
            }


            val paymentBtn = findViewById<Button>(R.id.btnPayment)
            paymentBtn.setOnClickListener {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("영수증")
                    .setMessage("식사 방법 : " + howToEat + "\n"
                            + "음식 갯수 : " + basketNum.toString() + "\n"
                            + "총 금액 : " + totalAmount.toString() + "\n"
                            + savingInf)
                dialog.show()
            }
        }
        else{
            finish()
        }
    }
}