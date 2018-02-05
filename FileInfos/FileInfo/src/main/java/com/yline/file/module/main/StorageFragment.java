package com.yline.file.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseFragment;
import com.yline.file.R;
import com.yline.file.module.file.FileInfoActivity;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 手机 存储
 *
 * @author yline 2018/2/5 -- 19:17
 * @version 1.0.0
 */
public class StorageFragment extends BaseFragment {
    private StorageRecyclerAdapter mRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_storage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.storage_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new StorageRecyclerAdapter();
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<StorageModel>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, StorageModel itemModel, int position) {
                LogUtil.v("selected path = " + itemModel);
                FileInfoActivity.launcher(getContext(), itemModel.getTopPath());
            }
        });
    }

    private void initData() {
        List<StorageModel> pathList = new ArrayList<>();
        pathList.add(new StorageModel("内部存储", FileUtil.getPathTop()));

        mRecyclerAdapter.setDataList(pathList, true);
    }

    private class StorageRecyclerAdapter extends AbstractCommonRecyclerAdapter<StorageModel> {
        private Callback.OnRecyclerItemClickListener<StorageModel> mOnItemClickListener;

        private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<StorageModel> listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public int getItemRes() {
            return R.layout.item_main;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            final StorageModel itemModel = getItem(position);

            holder.setText(R.id.item_main_name, itemModel.getTitle());
            holder.setText(R.id.item_main_info, getDescString(itemModel.getTopPath()));

            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        int holderPosition = holder.getAdapterPosition();
                        mOnItemClickListener.onItemClick(holder, itemModel, holderPosition);
                    }
                }
            });
        }

        private String getDescString(String topPath) {
            long totalSize = FileSizeUtil.getFileBlockSize(topPath);
            long retainSize = FileSizeUtil.getFileAvailableSize(topPath);

            return String.format("总共：%s 可用：%s", FileSizeUtil.formatFileAutoSize(totalSize), FileSizeUtil.formatFileAutoSize(retainSize));
        }
    }

    private static class StorageModel implements Serializable {
        private String title;
        private String topPath;

        public StorageModel(String title, String topPath) {
            this.title = title;
            this.topPath = topPath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTopPath() {
            return topPath;
        }

        public void setTopPath(String topPath) {
            this.topPath = topPath;
        }
    }
}
