package com.new_jew.ui.frament;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.bean.AttestationDataBean;
import com.new_jew.customview.ConstantDialog;
import com.new_jew.customview.CustomDialog;
import com.new_jew.customview.RoundImageView;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpBean;
import com.new_jew.net.HttpUtils;
import com.new_jew.net.Load_image;
import com.new_jew.toolkit.GetMebean;
import com.new_jew.toolkit.UpdateManager;
import com.new_jew.toolkit.UserUtil;
import com.new_jew.ui.activity.AboutUsActiviyt;
import com.new_jew.ui.activity.BasicsActivity;
import com.new_jew.ui.activity.DataAttestationActivity;
import com.new_jew.ui.activity.LogInActivity;
import com.new_jew.ui.activity.ManageActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zhangpei on 2016/11/9.
 */
public class CenterFrament extends BaseFrament implements View.OnClickListener, GetMebean {
    private RoundImageView head_image;
    private ImageView image;
    private TextView set_data, phonenumber, collection_name, companyname, rank_text, authentication_test, orders_number;
    private RelativeLayout data_attestation, exit, person_manage, about_us, updata;
    private HttpBean httpBean;
    private CustomDialog customdialog;
    private NetworkInfo info;
    private ConnectivityManager connectivity;

    @Override
    protected void initLayout() {
        customdialog = new CustomDialog(getActivity());
        data_attestation = (RelativeLayout) view.findViewById(R.id.data_attestation);
        phonenumber = (TextView) view.findViewById(R.id.phonenumber);
        collection_name = (TextView) view.findViewById(R.id.collection_name);
        companyname = (TextView) view.findViewById(R.id.companyname);
        rank_text = (TextView) view.findViewById(R.id.rank_text);
        exit = (RelativeLayout) view.findViewById(R.id.exit);
        set_data = (TextView) view.findViewById(R.id.set_data);
        person_manage = (RelativeLayout) view.findViewById(R.id.person_manage);
        head_image = (RoundImageView) view.findViewById(R.id.head_image);
        authentication_test = (TextView) view.findViewById(R.id.authentication_test);
        orders_number = (TextView) view.findViewById(R.id.orders_number);
        about_us = (RelativeLayout) view.findViewById(R.id.about_us);
        image = (ImageView) view.findViewById(R.id.image);
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
//        httpBean=new HttpBean();
//        httpBean.get_attestation_data();
//        httpBean.setGetMebean(this);


    }

    @Override
    protected void initListener() {
        data_attestation.setOnClickListener(this);
        exit.setOnClickListener(this);
        set_data.setOnClickListener(this);
        person_manage.setOnClickListener(this);
        about_us.setOnClickListener(this);
        updata.setOnClickListener(this);

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_centerframent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_attestation:
                startActivity(new Intent(getActivity(), DataAttestationActivity.class));
                break;
            case R.id.exit:

                final ConstantDialog dialog1 = new ConstantDialog(getActivity(), R.style.Dialog);
                dialog1.setText("确认退出吗？");
                dialog1.setnegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        dialog.show();
                        Constants.token = "-1";
                        UserUtil.setLoginState(getActivity(), false);
                        Set<String> set = new HashSet<>();
                        JPushInterface.setAliasAndTags(getActivity(), "", set, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, final Set<String> set) {
                                int a = 0;

                                Log.e("非状态", String.valueOf(i));
                                Log.e("非状态", String.valueOf(s));
                                Log.e("非状态", String.valueOf(set));

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

            case R.id.person_manage:
                startActivity(new Intent(getActivity(), ManageActivity.class));
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
                Log.e("个人中心", result.toString());
                try {
                    final JSONObject mjs = new JSONObject(result.toString());
                    if (mjs.getBoolean("is_auth") == true) {
                        authentication_test.setText("已认证");
                        image.setVisibility(View.VISIBLE);
                    } else {
                        authentication_test.setText("未认证");
                        image.setVisibility(View.GONE);
                    }
                    int grade = 0;
                    Set<String> set = new HashSet<>();
                    set.add("催收公司");
                    JPushInterface.setAliasAndTags(getActivity(), mjs.getString("telephone"), set, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, final Set<String> set) {
                            Log.e("状态", String.valueOf(i));
                            Log.e("状态", String.valueOf(s));
                            Log.e("状态", String.valueOf(set));
                        }
                    });
                    rank_text.setText(mjs.getString("level"));
                    orders_number.setText(String.valueOf(mjs.getInt("collecting_order_count") + "/" + String.valueOf(mjs.getInt("max_order_count"))));

                    phonenumber.setText(mjs.getString("telephone"));
//                    JSONObject profile_json=new JSONObject(mjs.getString("profile"));
                    if (!mjs.getString("business_name").equals("null")) {

                        companyname.setText(mjs.getString("business_name"));
                    }
                    if (!mjs.getString("contact_person_name").equals("null")) {

                        collection_name.setText(mjs.getString("contact_person_name"));
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
    public void getresult(AttestationDataBean bean) {


    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }
}
