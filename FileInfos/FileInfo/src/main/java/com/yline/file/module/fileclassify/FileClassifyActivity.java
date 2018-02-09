package com.yline.file.module.fileclassify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.R;
import com.yline.file.common.FileType;
import com.yline.file.common.IntentUtils;
import com.yline.file.common.LoadingView;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.file.module.fileclassify.adapter.AbstractTypeRecyclerAdapter;
import com.yline.file.module.fileclassify.adapter.AudioTypeRecyclerAdapter;
import com.yline.file.module.fileclassify.adapter.FileTypeRecyclerAdapter;
import com.yline.file.module.fileclassify.adapter.VideoTypeRecyclerAdapter;
import com.yline.file.module.fileclassify.view.ClassifyHeaderView;
import com.yline.file.module.fileclassify.view.UpperItemMenuView;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.test.StrConstant;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 查看文件类型信息
 *
 * @author yline 2018/2/6 -- 10:01
 * @version 1.0.0
 */
public class FileClassifyActivity extends BaseAppCompatActivity {
    private static final String KEY_FILE_TYPE = "Launcher_FileType";

    public static void launcher(Context context, @NonNull FileType fileType) {
        if (null != context) {
            Intent intent = new Intent(context, FileClassifyActivity.class);
            intent.putExtra(KEY_FILE_TYPE, fileType);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private AbstractTypeRecyclerAdapter mRecyclerAdapter;
    private LoadingView mLoadingView;
    private UpperItemMenuView mUpperMenuView;
    private ClassifyHeaderView mHeaderView;

    private FileType mFileType;
    private long mInitDataStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_classify);

        if (null != getIntent()) {
            mFileType = (FileType) getIntent().getSerializableExtra(KEY_FILE_TYPE);
        }

        initView();
        initData();
    }

    private void initView() {
        mLoadingView = findViewById(R.id.file_classify_loading);
        mHeaderView = findViewById(R.id.file_classify_header);
        mUpperMenuView = findViewById(R.id.file_classify_upper_item_menu);

        RecyclerView recyclerView = findViewById(R.id.file_classify_recycler);
        if (null != mFileType){
            switch (mFileType) {
                case VIDEO:
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                    mRecyclerAdapter = new VideoTypeRecyclerAdapter();
                    break;
                case AUDIO:
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerAdapter = new AudioTypeRecyclerAdapter();
                    break;
                case IMAGE:
                case APK:
                case WORD:
                case EXCEL:
                case PPT:
                case PDF:
                case TEXT:
                case HTML:
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerAdapter = new FileTypeRecyclerAdapter();
                    break;
                case UNKNOW:
                default:
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerAdapter = new FileTypeRecyclerAdapter();
            }
        }
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mHeaderView.setOnHeaderClickListener(new ClassifyHeaderView.OnHeaderClickListener() {
            @Override
            public void onHeaderCloseClick() {
                finish();
            }

            @Override
            public void onHeaderMoreClick() {
                mUpperMenuView.setVisibility(View.VISIBLE);
            }
        });

        mUpperMenuView.setOnUpperItemMenuListener(new UpperItemMenuView.OnUpperItemMenuListener() {
            @Override
            public void onUpperMaskClick() {
                closeUpperMenu();
            }

            @Override
            public void onUpperCloseClick() {
                closeUpperMenu();
            }

            @Override
            public void onUpperItemClick(String content, int position) {
                // TODO
            }
        });

        mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<FileInfoModel>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, FileInfoModel fileModel, int position) {
                IntentUtils.openFileAll(FileClassifyActivity.this, fileModel);
            }
        });
    }

    private void initData() {
        mUpperMenuView.setData(StrConstant.getListRandom(4));

        mLoadingView.loading();
        if (null == mFileType) {
            mLoadingView.loadEmpty("文件类型出错"); // 基本不会进入该条件
        } else {
            mHeaderView.setTitle(mFileType.getStr(), -1);

            mInitDataStartTime = System.currentTimeMillis();
            FileDbManager.loadAllAsync(mFileType, new AsyncHelper.OnResultListener<List<FileInfoModel>>() {
                @Override
                public void onAsyncResult(List<FileInfoModel> fileInfoModelList) {
                    LogUtil.v("sub, diffTime = " + (System.currentTimeMillis() - mInitDataStartTime));
                    if (null != fileInfoModelList && !fileInfoModelList.isEmpty()) {
                        mLoadingView.loadSuccess();
                        // 更新数据
                        mHeaderView.setTitle(mFileType.getStr(), calculateFileSize(fileInfoModelList));
                        mRecyclerAdapter.setDataList(fileInfoModelList, true);
                    } else {
                        mLoadingView.loadEmpty("文件夹为空");
                    }
                }
            });
            LogUtil.v("main, diffTime = " + (System.currentTimeMillis() - mInitDataStartTime));
        }
    }

    private void closeUpperMenu() {
        mUpperMenuView.translateOuter(new UpperItemMenuView.OnAnimatorFinishCallback() {
            @Override
            public void onFinish(boolean isAnimator) {
                mUpperMenuView.setVisibility(View.GONE);
            }
        });
    }

    private long calculateFileSize(@NonNull List<FileInfoModel> fileInfoModelList) {
        long totalSize = 0;
        for (FileInfoModel fileInfoModel : fileInfoModelList) {
            totalSize += fileInfoModel.getFileSize();
        }
        return totalSize;
    }
}
