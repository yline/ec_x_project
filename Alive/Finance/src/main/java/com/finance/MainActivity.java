package com.finance;

import android.os.Bundle;
import android.view.View;

import com.finance.util.FinanceUtils;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_equal_alipay).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinanceUtils.calculateEqualMonthlyAlipay(10000, FinanceUtils.interestDay2Month(4.0f), 12);
            }
        });

        findViewById(R.id.btn_equal).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinanceUtils.calculateEqualMonthly(10000, FinanceUtils.interestDay2Month(4.0f), 12);
            }
        });

        findViewById(R.id.btn_first_alipay).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinanceUtils.calculateFirstInterestAlipay(10000, FinanceUtils.interestDay2Month(4.0f), 12);
            }
        });

        findViewById(R.id.btn_matching).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FinanceUtils.calculateMatching(10000, FinanceUtils.interestDay2Month(4.0f), 12);
            }
        });
    }
}
