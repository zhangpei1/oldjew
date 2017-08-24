package com.new_jew.ui.collectorframent;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.bean.SimpleFragmentPagerAdapter;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.TabLongUtil;
import com.new_jew.ui.collectorframent.myorderframent.CollectorCompleted;
import com.new_jew.ui.collectorframent.myorderframent.CollectorOnCollection;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/12/2.
 */
public class MylistOrderFragment extends BaseFrament {
    private TabLayout tabLayout;
    private ViewPager collector_viewpager;
    private List<Fragment> mlist;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private String tab_name[];
    private List<String> tabnumber;
    private TabLayout.Tab mtabs;

    @Override
    protected void initLayout() {
        tabLayout = (TabLayout) view.findViewById(R.id.collector_tab);
        collector_viewpager = (ViewPager) view.findViewById(R.id.collector_viewpager);
        mlist = new ArrayList<>();
        tabnumber = new ArrayList<>();
        tabnumber.add("");
        tabnumber.add("");
        mlist.add(new CollectorOnCollection());
        mlist.add(new CollectorCompleted());
        tab_name = new String[]{"催收中", "已完成"};
        pagerAdapter = new SimpleFragmentPagerAdapter(getChildFragmentManager(), getActivity(), mlist, tab_name, tabnumber);
        collector_viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(collector_viewpager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            mtabs = tabLayout.getTabAt(i);
            mtabs.setCustomView(pagerAdapter.getTabView(i, tabnumber, tab_name));

        }

        TabLongUtil.setlong(tabLayout, 8f, 8f);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void initValue() {

    }

    @Override
    protected void initListener() {


    }

    @Override
    protected int setRootView() {
        return R.layout.mylistorderfragment_layout;
    }

    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.tab_text);
        TextView tab_number = (TextView) view.findViewById(R.id.tab_number);
        txt_title.setTextColor(Color.parseColor("#3290F2"));
        tab_number.setTextColor(Color.parseColor("#fE5B4C"));
        if (txt_title.getText().toString().equals("催收中")) {

            collector_viewpager.setCurrentItem(0);
        } else if (txt_title.getText().toString().equals("已完成")) {
            collector_viewpager.setCurrentItem(1);
        }
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();

        TextView txt_title = (TextView) view.findViewById(R.id.tab_text);
        TextView tab_number = (TextView) view.findViewById(R.id.tab_number);
        txt_title.setTextColor(Color.parseColor("#000000"));
        tab_number.setTextColor(Color.parseColor("#878787"));

    }

    public void getcount() {

        RequestParams params = new RequestParams(Api.staff_car_orders.staff_car_orders + "count/");
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                tabnumber.clear();
                Log.e("数量", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    tabnumber.add(String.valueOf(jsonObject.getInt("催收中")));
                    tabnumber.add(String.valueOf(jsonObject.getInt("已完成")));
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {

                        mtabs = tabLayout.getTabAt(i);
                        TextView V = (TextView) mtabs.getCustomView().findViewById(R.id.tab_number);
                        V.setText("(" + tabnumber.get(i) + ")");

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

    @Override
    public void onResume() {
        super.onResume();
        getcount();
    }
}
