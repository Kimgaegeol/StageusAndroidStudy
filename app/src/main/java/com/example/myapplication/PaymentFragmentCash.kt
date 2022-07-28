package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class PaymentFragmentCash : Fragment() {
    var saving : Boolean = false
    var receipt : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceStage: Bundle?): View? {
        val view = inflater.inflate(R.layout.payment_fragment_cash, container, false)

        btnEventChoice(view)

        return view
    }
    fun btnEventChoice(view: View) {
        view.findViewById<Button>(R.id.saving_o).setOnClickListener {
            saving = true
        }
        view.findViewById<Button>(R.id.saving_x).setOnClickListener {
            saving = false
        }
        view.findViewById<Button>(R.id.receipt_o).setOnClickListener {
            receipt = true
            val dataInterFace = context as DataFromCashFragment
            dataInterFace.sendDataCash(saving,receipt)
        }
        view.findViewById<Button>(R.id.receipt_x).setOnClickListener {
            receipt = false
            val dataInterFace = context as DataFromCashFragment
            dataInterFace.sendDataCash(saving,receipt)
        }
    }
}