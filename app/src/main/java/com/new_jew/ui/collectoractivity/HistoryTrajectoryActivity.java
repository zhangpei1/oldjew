package com.new_jew.ui.collectoractivity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.HistoryTrajectoryAdpter;
import com.new_jew.bean.HistoryTrajectoryBean;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.ButtonClicklistener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/12/5.
 */
public class HistoryTrajectoryActivity extends BaseActivity implements View.OnClickListener, ButtonClicklistener {
    @ViewInject(R.id.historical_recycler)
    private RecyclerView historical_recycler;
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    private List<HistoryTrajectoryBean> mlist;
    private HistoryTrajectoryAdpter adpter;
    private SwipeRefreshLayout historical_refresh;


    @Override
    protected void initLayout() {
        x.view().inject(this);
    }

    @Override
    protected void initValue() {
        historical_refresh = (SwipeRefreshLayout) this.findViewById(R.id.historical_refresh);
        title_text.setText("历史催记");
        mlist = new ArrayList<>();
        adpter = new HistoryTrajectoryAdpter(this, mlist);
        historical_recycler.setLayoutManager(new LinearLayoutManager(this));
        historical_recycler.setAdapter(adpter);

        getcollection_records(Constants.id);

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        historical_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getcollection_records(Constants.id);
            }
        });
        adpter.setButtonClicklistener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_history_trajectory;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;


        }
    }

    void getcollection_records(String id) {
        RequestParams params = new RequestParams(Api.staff_car_orders.staff_car_orders + id + "/collection_records/");

        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                Log.e("result", result.toString());
                mlist.clear();

                try {
                    JSONArray jsonArray = new JSONArray(result.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        mlist.add(new HistoryTrajectoryBean(jsonObject.getString("position"), jsonObject.getString("detail"), "",
                                "", "", jsonObject.getString("created"),
                                ""));


                    }
                    adpter.notifyDataSetChanged();
                    historical_refresh.setRefreshing(false);
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
    public void setButtonClicklistener(View v, int position) {
//        Toast.makeText(getApplicationContext(),"查看更多",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Accessory_Activity.class);
        intent.putExtra("id", position);
        startActivity(intent);
    }
}
