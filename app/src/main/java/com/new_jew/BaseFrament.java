package com.new_jew;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.new_jew.customview.CustomDialog;

/**
 * Created by zhangpei on 2016/7/4.
 */
public abstract class BaseFrament extends Fragment {
    protected View view;
    protected CustomDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(setRootView(), null);
        dialog = new CustomDialog(getActivity());
        initLayout();
        initValue();
        initListener();
        return view;
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
