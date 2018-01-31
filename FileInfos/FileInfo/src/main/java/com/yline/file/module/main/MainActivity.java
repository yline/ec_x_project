package com.yline.file.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.IApplication;
import com.yline.file.R;
import com.yline.file.module.file.FileInfoActivity;
import com.yline.file.module.file.helper.FileInfoLoadService;
import com.yline.utils.FileUtil;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序入口
 *
 * @author yline 2018/1/25 -- 13:42
 * @version 1.0.0
 */
public class MainActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, MainActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private MainRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, String s, int position) {
                LogUtil.v("selected path = " + s);
                FileInfoActivity.launcher(MainActivity.this, s);
            }
        });
    }

    private void initData() {
        List<String> pathList = new ArrayList<>();
        pathList.add(FileUtil.getPathTop());

        mRecyclerAdapter.setDataList(pathList, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        FileInfoLoadService.launcher(IApplication.getApplication(), false);
    }

    private class MainRecyclerAdapter extends AbstractCommonRecyclerAdapter<String> {
        private Callback.OnRecyclerItemClickListener<String> mOnItemClickListener;

        private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<String> listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public int getItemRes() {
            return R.layout.item_main;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            holder.setText(R.id.item_main_name, getItem(position));

            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        int holderPosition = holder.getAdapterPosition();
                        mOnItemClickListener.onItemClick(holder, getItem(holderPosition), holderPosition);
                    }
                }
            });
        }
    }

    private static class MainModel implements Serializable {

    }
}
