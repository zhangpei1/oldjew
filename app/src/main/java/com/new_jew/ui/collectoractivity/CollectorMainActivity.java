package com.new_jew.ui.collectoractivity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.bean.MainpagerAdpter;
import com.new_jew.customview.CustomDialog;
import com.new_jew.customview.MyViewpager;
import com.new_jew.global.Constants;
import com.new_jew.toolkit.UpdateManager;
import com.new_jew.ui.collectorframent.CollectorCenterFragment;
import com.new_jew.ui.collectorframent.CollectorMsgFragment;
import com.new_jew.ui.collectorframent.MylistOrderFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeRadioButton;

/**
 * Created by zhangpei on 2016/12/2.
 */
public class CollectorMainActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.collector_mian_viewpager)
    private MyViewpager collector_mian_viewpager;
    private Toolbar toolbar;
    private List<Fragment> mlist;
    private BGABadgeRadioButton mylist_radio, msg_radio, center_radio;
    private TextView title;
    private Intent intent;
    private long exitTime = 0;//用于再按一次返回桌面
    private CustomDialog customdialog;

    @Override
    protected void initLayout() {
        x.view().inject(this);
        intent = getIntent();
        UpdateManager.Update(this, false, customdialog);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        title = (TextView) this.findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mylist_radio = (BGABadgeRadioButton) this.findViewById(R.id.mylist_radio);
        msg_radio = (BGABadgeRadioButton) this.findViewById(R.id.msg_radio);
        center_radio = (BGABadgeRadioButton) this.findViewById(R.id.center_radio);

        mlist = new ArrayList<>();
        mlist.add(new MylistOrderFragment());
        mlist.add(new CollectorMsgFragment());
        mlist.add(new CollectorCenterFragment());

    }

    @Override
    protected void initValue() {
        mylist_radio.setChecked(true);
        title.setText("我的委单");
        collector_mian_viewpager.setAdapter(new MainpagerAdpter(getSupportFragmentManager(), mlist));
        collector_mian_viewpager.setOffscreenPageLimit(2);
    }

    @Override
    protected void initListener() {
        mylist_radio.setOnClickListener(this);
        msg_radio.setOnClickListener(this);
        center_radio.setOnClickListener(this);


    }

    @Override
    protected int setRootView() {
        return R.layout.collector_activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mylist_radio:
                collector_mian_viewpager.setCurrentItem(0);
                toolbar.setVisibility(View.VISIBLE);
                title.setText("我的委单");
                break;
            case R.id.msg_radio:
                collector_mian_viewpager.setCurrentItem(1);
                toolbar.setVisibility(View.VISIBLE);
                title.setText("消息");
                break;
            case R.id.center_radio:
                collector_mian_viewpager.setCurrentItem(2);
                toolbar.setVisibility(View.GONE);
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (intent.getExtras().getString("ishow").isEmpty() == false) {
                if (intent.getExtras().getString("ishow").equals("是")) {
                    collector_mian_viewpager.setCurrentItem(1);
                    title.setText("消息");
                    msg_radio.setChecked(true);
                    getIntent().removeExtra("ishow");
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次暂别" + Constants.TITLE_APP, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
