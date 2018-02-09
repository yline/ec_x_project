package com.yline.file.module.fileclassify.adapter;

import com.yline.file.R;
import com.yline.file.fresco.FrescoManager;
import com.yline.file.fresco.view.FrescoView;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 图片类型
 *
 * @author yline 2018/2/9 -- 17:17
 * @version 1.0.0
 */
public class ImageTypeRecyclerAdapter extends AbstractTypeRecyclerAdapter {
    private static final int IMAGE_SIZE = 300;

    @Override
    public int getItemRes() {
        return R.layout.item_image_type;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        FrescoView frescoView = holder.get(R.id.item_image_type_fresco);
        FrescoManager.setImageLocal(frescoView, getItem(position).getAbsolutePath(), IMAGE_SIZE, IMAGE_SIZE);
    }
}
