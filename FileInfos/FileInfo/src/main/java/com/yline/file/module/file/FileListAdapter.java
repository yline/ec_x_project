package com.yline.file.module.file;

import android.content.Context;
import android.view.ViewGroup;

import com.yline.file.R;
import com.yline.file.module.file.model.FileModel;
import com.yline.utils.FileSizeUtil;
import com.yline.view.recycler.adapter.AbstractListAdapter;
import com.yline.view.recycler.holder.ViewHolder;

import java.util.Locale;

public class FileListAdapter extends AbstractListAdapter<FileModel> {
    private final static int ICON_FOLDER = R.drawable.filechooser_folder;
    private final static int ICON_FILE = R.drawable.filechooser_file;

    public FileListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemRes(int i) {
        return R.layout.item_file_list;
    }

    @Override
    protected void onBindViewHolder(ViewGroup parent, ViewHolder viewHolder, int position) {
        FileModel fileBean = getItem(position);

        int imageId = fileBean.isDirectory() ? ICON_FOLDER : ICON_FILE;
        viewHolder.setImageResource(R.id.iv_type, imageId);

        viewHolder.setText(R.id.tv_name, fileBean.getFileName());

        String childMsg = getChildMessage(fileBean.isDirectory(), fileBean.getFileSize(), fileBean.getChildDirCount(), fileBean.getChildFileCount());
        viewHolder.setText(R.id.tv_info, childMsg);
    }

    /**
     * 文件夹：0，文件：0，大小：0.00B
     */
    private String getChildMessage(boolean isDirectory, long fileSize, int childDirCount, int childFileCount) {
        String fileSizeStr = (FileSizeUtil.getErrorSize() == fileSize) ? "loading" : FileSizeUtil.formatFileAutoSize(fileSize);

        if (isDirectory) {
            return String.format(Locale.CHINA, "文件夹：%d，文件：%d，大小：%s", childDirCount, childFileCount, fileSizeStr);
        } else {
            return String.format("大小：%s", fileSizeStr);
        }
    }
}
