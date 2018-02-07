package com.yline.file.module.file.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.yline.file.R;
import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 上一层，Item 菜单
 *
 * @author yline 2018/2/7 -- 15:36
 * @version 1.0.0
 */
public class UpperItemMenuView extends RelativeLayout {
    private OnUpperItemMenuListener mOnMenuListener;
    private UpperMenuRecyclerAdapter mMenuRecyclerAdapter;
    private RecyclerView mRecyclerView;

    public UpperItemMenuView(Context context) {
        super(context);
        initView();
    }

    public UpperItemMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UpperItemMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_upper_item_menu, this, true);

        mRecyclerView = findViewById(R.id.view_upper_item_menu_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMenuRecyclerAdapter = new UpperMenuRecyclerAdapter();
        mRecyclerView.setAdapter(mMenuRecyclerAdapter);

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mRecyclerView.getHeight() != 0) {
                    translateInner(mRecyclerView);
                }
            }
        });

        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_upper_item_menu_header, null);
        mMenuRecyclerAdapter.addHeadView(headerView);

        headerView.findViewById(R.id.view_upper_item_menu_header).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnMenuListener) {
                    mOnMenuListener.onUpperCloseClick();
                }
            }
        });

        findViewById(R.id.view_upper_item_menu_mask).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnMenuListener) {
                    mOnMenuListener.onUpperMaskClick();
                }
            }
        });

        mMenuRecyclerAdapter.setOnRecyclerItemClickListener(new Callback.OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, String s, int position) {
                if (null != mOnMenuListener) {
                    mOnMenuListener.onUpperItemClick(s, position);
                }
            }
        });
    }

    private void translateInner(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -view.getHeight(), 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    public void translateOuter(final OnAnimatorFinishCallback finishListener) {
        if (null != mRecyclerView) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mRecyclerView, "translationY", 0, -mRecyclerView.getHeight());
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    if (null != finishListener) {
                        finishListener.onFinish(true);
                    }
                }
            });
            animator.setDuration(300);
            animator.start();
        } else {
            if (null != finishListener) {
                finishListener.onFinish(false);
            }
        }
    }

    public void setData(List<String> selectionList) {
        if (null != mMenuRecyclerAdapter) {
            mMenuRecyclerAdapter.setDataList(selectionList, true);
        }
    }

    private class UpperMenuRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<String> {
        private Callback.OnRecyclerItemClickListener<String> mOnItemClickListener;

        @Override
        public int getItemRes() {
            return R.layout.item_view_upper_item_menu;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            final String itemModel = getItem(position);

            holder.setText(R.id.item_view_upper_item_menu, itemModel);
            holder.getItemView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(holder, itemModel, holder.getAdapterPosition());
                    }
                }
            });
        }

        private void setOnRecyclerItemClickListener(Callback.OnRecyclerItemClickListener<String> listener) {
            this.mOnItemClickListener = listener;
        }
    }

    public void setOnUpperItemMenuListener(OnUpperItemMenuListener listener) {
        this.mOnMenuListener = listener;
    }

    public interface OnUpperItemMenuListener {
        /**
         * 覆盖层，点击
         */
        void onUpperMaskClick();

        /**
         * 头部，关闭icon点击
         */
        void onUpperCloseClick();

        /**
         * 选择项，进行点击
         *
         * @param content  点击的内容
         * @param position 点击的位置
         */
        void onUpperItemClick(String content, int position);
    }

    public interface OnAnimatorFinishCallback {
        /**
         * 动画结束
         *
         * @param isAnimator 是否执行过动画
         */
        void onFinish(boolean isAnimator);
    }
}
