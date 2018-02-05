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
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_classify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.classify_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerAdapter = new ClassifyRecyclerAdapter();
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, String s, int position) {
                // TOAST
            }
        });
    }

    private void initData() {
        // 放入数据
    }

    private class ClassifyRecyclerAdapter extends AbstractCommonRecyclerAdapter<String> {
        private Callback.OnRecyclerItemClickListener<String> mOnItemClickListener;

        private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<String> listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public int getItemRes() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            final String itemModel = getItem(position);

            holder.setText(android.R.id.text1, getItem(position));

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
