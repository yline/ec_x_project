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
import com.yline.file.common.IntentUtils;
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
                if (itemModel.getFileType() != IntentUtils.FileType.UNKNOW) {
                    FileTypeActivity.launcher(getContext(), itemModel.getFileType());
                }
            }
        });
    }

    private void initData() {
        List<ClassifyModel> dataList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        attachFileType(dataList);
        LogUtil.v("diffOldTime = " + (System.currentTimeMillis() - startTime));

        mRecyclerAdapter.setDataList(dataList, true);
    }

    private void attachFileType(List<ClassifyModel> dataList) {
        long startTime = System.currentTimeMillis();
        IntentUtils.FileType fileType = IntentUtils.FileType.VIDEO;
        long count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_video));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.AUDIO;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_audio));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.IMAGE;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_image));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.APK;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_app));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.WORD;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_word));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.EXCEL;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_excel));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.PPT;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_ppt));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.PDF;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_pdf));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.TEXT;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_txt));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.HTML;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_html));
        LogUtil.v(fileType.getStr() + " count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        fileType = IntentUtils.FileType.UNKNOW;
        count = FileDbManager.count(fileType);
        dataList.add(new ClassifyModel(fileType, count, R.drawable.icon_type_other));
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

            holder.setImageResource(R.id.item_main_classify_icon, itemModel.getResId());
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
        private IntentUtils.FileType fileType;
        private long count;
        private int resId;

        public ClassifyModel(IntentUtils.FileType type, long count, int resId) {
            this.fileType = type;
            this.count = count;
            this.resId = resId;
        }

        public IntentUtils.FileType getFileType() {
            return fileType;
        }

        public long getCount() {
            return count;
        }

        public int getResId() {
            return resId;
        }
    }
}
