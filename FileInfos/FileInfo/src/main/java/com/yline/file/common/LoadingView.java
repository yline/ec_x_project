package com.yline.file.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yline.file.R;

/**
 * 加载 蒙版
 *
 * @author yline 2017/10/19 -- 9:16
 * @version 1.0.0
 */
public class LoadingView extends RelativeLayout {
    private View mLoadingLayout;
    private View mFailedLayout;
    private View mEmptyLayout;
    private OnClickListener mOnRetryClickListener;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this, true);

        mLoadingLayout = findViewById(R.id.loading_view_rl_loading);
        mFailedLayout = findViewById(R.id.loading_view_rl_failed);
        mEmptyLayout = findViewById(R.id.loading_view_rl_empty);

        // 点击重试
        mFailedLayout.findViewById(R.id.loading_view_tv_reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnRetryClickListener) {
                    mOnRetryClickListener.onClick(v);
                }
            }
        });
    }

    public void setOnRetryClickListener(OnClickListener mOnRetryClickListener) {
        this.mOnRetryClickListener = mOnRetryClickListener;
    }

    public void loading() {
        setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(VISIBLE);
        mFailedLayout.setVisibility(GONE);
        mEmptyLayout.setVisibility(GONE);
    }

    public void loadFailed() {
        loadFailed(null, null);
    }

    public void loadFailed(String failedStr, String retryStr) {
        setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(GONE);
        mEmptyLayout.setVisibility(GONE);
        mFailedLayout.setVisibility(VISIBLE);

        TextView tvFailed = mFailedLayout.findViewById(R.id.loading_view_tv_failed);
        if (null != failedStr) {
            tvFailed.setText(failedStr);
        }

        TextView tvRetry = mFailedLayout.findViewById(R.id.loading_view_tv_reload);
        if (null != retryStr) {
            tvRetry.setText(retryStr);
        }
    }

    public void loadEmpty() {
        loadEmpty(null);
    }

    public void loadEmpty(String emptyStr) {
        setVisibility(VISIBLE);
        mEmptyLayout.setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(GONE);
        mFailedLayout.setVisibility(GONE);

        TextView tvEmpty = mEmptyLayout.findViewById(R.id.loading_view_tv_empty);
        if (null != emptyStr) {
            tvEmpty.setText(emptyStr);
        }
    }

    public void loadSuccess() {
        setVisibility(GONE);
    }
}
