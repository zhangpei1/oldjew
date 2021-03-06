package com.new_jew.ui.collectorframent.myorderframent;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.ui.collectoractivity.CollectorMyordersDetials;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangpei on 2016/11/16.
 */
public class CollectorOnCollection extends BaseFrament {
    private PullToRefreshListView on_collect_listview;
    private List<Map<String, String>> mapList;
    private SimpleAdapter adapter;
    private ArrayList<String> id_list;
    private int pagers = 1;
    private ImageView image;

    @Override
    protected void initLayout() {
        image= (ImageView) view.findViewById(R.id.image);
        on_collect_listview = (PullToRefreshListView) view.findViewById(R.id.on_collect_listview);
        mapList = new ArrayList<>();
        adapter = new SimpleAdapter(getActivity(),
                mapList,
                R.layout.layout_on_collect_collector_item,
                new String[]{"on_collect_level", "on_collect_type", "on_collect_percentage",
                        "surplus_day", "on_collect_car", "on_collect_number",
                        "on_collector_name", "on_collect_location"},
                new int[]{R.id.on_collect_level, R.id.on_collect_type, R.id.on_collect_percentage,
                        R.id.surplus_day, R.id.on_collect_car, R.id.on_collect_number,
                        R.id.on_collector_name, R.id.on_collect_location});
        on_collect_listview.setAdapter(adapter);


    }

    @Override
    protected void initValue() {
        id_list = new ArrayList<>();
        on_collect_listview.setMode(PullToRefreshBase.Mode.BOTH);
        get_my_car_orders(pagers);

    }

    @Override
    protected void initListener() {
        on_collect_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagers = 1;
                get_my_car_orders(pagers);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagers++;
                get_my_car_orders(pagers);
            }
        });
        on_collect_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constants.id = id_list.get(position - 1);
                startActivity(new Intent(getActivity(), CollectorMyordersDetials.class));
            }
        });
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_on_collection;
    }

    void get_my_car_orders(final int pages) {
        dialog.show();
        RequestParams params = new RequestParams(Api.staff_car_orders.staff_car_orders + "?");
        params.addParameter("order_state", "催收中");
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
                Log.e("催收中", result.toString());
                if (pages == 1) {

                    mapList.clear();
                    id_list.clear();
                }

                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    JSONArray ajson = new JSONArray(jsonObject.getString("results"));
                    if (ajson.length()==0){
                        if (pages==1){
                            image.setVisibility(View.VISIBLE);
                        }
                        on_collect_listview.onRefreshComplete();
                  return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {
                        JSONObject json = new JSONObject(ajson.get(i).toString());
                        Map<String, String> map = new HashMap<>();
                        map.put("on_collect_level", json.getString("get_level") + "级");
                        map.put("on_collect_type", "[" + json.getString("loan_type") + "]");
                        map.put("on_collect_percentage", json.getString("demands_of_collection") + "回抵业务");
                        map.put("surplus_day", String.valueOf(json.getInt("collecting_days") - TimeUtil.getformatdata(json.getString("collecting_day"))) + "天");
                        map.put("on_collect_car", json.getString("vehicle_brand_name"));
                        map.put("on_collect_number", json.getString("vehicle_plate_number"));
                        map.put("on_collector_name", json.getString("debtor_name"));
                        map.put("on_collect_location", json.getString("vehicle_possible_address"));
                        id_list.add(json.getString("id"));
                        mapList.add(map);

                    }
                    image.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    on_collect_listview.onRefreshComplete();

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
