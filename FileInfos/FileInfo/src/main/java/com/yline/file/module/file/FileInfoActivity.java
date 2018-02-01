package com.yline.file.module.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.IApplication;
import com.yline.file.R;
import com.yline.file.common.IntentUtils;
import com.yline.file.common.LoadingView;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.helper.FileDbLoader;
import com.yline.file.module.file.helper.FileInfoLoadReceiver;
import com.yline.file.module.file.model.FileModel;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * 文件信息，展示
 *
 * @author yline 2018/1/29 -- 20:41
 * @version 1.0.0
 */
public class FileInfoActivity extends BaseAppCompatActivity {
    private static final String KEY_TOP_PATH = "topPath";

    public static void launcher(Context context, String topPath) {
        if (null != context) {
            Intent intent = new Intent(context, FileInfoActivity.class);
            intent.putExtra(KEY_TOP_PATH, topPath);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private FileInfoAdapter mFileInfoAdapter;
    private LoadingView mLoadingView;
    private TextView mTvPath, mTvSize;

    private Stack<String> mPathStack;
    private FileInfoLoadReceiver mLoadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_info);

        initView();
        initData();
    }

    private void initView() {
        mLoadingView = findViewById(R.id.file_info_loading);
        mTvPath = findViewById(R.id.file_info_title_path);
        mTvSize = findViewById(R.id.file_info_title_size);

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
                } else {
                    Intent intent = IntentUtils.getIntentAll(fileModel.getAbsolutePath());
                    if (null != intent) {
                        if (null != intent.resolveActivity(getPackageManager())) {
                            startActivity(intent);
                        } else {
                            LogUtil.v("file: " + fileModel.getAbsolutePath() + ", cannot resolve");
                            IApplication.toast("文件无法打开");
                        }
                    }
                }
            }
        });
    }

    private void initData() {
        mPathStack = new Stack<>();

        mLoadReceiver = new FileInfoLoadReceiver();
        FileInfoLoadReceiver.registerReceiver(mLoadReceiver);
        mLoadReceiver.setOnFileInfoReceiverListener(new FileInfoLoadReceiver.OnFileInfoReceiverListener() {
            @Override
            public void onFileInfoReceiver() {
                if (null != mPathStack && !mPathStack.isEmpty()) {
                    refreshRecycler(mPathStack.peek());
                }
            }
        });

        String path = "";
        Intent intent = getIntent();
        if (null != intent) {
            path = intent.getStringExtra(KEY_TOP_PATH);
            // 如果首次传入的路径，带"/"，则去除
            if (!TextUtils.isEmpty(path) && path.endsWith(File.separator)) {
                path = path.substring(0, path.length() - 1);
            }
        }
        refreshRecycler(path);
    }

    private void refreshRecycler(final String path) {
        if (!mPathStack.contains(path)) {
            mPathStack.push(path);
        }

        mLoadingView.loading();
        mTvPath.setText((path + File.separator));
        FileDbLoader.getFileList(path, new FileDbLoader.OnLoadListener() {
            @Override
            public void onLoadFinish(@NonNull List<FileModel> fileBeanList) {
                if (!isFinishing()) {
                    if (fileBeanList.isEmpty()) {
                        mLoadingView.loadEmpty();
                    } else {
                        mLoadingView.loadSuccess();
                    }

                    long fileSize = FileDbManager.loadFileModelForSize(path + File.separator);
                    mTvSize.setText(String.format("大小：%s", (fileSize == FileSizeUtil.getErrorSize() ? "loading" : FileSizeUtil.formatFileAutoSize(fileSize))));
                    mFileInfoAdapter.setDataList(fileBeanList, true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mPathStack.isEmpty()) {
            mPathStack.pop(); // 先，抛出，当前的
        }

        if (mPathStack.isEmpty()) {
            super.onBackPressed();
        } else {
            refreshRecycler(mPathStack.pop());
        }
    }

    @Override
    protected void onDestroy() {
        FileInfoLoadReceiver.unRegisterReceiver(mLoadReceiver);
        super.onDestroy();
    }

    private class FileInfoAdapter extends AbstractCommonRecyclerAdapter<FileModel> {
        private final static int ICON_FOLDER = R.drawable.file_info_dir;
        private final static int ICON_FILE = R.drawable.file_info_file;

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
