package com.flight.flight

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.project.wechatflight.R
import kotlinx.android.synthetic.main.fragment_main.*

class FlightStopDialog : DialogFragment() {
    companion object {
        fun newInstance(): FlightStopDialog {
            val dialogFragment = FlightStopDialog()
            dialogFragment.isCancelable = false
            return dialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.fragment_main, null)
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setView(view)

        btn_restart.setOnClickListener {
            dialogCallback?.invoke(true)
            dismiss()
        }
        btn_quit.setOnClickListener {
            dialogCallback?.invoke(false)
            dismiss()
        }
        return dialog.create()
    }

    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun registerListener(callback: ((Boolean) -> Unit)?): FlightStopDialog {
        dialogCallback = callback
        return this
    }
}