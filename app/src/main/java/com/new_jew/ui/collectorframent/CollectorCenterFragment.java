package com.new_jew.ui.collectorframent;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.customview.ConstantDialog;
import com.new_jew.customview.CustomDialog;
import com.new_jew.customview.RoundImageView;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpBean;
import com.new_jew.net.HttpUtils;
import com.new_jew.net.Load_image;
import com.new_jew.toolkit.UpdateManager;
import com.new_jew.toolkit.UserUtil;
import com.new_jew.ui.activity.AboutUsActiviyt;
import com.new_jew.ui.activity.BasicsActivity;
import com.new_jew.ui.activity.LogInActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zhangpei on 2016/12/2.
 */
public class CollectorCenterFragment extends BaseFrament implements View.OnClickListener {
    private RoundImageView head_image;
    private TextView set_data, phonenumber, collection_name, companyname, rank_text;
    private RelativeLayout exit;
    private HttpBean httpBean;
    private RelativeLayout about_us, updata;
    private CustomDialog customdialog;
    private NetworkInfo info;
    private ConnectivityManager connectivity;

    @Override
    protected void initLayout() {
        customdialog = new CustomDialog(getActivity());
        phonenumber = (TextView) view.findViewById(R.id.phonenumber);
        collection_name = (TextView) view.findViewById(R.id.collection_name);
        companyname = (TextView) view.findViewById(R.id.companyname);
        rank_text = (TextView) view.findViewById(R.id.rank_text);
        exit = (RelativeLayout) view.findViewById(R.id.exit);
        set_data = (TextView) view.findViewById(R.id.set_data);
        head_image = (RoundImageView) view.findViewById(R.id.head_image);
        about_us = (RelativeLayout) view.findViewById(R.id.about_us);
        updata = (RelativeLayout) view.findViewById(R.id.updata);

    }

    @Override
    protected void initValue() {
        connectivity = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        info = connectivity.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //WiFi网络
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //移动网络
            } else {
                //网络错误
            }
        } else {
            //网络错误
        }
        getdata();
    }

    @Override
    protected void initListener() {
        exit.setOnClickListener(this);
        set_data.setOnClickListener(this);
        about_us.setOnClickListener(this);
        updata.setOnClickListener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.collectorc_entergragment_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.exit:
                final ConstantDialog dialog1 = new ConstantDialog(getActivity(), R.style.Dialog);
                dialog1.setText("确认退出吗?");
                dialog1.setnegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.show();
                        Constants.token = "-1";
                        UserUtil.setLoginState(getActivity(), false);
                        Set<String> set = new HashSet<>();
//                        set.add("催收员");
                        JPushInterface.setAliasAndTags(getActivity(), "", set, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, final Set<String> set) {

                                Log.e("状态", String.valueOf(i));
                                Log.e("状态", String.valueOf(s));
                                Log.e("状态", String.valueOf(set));

//

                            }
                        });
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), LogInActivity.class));
                        getActivity().finish();
                    }
                });
                dialog1.setpositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;

            case R.id.set_data:

                startActivity(new Intent(getActivity(), BasicsActivity.class));
                break;

            case R.id.about_us:
                startActivity(new Intent(getActivity(), AboutUsActiviyt.class));
                break;

            case R.id.updata:

                UpdateManager.Update(getActivity(), true, customdialog);
                break;

        }

    }

    void getdata() {
        dialog.show();
        RequestParams params = new RequestParams(Api.Me.Me);
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
                Log.e("个人中心", result.toString());
                try {
                    JSONObject mjs = new JSONObject(result.toString());
                    int grade = 0;
                    Set<String> set = new HashSet<>();
                    set.add("催收员");
                    JPushInterface.setAliasAndTags(getActivity(), mjs.getString("telephone"), set, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.e("状态", String.valueOf(i));
                            Log.e("状态", String.valueOf(s));
                            Log.e("状态", String.valueOf(set));
                        }
                    });
                    phonenumber.setText(mjs.getString("telephone"));
                    if (!mjs.getString("business_name").equals("null")) {

                        companyname.setText(mjs.getString("business_name"));
                    }
                    if (!mjs.getString("fullname").equals("null")) {

                        collection_name.setText(mjs.getString("fullname"));
                    }
                    Load_image.Setimage(Api.pic + mjs.getString("thumbnail"), head_image);

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
        getdata();
    }
}
