package com.yline.file.module.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.R;
import com.yline.file.common.FileType;
import com.yline.file.common.IntentUtils;
import com.yline.file.common.LoadingView;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 查看文件类型信息
 *
 * @author yline 2018/2/6 -- 10:01
 * @version 1.0.0
 */
public class FileTypeActivity extends BaseAppCompatActivity {
    private static final String KEY_FILE_TYPE = "Launcher_FileType";

    public static void launcher(Context context, @NonNull FileType fileType) {
        if (null != context) {
            Intent intent = new Intent(context, FileTypeActivity.class);
            intent.putExtra(KEY_FILE_TYPE, fileType);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private FileTypeRecyclerAdapter mRecyclerAdapter;
    private LoadingView mLoadingView;
    private TextView mTvTitle, mTvTotalSize;

    private FileType mFileType;
    private long mInitDataStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_type);

        if (null != getIntent()) {
            mFileType = (FileType) getIntent().getSerializableExtra(KEY_FILE_TYPE);
        }

        initView();
        initData();
    }

    private void initView() {
        mLoadingView = findViewById(R.id.file_type_loading);
        mTvTitle = findViewById(R.id.file_type_title);
        mTvTotalSize = findViewById(R.id.item_file_type_size);

        RecyclerView recyclerView = findViewById(R.id.file_type_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new FileTypeRecyclerAdapter();
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<FileInfoModel>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, FileInfoModel fileModel, int position) {
                IntentUtils.openFileAll(FileTypeActivity.this, fileModel);
            }
        });
    }

    private void initData() {
        mLoadingView.loading();
        if (null == mFileType) {
            mLoadingView.loadEmpty("文件类型出错"); // 基本不会进入该条件
        } else {
            mInitDataStartTime = System.currentTimeMillis();
            FileDbManager.loadAllAsync(mFileType, new AsyncHelper.OnResultListener<List<FileInfoModel>>() {
                @Override
                public void onAsyncResult(List<FileInfoModel> fileInfoModelList) {
                    LogUtil.v("sub, diffTime = " + (System.currentTimeMillis() - mInitDataStartTime));
                    if (null != fileInfoModelList && !fileInfoModelList.isEmpty()) {
                        mLoadingView.loadSuccess();
                        // 更新数据
                        mRecyclerAdapter.setDataList(fileInfoModelList, true);
                    } else {
                        mLoadingView.loadEmpty("文件夹为空");
                    }
                }
            });
            LogUtil.v("main, diffTime = " + (System.currentTimeMillis() - mInitDataStartTime));
        }
    }

    private class FileTypeRecyclerAdapter extends AbstractCommonRecyclerAdapter<FileInfoModel> {
        private Callback.OnRecyclerItemClickListener<FileInfoModel> mOnItemClickListener;

        private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<FileInfoModel> listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public int getItemRes() {
            return R.layout.item_file_type;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            final FileInfoModel itemModel = getItem(position);

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

            // 点击事件
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(holder, itemModel, holder.getAdapterPosition());
                    }
                }
            });
        }
    }
}
