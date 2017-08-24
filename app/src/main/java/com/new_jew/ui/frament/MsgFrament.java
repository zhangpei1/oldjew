package com.new_jew.ui.frament;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.adpter.MsgAdpter;
import com.new_jew.bean.MsgBean;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.TimeUtil;

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
 * Created by zhangpei on 2016/11/9.
 */
public class MsgFrament extends BaseFrament {
    private LinearLayout back;
    private TextView title_text;
    private PullToRefreshListView msg_list;
    private List<MsgBean> mlist;
    //    private MsgAdpter adpter;
    private SimpleAdapter adapter;
    private List<Map<String, String>> list;
    private int pagers = 1;

    @Override
    protected void initLayout() {
        back = (LinearLayout) view.findViewById(R.id.back);
        title_text = (TextView) view.findViewById(R.id.title_text);
        msg_list = (PullToRefreshListView) view.findViewById(R.id.msg_list);
        mlist = new ArrayList<>();
//        adpter=new MsgAdpter(getActivity(),mlist);
        msg_list.setMode(PullToRefreshBase.Mode.BOTH);


    }

    @Override
    protected void initValue() {
        back.setVisibility(View.GONE);
        title_text.setText("消息");
        list = new ArrayList<>();
        adapter = new SimpleAdapter(getActivity(), list, R.layout.layout_msg_item, new String[]{"created", "title", "content"}, new int[]{R.id.msg_data,
                R.id.title, R.id.msg_content});
//        msg_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        msg_list.setAdapter(adapter);
        pagers = 1;
        getmsg(pagers,true);
    }

    @Override
    protected void initListener() {

        msg_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagers = 1;
                getmsg(pagers,false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagers++;
                getmsg(pagers,false);
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_msgframent;
    }

    void getmsg(final int pagers, final boolean isfirst) {
        if (isfirst==false){

            dialog.show();
        }
        RequestParams params = new RequestParams(Api.messages.messages);
        params.addParameter("limit", 7);
        params.addParameter("offset", (pagers - 1) * 7);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                if (isfirst==false){

                    dialog.dismiss();
                }
                if (pagers == 1) {
                    mlist.clear();
                }

                msg_list.onRefreshComplete();
                Log.e("消息", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    JSONArray ajs = new JSONArray(jsonObject.getString("results"));
                    if (ajs.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < ajs.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(ajs.get(i).toString());
                        mlist.add(new MsgBean(jsonObject1.getString("id"), jsonObject1.getString("created"), jsonObject1.getString("title"),
                                jsonObject1.getString("content")));
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("created", TimeUtil.getformatdata1(jsonObject1.getString("created")));
                        map.put("title", jsonObject1.getString("title"));
                        map.put("content", jsonObject1.getString("content"));
                        list.add(map);
                    }


                    adapter.notifyDataSetChanged();
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
    }
}
