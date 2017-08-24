package com.new_jew.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.DebtDetailsAdpter;
import com.new_jew.bean.DebtDetailsBean;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.toolkit.UserUtil;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistAdjunctFragment;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistCarMsgFragment;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistDebtMsgFragment;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistDebtorMsgFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/23.
 */
public class MyListDetailsActivity extends BaseActivity {
    private TabLayout mylistdetails_tabs;
    private ViewPager mylistdetails_viewpager;
    private List<Fragment> list;
    private String title[] = {"债务信息", "债务人信息", "车辆信息", "附件"};
    private View mylistdetails_title;
    private LinearLayout back;
    private RelativeLayout isshow_uploading;
    private TextView title_text;

    @Override
    protected void initLayout() {
        Log.e(">>>>>>>>>>>>>>>", Constants.id);
        mylistdetails_tabs = (TabLayout) this.findViewById(R.id.mylistdetails_tabs);
        mylistdetails_viewpager = (ViewPager) this.findViewById(R.id.mylistdetails_viewpager);
        mylistdetails_title = this.findViewById(R.id.mylistdetails_title);
        back = (LinearLayout) mylistdetails_title.findViewById(R.id.back);
        title_text = (TextView) mylistdetails_title.findViewById(R.id.title_text);
        isshow_uploading = (RelativeLayout) this.findViewById(R.id.isshow_uploading);

    }

    @Override
    protected void initValue() {
        isshow_uploading.setVisibility(View.GONE);
        title_text.setText("委单详情");
        list = new ArrayList<>();
        list.add(new MylistDebtMsgFragment());
        list.add(new MylistDebtorMsgFragment());
        list.add(new MylistCarMsgFragment());
        list.add(new MylistAdjunctFragment());

        mylistdetails_viewpager.setOffscreenPageLimit(3);
        mylistdetails_viewpager.setAdapter(new DebtDetailsAdpter(getSupportFragmentManager(), list, title));
        mylistdetails_tabs.setupWithViewPager(mylistdetails_viewpager);

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_mylistdetails;
    }


}
