package com.new_jew.ui.collectorframent;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.adpter.MsgAdpter;
import com.new_jew.bean.MsgBean;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/12/2.
 */
public class CollectorMsgFragment extends BaseFrament{
    private RecyclerView collector_recyclerciew;
    private LinearLayout back;
    private TextView title_text;
    private List<MsgBean> mlist;
    private MsgAdpter adpter;
    private SwipeRefreshLayout collector_msg_refresh;
    @Override
    protected void initLayout() {
        collector_recyclerciew= (RecyclerView) view.findViewById(R.id.collector_recyclerciew);
        mlist=new ArrayList<>();
        adpter=new MsgAdpter(getActivity(),mlist);
        collector_msg_refresh= (SwipeRefreshLayout) view.findViewById(R.id.collector_msg_refresh);
    }

    @Override
    protected void initValue() {
        collector_recyclerciew.setLayoutManager(new LinearLayoutManager(getActivity()));
        collector_recyclerciew.setAdapter(adpter);
        getmsg();

    }

    @Override
    protected void initListener() {
        collector_msg_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getmsg();
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.collectormsgfragment_layout;
    }

    void getmsg(){
        RequestParams params=new RequestParams(Api.messages.messages);
        params.addParameter("limit", 100);
        params.addParameter("offset", 0);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                mlist.clear();
                collector_msg_refresh.setRefreshing(false);
                Log.e("result",result.toString());
                try {
                    JSONObject jsonObject=new JSONObject(result.toString());
                    JSONArray ajs=new JSONArray(jsonObject.getString("results"));
                    for (int i=0;i<ajs.length();i++){
                        JSONObject jsonObject1=new JSONObject(ajs.get(i).toString());
                        mlist.add(new MsgBean(jsonObject1.getString("id"),jsonObject1.getString("created"),jsonObject1.getString("title"),
                                jsonObject1.getString("content")));

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
}
