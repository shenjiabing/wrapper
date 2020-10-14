package com.sayweee.app2.test;


import android.os.Bundle;
import android.view.View;


import com.sayweee.app2.R;
import com.sayweee.app2.databinding.ActivityTestBinding;
import com.sayweee.wrapper.core.view.WrapperBindingActivity;


/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class TestActivity extends WrapperBindingActivity<TestViewModel, ActivityTestBinding> implements View.OnClickListener {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_test;
    }

    /**
     * model层数据关联
     */
    @Override
    public void initView(Bundle savedInstanceState) {
        binding.btnTestN.setOnClickListener(this);
        binding.btnTestVoid.setOnClickListener(this);
        binding.btnTestNormal.setOnClickListener(this);
    }


    @Override
    public void attachModel() {

    }

    /**
     * 数据加载
     */
    @Override
    public void loadData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_n:
                viewModel.testN();
                break;
            case R.id.btn_test_void:
                viewModel.testVoid();
                break;
            case R.id.btn_test_normal:
                viewModel.testNormal();
                break;
        }
    }
}
