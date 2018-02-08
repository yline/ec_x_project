package com.yline.file.module.fileclassify.adapter;

import com.yline.file.R;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 视频的文件类型
 *
 * @author yline 2018/2/8 -- 15:46
 * @version 1.0.0
 */
public class VideoTypeRecyclerAdapter extends AbstractCommonRecyclerAdapter<FileInfoModel> {
    private Callback.OnRecyclerItemClickListener<FileInfoModel> mOnItemClickListener;

    @Override
    public int getItemRes() {
        return R.layout.item_video_type;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

    public void setOnItemClickListener(Callback.OnRecyclerItemClickListener<FileInfoModel> listener) {
        this.mOnItemClickListener = listener;
    }
}
