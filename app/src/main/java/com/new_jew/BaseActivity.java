package com.new_jew;


import android.os.Bundle;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


import com.new_jew.customview.CustomDialog;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhangpei on 2016/6/21.
 */
public abstract class BaseActivity extends AppCompatActivity {
    ;
    protected ActionBar actionbar;
    protected CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(setRootView());
        actionbar = getSupportActionBar();
        dialog = new CustomDialog(this);
        initLayout();
        initValue();
        initListener();

        // 解析注册结果

    }

    abstract protected void initLayout();

    /**
     * 初始化值，比如setAdapter，setText
     */
    abstract protected void initValue();

    /**
     * 初始化监听器，比如setOnClickListener
     */
    abstract protected void initListener();

    /**
     * 设置根视图layout
     *
     * @return
     */
    abstract protected int setRootView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) this.finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
