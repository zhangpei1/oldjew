package com.new_jew.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.new_jew.bean.AttestationDataBean;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.toolkit.GetMebean;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/25.
 */
public class HttpBean {


    private GetMebean beanutil;

    public void setGetMebean(GetMebean beanutil) {
        this.beanutil = beanutil;


    }

    //个人中心
    public void get_attestation_data() {
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
                Log.e("result1111", result.toString());
                try {
                    JSONObject mjs = new JSONObject(result.toString());
//                    JSONObject profile_json = new JSONObject(mjs.getString("profile"));
                    Gson gson = new Gson();
                    gson = new GsonBuilder()
                            .setLenient()// json宽松
                            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                            .serializeNulls() //智能null
                            .setPrettyPrinting()// 调教格式
                            .disableHtmlEscaping() //默认是GSON把HTML 转义的
                            .create();
                    String str = mjs.toString();
                    AttestationDataBean bean = gson.fromJson(str, AttestationDataBean.class);
                    beanutil.getresult(bean);
                    Log.e(">>>>>>", String.valueOf(bean.toString()));
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
