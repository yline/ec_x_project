package com.yline.file.module.file;

import android.content.Context;
import android.view.ViewGroup;

import com.yline.file.R;
import com.yline.file.module.file.model.FileBean;
import com.yline.view.recycler.adapter.AbstractListAdapter;
import com.yline.view.recycler.holder.ViewHolder;

public class FileListAdapter extends AbstractListAdapter<FileBean> {
    public FileListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemRes(int i) {
        return R.layout.item_file_list;
    }

    @Override
    protected void onBindViewHolder(ViewGroup parent, ViewHolder viewHolder, int position) {
        viewHolder.setImageResource(R.id.iv_type, getItem(position).getImageId());
        viewHolder.setText(R.id.tv_name, getItem(position).getFileName());
        viewHolder.setText(R.id.tv_info, getItem(position).getChildMessage());
    }
}
