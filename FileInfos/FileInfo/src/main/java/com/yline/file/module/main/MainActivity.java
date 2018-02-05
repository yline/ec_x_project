package com.yline.file.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yline.base.BaseAppCompatActivity;
import com.yline.base.BaseFragment;
import com.yline.file.IApplication;
import com.yline.file.R;
import com.yline.file.module.file.helper.FileInfoLoadService;

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

    private StorageFragment mStorageFragment;
    private ClassifyFragment mClassifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<BaseFragment> fragmentList = new ArrayList<>();
        final List<String> titleList = new ArrayList<>();

        fragmentList.add(new ClassifyFragment());
        titleList.add("分类");

        fragmentList.add(new StorageFragment());
        titleList.add("手机");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        FileInfoLoadService.launcher(IApplication.getApplication(), false);
    }
}
