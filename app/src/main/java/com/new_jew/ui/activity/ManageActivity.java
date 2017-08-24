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
import com.new_jew.adpter.Manage_Adpter;
import com.new_jew.bean.Manage_Bean;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;

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
 * Created by zhangpei on 2016/11/17.
 */
public class ManageActivity extends BaseActivity {
    @ViewInject(R.id.recyler_view)
    private RecyclerView recyler_view;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.title_image)
    private TextView title_image;
    @ViewInject(R.id.back)
    private LinearLayout back;
    private List<Manage_Bean> mlist;
    private Manage_Adpter adpter;

    @Override
    protected void initLayout() {
        x.view().inject(this);
        mlist = new ArrayList<>();
        adpter = new Manage_Adpter(getApplicationContext(), mlist);
        recyler_view.setLayoutManager(new LinearLayoutManager(this));
        recyler_view.setAdapter(adpter);


    }

    @Override
    protected void initValue() {
        title_text.setText("人员管理");
        title_image.setText("增加");

    }

    @Override
    protected void initListener() {
        title_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(ManageActivity.this,AddCollectorActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_manage;
    }

    void getcollection_company_staffs() {
        dialog.show();
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
                dialog.dismiss();
                mlist.clear();
                Log.e("result", result.toString());
                try {
                    JSONArray ajson = new JSONArray(result.toString());
                    for (int i = 0; i < ajson.length(); i++) {

                        JSONObject json = new JSONObject(ajson.get(i).toString());
                        mlist.add(new Manage_Bean(Api.pic+json.getString("thumbnail"), json.getString("fullname"), json.getString("telephone"), json.getString("collecting_count"),
                                json.getString("order_count")));
                    }
                    adpter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure() {
                dialog.dismiss();


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getcollection_company_staffs();
    }
}
