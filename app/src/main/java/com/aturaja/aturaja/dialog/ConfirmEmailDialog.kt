package com.aturaja.aturaja.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import com.aturaja.aturaja.R

import android.widget.TextView


class ConfirmEmailDialog(konteks: Activity) : Dialog(konteks) {
    private lateinit var tvYes: TextView
    private lateinit var tvNo: TextView
    private lateinit var listener: DialogEmailListener
    private var konteks = konteks

    interface DialogEmailListener {
        fun onFinishDialog(choose: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_confirm_email)

        tvYes = findViewById(R.id.tvYes)
        tvNo = findViewById(R.id.tvNo)

        listener = konteks as DialogEmailListener

        tvYes.setOnClickListener {
            listener.onFinishDialog(true)
            dismiss()
        }

        tvNo.setOnClickListener {
            listener.onFinishDialog(false)
            dismiss()
        }

    }
}