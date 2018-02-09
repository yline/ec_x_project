package com.yline.file.module.fileclassify.adapter;

import com.yline.file.R;
import com.yline.file.fresco.FrescoManager;
import com.yline.file.fresco.drawable.LevelLoadingRenderer;
import com.yline.file.fresco.view.FrescoView;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 视频的文件类型
 *
 * @author yline 2018/2/8 -- 15:46
 * @version 1.0.0
 */
public class VideoTypeRecyclerAdapter extends AbstractTypeRecyclerAdapter {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    @Override
    public int getItemRes() {
        return R.layout.item_video_type;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final FileInfoModel itemModel = getItem(position);

        // 视频，图片
        FrescoView frescoView = holder.get(R.id.item_video_type_fresco);
        LevelLoadingRenderer renderer = new LevelLoadingRenderer.Builder(frescoView.getContext()).build();
        FrescoManager.setImageLocal(frescoView, itemModel.getAbsolutePath(), WIDTH, HEIGHT, renderer);

        //
    }
}
