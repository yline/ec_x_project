package com.yline.file.module.file.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.yline.file.IApplication;
import com.yline.utils.LogUtil;

/**
 * 文件读取完成，监听
 *
 * @author yline 2018/1/30 -- 13:20
 * @version 1.0.0
 */
public class FileInfoLoadReceiver extends BroadcastReceiver {
    private static final String ACTION = "com.yline.file.fileinfo";
    private OnFileInfoReceiverListener mOnReceiverListener;

    public static void registerReceiver(@NonNull FileInfoLoadReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(IApplication.getApplication());
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    public static void unRegisterReceiver(@NonNull FileInfoLoadReceiver receiver) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(IApplication.getApplication());
        broadcastManager.unregisterReceiver(receiver);
    }

    public static void sendReceiver() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(IApplication.getApplication());

        Intent intent = new Intent();
        intent.setAction(ACTION);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        LogUtil.v("FileInfoLoadReceiver, action = " + action);
        if (ACTION.equals(action) && null != mOnReceiverListener){
            mOnReceiverListener.onFileInfoReceiver();
        }
    }

    public void setOnFileInfoReceiverListener(OnFileInfoReceiverListener listener) {
        this.mOnReceiverListener = listener;
    }

    public interface OnFileInfoReceiverListener {
        /**
         * 消息接受
         */
        void onFileInfoReceiver();
    }
}
