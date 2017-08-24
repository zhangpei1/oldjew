
package com.new_jew.ui.frament.mylistframent;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.adpter.PrepareSendAdpter;
import com.new_jew.bean.DebtAdpter;
import com.new_jew.bean.Manage_Bean;
import com.new_jew.bean.PrepareSendBean;
import com.new_jew.bean.PrepareSendOrdersAdpter;
import com.new_jew.customview.SendOderDialog;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.CollectionRecordItemOnClick;
import com.new_jew.toolkit.MyItemClickListener;
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.ui.activity.MyListDetailsActivity;
import com.new_jew.ui.activity.OrderScheduleActivity;
import com.new_jew.ui.frament.MylistFrament;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by zhangpei on 2016/11/16.
 */
public class PrepareSendOrders extends BaseFrament implements CollectionRecordItemOnClick {
    private PullToRefreshListView id_recyclerview;
    private List<PrepareSendBean> mDatas;
    private PrepareSendOrdersAdpter mAdapter;
    private PrepareSendAdpter adpter;
    private List<String> id_list;
    private SendOderDialog sendOderDialog;
    private List<String> name_list;
    private String collect_id = "";//催收员的ID；
    private List<String> collection_id;//催收员ID list
    private int pagers = 1;
    private ImageView image;

    @Override
    protected void initLayout() {
        id_recyclerview = (PullToRefreshListView) view.findViewById(R.id.id_recyclerview);
        image = (ImageView) view.findViewById(R.id.image);
        mDatas = new ArrayList<>();
        id_list = new ArrayList<>();

        adpter = new PrepareSendAdpter(getActivity(), mDatas);
        id_recyclerview.setAdapter(adpter);

//        initData();

    }

    @Override
    protected void initValue() {
        id_recyclerview.setMode(PullToRefreshBase.Mode.BOTH);

    }

    @Override
    protected void initListener() {
        adpter.setCollectionRecordItemOnClick(this);
        id_recyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderScheduleActivity.class);
                Constants.id = id_list.get(position - 1);
                intent.putExtra("id", 2);
                startActivity(intent);
            }
        });

        id_recyclerview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ((MylistFrament) (PrepareSendOrders.this.getParentFragment())).getcount();
                pagers = 1;
                get_my_car_orders(pagers);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagers++;
                get_my_car_orders(pagers);
            }
        });
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_prepare_send_orders;
    }

    void get_my_car_orders(final int pages) {
        dialog.show();
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + "?");
        params.addParameter("order_state", "待派单");
        params.addParameter("limit", 7);
        params.addParameter("offset", (pages - 1) * 7);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                dialog.dismiss();
                id_recyclerview.onRefreshComplete();
                Log.e("..........", result.toString());
                if (pages == 1) {

                    mDatas.clear();
                    id_list.clear();
                }

                try {
                    JSONObject base_json = new JSONObject(result.toString());
                    JSONArray ajson = new JSONArray(base_json.getString("results"));
                    if (ajson.length() == 0) {
                        id_recyclerview.onRefreshComplete();
                        if (pages == 1) {
                            image.setVisibility(View.VISIBLE);
                            adpter.notifyDataSetChanged();
                        }
                        return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {
                        JSONObject json = new JSONObject(ajson.get(i).toString());
                        int d = TimeUtil.getformatdata(json.getString("collecting_day"));
                        Log.e("d", String.valueOf(d));
                        int days = json.getInt("collecting_days");
                        Log.e("days", String.valueOf(days));
                        int a = json.getInt("collecting_days") - d;
                        Log.e("a", String.valueOf(a));
                        double f = (double) a / days;
                        Log.e("f", String.valueOf(f));
                        float c = (float) (f * 100);
                        Log.e("c", String.valueOf(c));
                        mDatas.add(new PrepareSendBean(json.getString("get_level") + "级", "[" + json.getString("loan_type") + "]", json.getString("demands_of_collection") + "业务", json.getString("vehicle_brand_name"),
                                json.getString("vehicle_plate_number"), json.getString("vehicle_possible_address"), a, c));

                        id_list.add(json.getString("id"));
                    }
                    image.setVisibility(View.GONE);
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
    void get_my_car_orders(final int pages,boolean isfirst) {
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + "?");
        params.addParameter("order_state", "待派单");
        params.addParameter("limit", 7);
        params.addParameter("offset", (pages - 1) * 7);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                id_recyclerview.onRefreshComplete();
                Log.e("..........", result.toString());
                if (pages == 1) {

                    mDatas.clear();
                    id_list.clear();
                }

                try {
                    JSONObject base_json = new JSONObject(result.toString());
                    JSONArray ajson = new JSONArray(base_json.getString("results"));
                    if (ajson.length() == 0) {
                        id_recyclerview.onRefreshComplete();
                        if (pages == 1) {
                            image.setVisibility(View.VISIBLE);
                            adpter.notifyDataSetChanged();
                        }
                        return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {
                        JSONObject json = new JSONObject(ajson.get(i).toString());
                        int d = TimeUtil.getformatdata(json.getString("collecting_day"));
                        Log.e("d", String.valueOf(d));
                        int days = json.getInt("collecting_days");
                        Log.e("days", String.valueOf(days));
                        int a = json.getInt("collecting_days") - d;
                        Log.e("a", String.valueOf(a));
                        double f = (double) a / days;
                        Log.e("f", String.valueOf(f));
                        float c = (float) (f * 100);
                        Log.e("c", String.valueOf(c));
                        mDatas.add(new PrepareSendBean(json.getString("get_level") + "级", "[" + json.getString("loan_type") + "]", json.getString("demands_of_collection") + "业务", json.getString("vehicle_brand_name"),
                                json.getString("vehicle_plate_number"), json.getString("vehicle_possible_address"), a, c));

                        id_list.add(json.getString("id"));
                    }
                    image.setVisibility(View.GONE);
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
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        pagers = 1;
        get_my_car_orders(pagers,true);
    }

    @Override
    public void ItemOnClick(View v, int position) {
        name_list = new ArrayList<>();
        collection_id = new ArrayList<>();
        getcollection_company_staffs(position);
    }

    void getcollection_company_staffs(final int position) {

        RequestParams params = new RequestParams(Api.collection_company_staffs.collection_company_staffs);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                collect_id = "";
                Log.e("result", result.toString());
                try {
                    JSONArray ajson = new JSONArray(result.toString());
                    if (ajson.length() == 0) {
                        Toast.makeText(getActivity(), "您还没有创建催收员！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {

                        JSONObject json = new JSONObject(ajson.get(i).toString());
                        name_list.add(json.getString("fullname"));
                        collection_id.add(json.getString("id"));
                    }
                    Constants.id = id_list.get(position);

                    sendOderDialog = new SendOderDialog(getActivity(), name_list);
                    sendOderDialog.setpositiveButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendOderDialog.dismiss();
                        }
                    });
                    sendOderDialog.setnegativeButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (collect_id.equals("")) {
                                Toast.makeText(getActivity(), "请选择催收员", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            sendOderDialog.dismiss();
                            distribute(collect_id, Constants.id);
                        }

                    });

                    sendOderDialog.setitemClick(new MyItemClickListener() {
                        @Override
                        public void setItemClickListener(View v, int position) {
                            sendOderDialog.adpter.setpitch(position);
                            sendOderDialog.adpter.notifyDataSetChanged();
                            collect_id = collection_id.get(position);
                        }
                    });
                    sendOderDialog.show();

//                    sendOderDialog.adpter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure() {

            }
        });

    }

    public void distribute(String id, String id_list1) {
        dialog.show();
        final RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + id_list1 + "/distribute/");
        params.addBodyParameter("collection", id);
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
                Log.e("派单", result.toString());
                Toast.makeText(getActivity(), "派单成功", Toast.LENGTH_SHORT).show();
                ((MylistFrament) PrepareSendOrders.this.getParentFragment()).getcount();//刷新父控件的数据
                pagers = 1;
                get_my_car_orders(pagers);
                collect_id = "";

            }

            @Override
            public void onFailure() {
                Toast.makeText(getActivity(), "派单失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
