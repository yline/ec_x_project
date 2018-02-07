package com.yline.file.module.fileclassify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yline.file.R;

/**
 * 文件分类，统一的头部
 *
 * @author yline 2018/2/7 -- 19:48
 * @version 1.0.0
 */
public class ClassifyHeaderView extends RelativeLayout {
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
    }

    public interface OnHeaderClickListener {
        
    }
}
