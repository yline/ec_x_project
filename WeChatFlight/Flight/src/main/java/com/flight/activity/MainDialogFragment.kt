package com.flight.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.project.wechatflight.R

class MainDialogFragment : DialogFragment() {
    private var mBtnRestart: Button? = null
    private var mBtnQuit: Button? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.fragment_main, null)
        mBtnRestart = view.findViewById<View>(R.id.btn_restart) as Button
        mBtnQuit = view.findViewById<View>(R.id.btn_quit) as Button
        val dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
        mBtnRestart!!.setOnClickListener {
            if (activity is AlterInputListener) {
                (activity as AlterInputListener?)!!.onAlertInputComplete(true)
                dismiss()
            }
        }
        mBtnQuit!!.setOnClickListener {
            if (activity is AlterInputListener) {
                (activity as AlterInputListener?)!!.onAlertInputComplete(false)
                dismiss()
            }
        }
        return dialog.create()
    }

    interface AlterInputListener {
        fun onAlertInputComplete(isRestart: Boolean)
    }
}