package com.yline.file.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseFragment;
import com.yline.file.R;
import com.yline.file.common.FileType;
import com.yline.file.module.file.FileTypeActivity;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 *
 * @author yline 2018/2/5 -- 19:16
 * @version 1.0.0
 */
public class ClassifyFragment extends BaseFragment {
    private ClassifyRecyclerAdapter mRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.classify_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerAdapter = new ClassifyRecyclerAdapter();
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<ClassifyModel>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, ClassifyModel itemModel, int position) {
                if (itemModel.getFileType() != FileType.UNKNOW) {
                    FileTypeActivity.launcher(getContext(), itemModel.getFileType());
                }
            }
        });
    }

    private void initData() {
        refreshFileType();
    }

    public void refreshFileType() {
        List<ClassifyModel> dataList = new ArrayList<>();
        attachFileType(dataList);
        mRecyclerAdapter.setDataList(dataList, true);
    }

    private void attachFileType(List<ClassifyModel> dataList) {
        long startTime = System.currentTimeMillis();
        for (FileType fileType : FileType.values()) {
            attachItem(fileType, dataList);
        }
        LogUtil.v("attachFileType diffTime = " + (System.currentTimeMillis() - startTime));
    }

    private void attachItem(FileType fileType, List<ClassifyModel> dataList) {
        long startTime = System.currentTimeMillis();
        long count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
    }

    private class ClassifyRecyclerAdapter extends AbstractCommonRecyclerAdapter<ClassifyModel> {
        private Callback.OnRecyclerItemClickListener<ClassifyModel> mOnItemClickListener;

        private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<ClassifyModel> listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public int getItemRes() {
            return R.layout.item_main_classify;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            final ClassifyModel itemModel = getItem(position);

            holder.setImageResource(R.id.item_main_classify_icon, itemModel.getFileType().getResId());
            holder.setText(R.id.item_main_classify_name, itemModel.getFileType().getStr());
            holder.setText(R.id.item_main_classify_count, itemModel.getCount() + "项");

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

    private static class ClassifyModel implements Serializable {
        private FileType fileType;
        private long count;

        public ClassifyModel(FileType type, long count) {
            this.fileType = type;
            this.count = count;
        }

        public FileType getFileType() {
            return fileType;
        }

        public long getCount() {
            return count;
        }
    }
}
