package com.yline.file.module.file.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yline.file.R;
import com.yline.utils.FileSizeUtil;

/**
 * 文件分类，统一的头部
 *
 * @author yline 2018/2/7 -- 19:48
 * @version 1.0.0
 */
public class ClassifyHeaderView extends RelativeLayout {
    private OnHeaderClickListener mOnHeaderClickListener;

    public ClassifyHeaderView(Context context) {
        super(context);
        initView();
    }

    public ClassifyHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ClassifyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_classify_header, this, true);

        // 返回
        findViewById(R.id.view_classify_header_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnHeaderClickListener) {
                    mOnHeaderClickListener.onHeaderCloseClick();
                }
            }
        });

        // 更多
        findViewById(R.id.view_classify_header_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnHeaderClickListener) {
                    mOnHeaderClickListener.onHeaderMoreClick();
                }
            }
        });
    }

    /**
     * 设置显示文案
     *
     * @param title 标题名称
     * @param size  总大小
     */
    public void setTitle(String title, long size) {
        TextView titleTextView = findViewById(R.id.view_classify_header_title);
        titleTextView.setText(title);

        TextView sizeTextView = findViewById(R.id.view_classify_header_size);
        sizeTextView.setText((size < 0 ? "" : FileSizeUtil.formatFileAutoSize(size)));
    }

    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        this.mOnHeaderClickListener = listener;
    }

    public interface OnHeaderClickListener {
        /**
         * 点击头部，关闭
         */
        void onHeaderCloseClick();

        /**
         * 点击头部，更多
         */
        void onHeaderMoreClick();
    }
}
