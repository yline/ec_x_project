package com.flight.flight

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.project.wechatflight.R
import com.yline.application.SDKManager
import com.yline.utils.UIScreenUtil
import kotlinx.android.synthetic.main.fragment_flight_stop.*

class FlightStopDialog : DialogFragment() {
    companion object {
        fun newInstance(): FlightStopDialog {
            val dialogFragment = FlightStopDialog()
            dialogFragment.isCancelable = false
            return dialogFragment
        }
    }

    override fun onStart() {
        super.onStart()

        val window = dialog!!.window ?: return

        //这里设置透明度
        window.decorView.setPadding(0, 0, 0, 0)
        window.setDimAmount(0.2f)
        window.setGravity(Gravity.CENTER)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        // window.setWindowAnimations(android.R.style.MenuBottomDialogAnimation;)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val layoutParams = window.attributes
        layoutParams.width = getWidth()
        window.attributes = layoutParams
    }

    protected fun getWidth(): Int {
        return UIScreenUtil.getScreenWidth(SDKManager.getApplication()) - UIScreenUtil.dp2px(SDKManager.getApplication(), 40f)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flight_stop, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_restart.setOnClickListener {
            dialogCallback?.invoke(true)
            dismiss()
        }
        btn_quit.setOnClickListener {
            dialogCallback?.invoke(false)
            dismiss()
        }
    }

    private var dialogCallback: ((Boolean) -> Unit)? = null

    fun registerListener(callback: ((Boolean) -> Unit)?): FlightStopDialog {
        dialogCallback = callback
        return this
    }
}