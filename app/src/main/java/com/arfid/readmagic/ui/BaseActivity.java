package com.arfid.readmagic.ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.util.Log;


/**
 * 功能：Activity的基类
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/3/13
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 当前显示的Activity是========" + this.getClass().getName());
        beforeSetContentView();
        setContentView(getLayoutId());
        init();
    }

    public void beforeSetContentView() {

    }

    private void init() {
        beforeInit();
        initView();
        initListener();
        initData();
        afterInit();
    }

    /**
     * 钩子方法：得到layout的资源Id
     *
     * @return
     */
    public abstract int getLayoutId();

    public void beforeInit() {

    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 注册监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化之后
     */
    protected void afterInit() {

    }

    /**
     * 得到资源文件中得String
     *
     * @param resId
     * @return
     */
    protected String getStr(int resId) {

        return getResources().getString(resId);
    }

    public void showLoadingDialog(String tip){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("操作");
        progressDialog.setMessage(tip);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    progressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    protected void setTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setReturnTransition(new Fade());
            getWindow().setSharedElementReturnTransition(new ChangeBounds());
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }
    }


}