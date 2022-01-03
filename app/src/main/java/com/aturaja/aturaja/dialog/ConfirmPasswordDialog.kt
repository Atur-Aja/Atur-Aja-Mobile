package com.aturaja.aturaja.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import com.aturaja.aturaja.R

class ConfirmPasswordDialog(konteks: Activity): Dialog(konteks) {
    private lateinit var tvYes: TextView
    private lateinit var tvNo: TextView
    private lateinit var listener: DialogPasswordListener
    private var konteks = konteks

    interface DialogPasswordListener {
        fun onFinishDialog(choose: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_confirm_password)

        tvYes = findViewById(R.id.tvYes)
        tvNo = findViewById(R.id.tvNo)

        listener = konteks as DialogPasswordListener

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