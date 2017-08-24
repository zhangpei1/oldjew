package com.new_jew.ui.collectoractivity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.DebtDetailsAdpter;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.UserUtil;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistAdjunctFragment;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistCarMsgFragment;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistDebtMsgFragment;
import com.new_jew.ui.frament.mylistdetailsfragment.MylistDebtorMsgFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/12/5.
 */
public class CollectorMyordersDetials extends BaseActivity implements View.OnClickListener {
    private TabLayout mylistdetails_tabs;
    private ViewPager mylistdetails_viewpager;
    private List<Fragment> list;
    private String title[] = {"债务信息", "债务人信息", "车辆信息", "附件"};
    private View mylistdetails_title;
    private LinearLayout back;
    private TextView title_text, title_image;
    private RelativeLayout isshow_uploading;
    private TextView upcollection;

    @Override
    protected void initLayout() {
        Log.e(">>>>>>>>>>>>>>>", Constants.id);
        mylistdetails_tabs = (TabLayout) this.findViewById(R.id.mylistdetails_tabs);
        mylistdetails_viewpager = (ViewPager) this.findViewById(R.id.mylistdetails_viewpager);
        mylistdetails_title = this.findViewById(R.id.mylistdetails_title);
        back = (LinearLayout) mylistdetails_title.findViewById(R.id.back);
        title_text = (TextView) mylistdetails_title.findViewById(R.id.title_text);
        title_image = (TextView) mylistdetails_title.findViewById(R.id.title_image);
        isshow_uploading = (RelativeLayout) findViewById(R.id.isshow_uploading);
        upcollection = (TextView) findViewById(R.id.upcollection);
    }

    @Override
    protected void initValue() {

        isshow_uploading.setVisibility(View.VISIBLE);
        title_text.setText("委单详情");
        title_image.setText("历史催记");
        getdebtdetail(Constants.id);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_image.setOnClickListener(this);
        upcollection.setOnClickListener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_mylistdetails;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.title_image:
                startActivity(new Intent(getApplicationContext(), HistoryTrajectoryActivity.class));
                break;
            case R.id.upcollection:
                startActivity(new Intent(getApplicationContext(), Add_trajectory_Activity.class));
                break;
        }

    }


    void getdebtdetail(String id) {
        dialog.show();
        String url = null;


        url = Api.staff_car_orders.staff_car_orders;


        RequestParams params = new RequestParams(url + id + "/");
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                try {
                    dialog.dismiss();
                    Log.e("result", result.toString());
                    JSONObject json = new JSONObject(result.toString());
                    UserUtil.setUserToken(getApplicationContext(), json.toString(), "details");
                    title_text.setText("委单详情");
                    list = new ArrayList<>();
                    list.add(new MylistDebtMsgFragment());
                    list.add(new MylistDebtorMsgFragment());
                    list.add(new MylistCarMsgFragment());
                    list.add(new MylistAdjunctFragment());
                    mylistdetails_viewpager.setAdapter(new DebtDetailsAdpter(getSupportFragmentManager(), list, title));
                    mylistdetails_tabs.setupWithViewPager(mylistdetails_viewpager);
                    if (Constants.type.equals("3")) {

                        isshow_uploading.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

            }
        });

    }
}
