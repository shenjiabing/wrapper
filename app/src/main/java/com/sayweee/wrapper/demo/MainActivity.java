package com.sayweee.wrapper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.sayweee.logger.Logger;
import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.base.view.WrapperMvpActivity;
import com.sayweee.wrapper.core.model.BaseVo;
import com.sayweee.wrapper.core.model.FailureVo;
import com.sayweee.wrapper.utils.Utils;


public class MainActivity extends WrapperMvpActivity<BaseMvpPresenter> {
    GoodsMarkerView mvTag;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState, Intent intent) {
        mvTag = findViewById(R.id.mv_tag);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void onResponse(String url, BaseVo dataVo) {
        Logger.json(Logger.TAG, url, dataVo);
    }

    @Override
    public boolean onError(String url, FailureVo dataVo) {
        Logger.json(Logger.TAG, url, dataVo);
        return super.onError(url, dataVo);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                test1();
                break;
            case R.id.btn_2:
                test2();
                break;
            case R.id.btn_3:
                test3("新品");
                break;
            case R.id.btn_4:
                test3("New");
                break;
            case R.id.btn_5:
                test3("限购");
                break;

        }
    }

    private void test1() {
        presenter.enqueue(presenter.getHttpService(HttpApi.class).getEmailLoginInfo("heardown@163.com", "123456", ""));
    }

    private void test2() {
        presenter.enqueue(presenter.getHttpService(HttpApi.class).getEmailLoginInfo("jingyu@163.com", "123456", ""));
    }


    private void test3(String text) {
        mvTag.setText(text);
    }

    private void testJson() {
        String s = "{\"result\":false,\"message\":\"The argument is empty\",\"object\":\"The argument is empty\"}";
        TestVo parse = Utils.parse(new Gson(), TestVo.class, s);
    }
    private void testJson1() {
        String s = "{\"result\":false,\"message\":\"The argument is empty\",\"object\":false";
        TestVo parse = Utils.parse(new Gson(), TestVo.class, s);
    }

    private void testJson2() {
        String s = "{\"result\":false,\"message\":\"The argument is empty\",\"object\":2";
        TestVo parse = Utils.parse(new Gson(), TestVo.class, s);
    }
    private void testJson3() {
        String s = "{\"result\":false,\"message\":\"The argument is empty\",\"object\":0.01";
        TestVo parse = Utils.parse(new Gson(), TestVo.class, s);
    }
}
