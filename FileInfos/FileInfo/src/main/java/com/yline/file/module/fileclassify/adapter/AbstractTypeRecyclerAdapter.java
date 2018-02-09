package com.yline.file.module.fileclassify.adapter;

import android.view.View;

import com.yline.file.module.file.model.FileInfoModel;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 文件类型的，基类；不支持头部和底部
 *
 * @author yline 2018/2/9 -- 10:47
 * @version 1.0.0
 */
public abstract class AbstractTypeRecyclerAdapter extends AbstractCommonRecyclerAdapter<FileInfoModel> {
    private Callback.OnRecyclerItemClickListener<FileInfoModel> mOnItemClickListener;

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position, List<Object> payloads) {
        // 点击事件
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(holder, getItem(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            }
        });
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setOnItemClickListener(Callback.OnRecyclerItemClickListener<FileInfoModel> listener) {
        this.mOnItemClickListener = listener;
    }
}
