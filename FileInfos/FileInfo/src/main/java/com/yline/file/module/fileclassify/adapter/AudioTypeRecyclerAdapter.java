package com.yline.file.module.fileclassify.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yline.file.R;
import com.yline.file.common.IntentUtils;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.file.module.fileclassify.manager.ClassifyManager;
import com.yline.utils.FileSizeUtil;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 音频类型
 *
 * @author yline 2018/2/9 -- 14:15
 * @version 1.0.0
 */
public class AudioTypeRecyclerAdapter extends AbstractTypeRecyclerAdapter {
    @Override
    public int getItemRes() {
        return R.layout.item_audio_type;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final FileInfoModel itemModel = getItem(position);

        // 文件图片
        Bitmap audioBitmap = ClassifyManager.getAudioBitmap(itemModel.getAbsolutePath());
        if (null != audioBitmap) {
            ImageView imageView = holder.get(R.id.item_audio_type_iv);
            imageView.setImageBitmap(audioBitmap);
        } else {
            int fileType = itemModel.getFileType();
            holder.setImageResource(R.id.item_audio_type_iv, IntentUtils.getFileType(fileType).getResId());
        }

        // 文件名
        String fileName = itemModel.getFileName();
        holder.setText(R.id.item_audio_type_name, fileName);

        // 文件大小
        String duration = ClassifyManager.getAudioDurationFormat(itemModel.getAbsolutePath());
        String size = FileSizeUtil.formatFileAutoSize(itemModel.getFileSize());
        holder.setText(R.id.item_audio_type_info, String.format("%s  |  %s", duration, size));

        // 文件路径
        String path = itemModel.getAbsolutePath();
        if (null != path && path.endsWith(fileName)) {
            path = path.substring(0, path.length() - fileName.length());
        }
        holder.setText(R.id.item_audio_type_path, path);
    }
}
