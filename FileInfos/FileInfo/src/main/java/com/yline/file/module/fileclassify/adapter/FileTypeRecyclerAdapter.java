package com.yline.file.module.fileclassify.adapter;

import com.yline.file.R;
import com.yline.file.common.IntentUtils;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.utils.FileSizeUtil;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 普通的文件类型
 *
 * @author yline 2018/2/8 -- 11:18
 * @version 1.0.0
 */
public class FileTypeRecyclerAdapter extends AbstractTypeRecyclerAdapter {
    @Override
    public int getItemRes() {
        return R.layout.item_file_type;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final FileInfoModel itemModel = getItem(position);

        // 文件图片
        int fileType = itemModel.getFileType();
        holder.setImageResource(R.id.item_file_type_iv, IntentUtils.getFileType(fileType).getResId());

        // 文件名
        String fileName = itemModel.getFileName();
        holder.setText(R.id.item_file_type_name, fileName);

        // 文件路径
        String path = itemModel.getAbsolutePath();
        if (null != path && path.endsWith(fileName)) {
            path = path.substring(0, path.length() - fileName.length());
        }
        holder.setText(R.id.item_file_type_info, path);

        // 文件大小
        holder.setText(R.id.item_file_type_size, FileSizeUtil.formatFileAutoSize(itemModel.getFileSize()));
    }
}
