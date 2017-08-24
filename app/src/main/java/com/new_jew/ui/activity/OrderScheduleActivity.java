package com.new_jew.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.CollectionRecordAdpter;
import com.new_jew.bean.CollectionRecordBean;
import com.new_jew.customview.ConstantDialog;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.CollectionRecordItemOnClick;
import com.new_jew.toolkit.DetailsOnClick;
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.ui.collectoractivity.Accessory_Activity;

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
 * Created by zhangpei on 2016/11/28.
 */
public class OrderScheduleActivity extends BaseActivity implements View.OnClickListener, DetailsOnClick, CollectionRecordItemOnClick {

    private TextView order_details;
    private TextView order_pick;
    private TextView collection_day;
    private TextView surplus_days;
    @ViewInject(R.id.order_chschedule_recyclerview)
    private RecyclerView order_chschedule_recyclerview;
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.single_back)
    private TextView single_back;
    @ViewInject(R.id.apply_postpone)
    private TextView apply_postpone;
    @ViewInject(R.id.apply_accounts)
    private TextView apply_accounts;
    private List<CollectionRecordBean> list;
    private CollectionRecordAdpter adpter;
    private ConstantDialog mdialog;
    private List<String> mData;
    private String ishave;
    private int id = 0;

    @Override
    protected void initLayout() {
        x.view().inject(this);
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");


    }

    @Override
    protected void initValue() {

    }

    @Override
    protected void initListener() {


    }

    @Override
    protected int setRootView() {
        return R.layout.layout_order_schedule;
    }

    void get_process(String id) {
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + id + "/process/");
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                list.clear();
                Log.e("result", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String d = TimeUtil.getformatdata1(jsonObject.getString("collecting_day"));
//                    order_pick.setText(d);
                    mData.add(d);
                    mData.add(String.valueOf(TimeUtil.getformatdata(jsonObject.getString("collecting_day"))) + "天");
                    mData.add("剩余" + String.valueOf(jsonObject.getInt("collecting_days") - TimeUtil.getformatdata(jsonObject.getString("collecting_day"))) + "天");
                    ishave = jsonObject.getString("handover_evidence");
                    if (ishave.equals("null")) {
                        apply_accounts.setText("申请结算");
                    } else {
                        single_back.setVisibility(View.GONE);
                        apply_postpone.setVisibility(View.GONE);
                        apply_accounts.setText("查看交接证明");

                    }
                    adpter.notifyDataSetChanged();
//                    collection_day.setText(String.valueOf(TimeUtil.getformatdata(jsonObject.getString("collecting_day")))+"天");
//                    surplus_days.setText("剩余"+String.valueOf(jsonObject.getInt("collecting_days")-TimeUtil.getformatdata(jsonObject.getString("collecting_day")))+"天");
                    JSONArray ajson = new JSONArray(jsonObject.getString("collection_records"));
                    if (ajson.length() == 0) {

                        return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(ajson.get(i).toString());
                        list.add(new CollectionRecordBean(jsonObject1.getString("detail"), "", "", jsonObject1.getString("created"), "", jsonObject1.getString("position"),
                                jsonObject1.getString("collection_name"), "", "", "", "", "", "", ""));

                    }
                    adpter.notifyDataSetChanged();
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.single_back:
                mdialog = new ConstantDialog(this, R.style.Dialog);
                mdialog.setText("确定退单吗？");
                mdialog.setnegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdialog.dismiss();
                        give_up();
                    }
                });
                mdialog.setpositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdialog.dismiss();
                    }
                });
                mdialog.show();

                break;
            case R.id.apply_postpone:
                mdialog = new ConstantDialog(this, R.style.Dialog);
                mdialog.setText("确定延期吗？");
                mdialog.setnegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdialog.dismiss();
                        collecting_days();
                    }
                });
                mdialog.setpositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdialog.dismiss();
                    }
                });
                mdialog.show();
                break;
            case R.id.apply_accounts:
                Log.e("JSON", String.valueOf(ishave));
                if (ishave.equals("null")) {
                    apply_accounts.setText("提交交接证明");
                    startActivity(new Intent(this, AssociateProveActivity.class));
                } else {
                    apply_accounts.setText("查看交接证明");
                    startActivity(new Intent(this, AssociateProveActivity1.class));
                }

//
                break;


        }

    }

    @Override
    public void detailsOnClick(View v) {
        Intent intent = new Intent(OrderScheduleActivity.this, MyListDetailsActivity.class);
        startActivity(intent);
    }

    //申请退单
    void give_up() {
        dialog.show();
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + Constants.id + "/give_up/");
        HttpUtils.Posthttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "申请已提交", Toast.LENGTH_SHORT).show();
                Log.e("申请退单", result.toString());
                finish();

            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "申请失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //申请延期
    void collecting_days() {
        dialog.show();
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + Constants.id + "/collecting_days/");
        params.addBodyParameter("collecting_days", "7");
        HttpUtils.Posthttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "申请已提交！", Toast.LENGTH_SHORT).show();
//                Log.e("申请延期", result.toString());
                finish();

            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "申请失败！", Toast.LENGTH_SHORT).show();

            }
        });

    }

    void apply_settlement() {
        dialog.show();
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + Constants.id + "/apply_settlement/");
        HttpUtils.Posthttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                dialog.dismiss();

                Log.e("申请结算", result.toString());
                finish();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();

            }
        });

    }

    @Override
    public void ItemOnClick(View v, int position) {
//        Toast.makeText(getApplicationContext(),"查看更多",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Accessory_Activity.class);
        intent.putExtra("id", position - 1);
        startActivity(intent);

//        Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (id == 1) {
            single_back.setVisibility(View.GONE);
            apply_postpone.setVisibility(View.GONE);
        } else if (id == 2) {
            apply_accounts.setVisibility(View.GONE);

        } else if (id == 0) {
            apply_accounts.setText("申请结算");

        }
        title_text.setText("委单进度");
        list = new ArrayList<>();
        mData = new ArrayList<>();
//        for (int i=0; i<10;i++){
////            JSONObject jsonObject1=new JSONObject(ajson.get(i).toString());
//            list.add(new CollectionRecordBean("20123132","","","2016-11-21T19:44:39.433107Z","","成都市",
//                   "杨杨","","","","","","",""));
//
//        }
        adpter = new CollectionRecordAdpter(list, mData, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        order_chschedule_recyclerview.setLayoutManager(layoutManager);
        order_chschedule_recyclerview.setHasFixedSize(true);
        order_chschedule_recyclerview.setAdapter(adpter);
        get_process(Constants.id);

        adpter.SetDetailsOnClick(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        single_back.setOnClickListener(this);
        apply_postpone.setOnClickListener(this);
        apply_accounts.setOnClickListener(this);
        adpter.SetItemOnClick(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
