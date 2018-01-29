package com.yline.file.module.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.R;
import com.yline.file.module.file.helper.FileDbLoader;
import com.yline.file.module.file.model.FileModel;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;
import java.util.Locale;

public class FileInfoActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FileInfoActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private FileInfoAdapter mFileInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_info);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.file_info_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFileInfoAdapter = new FileInfoAdapter();
        recyclerView.setAdapter(mFileInfoAdapter);

        mFileInfoAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<FileModel>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, FileModel fileModel, int position) {
                if (fileModel.isDirectory()) {
                    String newTopPath = fileModel.getAbsolutePath();
                    refreshRecycler(newTopPath);
                }
            }
        });
    }

    private void initData() {
        String path = FileUtil.getPathTop();
        refreshRecycler(path);
    }

    private void refreshRecycler(String path) {
        FileDbLoader.getFileList(path, new FileDbLoader.OnLoadListener() {
            @Override
            public void onLoadFinish(List<FileModel> fileBeanList) {
                if (!isFinishing()) {
                    mFileInfoAdapter.setDataList(fileBeanList, true);
                }
            }
        });
    }

    private class FileInfoAdapter extends AbstractCommonRecyclerAdapter<FileModel> {
        private final static int ICON_FOLDER = R.drawable.filechooser_folder;
        private final static int ICON_FILE = R.drawable.filechooser_file;

        private Callback.OnRecyclerItemClickListener<FileModel> mItemClickListener;

        private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<FileModel> listener) {
            this.mItemClickListener = listener;
        }

        @Override
        public int getItemRes() {
            return R.layout.item_file_info;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
            final FileModel fileBean = getItem(position);

            int imageId = fileBean.isDirectory() ? ICON_FOLDER : ICON_FILE;
            holder.setImageResource(R.id.item_file_info_iv, imageId);

            holder.setText(R.id.item_file_info_name, fileBean.getFileName());

            String childMsg = getChildMessage(fileBean.isDirectory(), fileBean.getFileSize(), fileBean.getChildDirCount(), fileBean.getChildFileCount());
            holder.setText(R.id.item_file_info_info, childMsg);

            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mItemClickListener) {
                        mItemClickListener.onItemClick(holder, fileBean, position);
                    }
                }
            });
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
}
