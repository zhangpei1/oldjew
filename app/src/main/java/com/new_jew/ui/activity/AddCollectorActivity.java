package com.new_jew.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.global.NoteManager;
import com.new_jew.net.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.smssdk.EventHandler;

/**
 * Created by zhangpei on 2016/11/25.
 */
public class AddCollectorActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.colltector)
    private EditText colltector;
    @ViewInject(R.id.colltector_phone)
    private EditText colltector_phone;
    @ViewInject(R.id.edit_code)
    private EditText edit_code;
    @ViewInject(R.id.obtain_text)
    private TextView obtain_text;
    @ViewInject(R.id.sure_modify)
    private Button sure_modify;
    private int s = 60;
    private NoteManager noteManager;
    private EventHandler eh;//短信接口回调接口
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    obtain_text.setText(String.valueOf(s) + "s");
                    break;
                case 2:
                    s = 60;
                    obtain_text.setClickable(true);
                    obtain_text.setText("获取验证码");
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "短信服务器异常！", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void initLayout() {
        x.view().inject(this);
        noteManager = new NoteManager(eh, mhandler);
        title_text.setText("增加催收员");
    }

    @Override
    protected void initValue() {

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        obtain_text.setOnClickListener(this);
        sure_modify.setOnClickListener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_addcollector;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();

                break;
            case R.id.obtain_text:
                if (colltector_phone.getText().length()!=11){

                    Toast.makeText(getApplicationContext(),"请输入正确的手机号码!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Verify();
                break;
            case R.id.sure_modify:
                if (colltector.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"催收员姓名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (colltector_phone.getText().length()!=11){
                    Toast.makeText(getApplicationContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    return;

                }
                if (edit_code.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                    return;

                }

                post_collection_company_staffs();
                break;


        }

    }

    void post_collection_company_staffs() {
        dialog.show();
        RequestParams params = new RequestParams(Api.collection_company_staffs.collection_company_staffs);
        params.addBodyParameter("telephone", colltector_phone.getText().toString());
        params.addBodyParameter("fullname", colltector.getText().toString());
        params.addBodyParameter("password", "123456");
        params.addBodyParameter("code", edit_code.getText().toString());
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
                Toast.makeText(getApplicationContext(), "创建成功！", Toast.LENGTH_SHORT).show();
                Log.e("result", result.toString());

            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "创建失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*****
     * 获取验证码！
     */
    private void sendcode() {
        obtain_text.setClickable(false);
        obtain_text.setText(String.valueOf(s) + "s ");
        noteManager.getCode(colltector_phone.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        s--;
                        Message message = new Message();
                        message.what = 1;
                        mhandler.sendMessage(message);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (s == 0) {

                        Message message = new Message();
                        message.what = 2;
                        mhandler.sendMessage(message);
                        return;
                    }

                }
            }
        }).start();


    }

    private void Verify() {

        RequestParams params = new RequestParams(Api.Verify.Verify);
        params.addBodyParameter("telephone", colltector_phone.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                sendcode();

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.e("throwable", String.valueOf(throwable));
                if (throwable instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) throwable;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    Log.e("msg", errorResult);
                    try {
                        JSONObject error = new JSONObject(errorResult.toString());
                        Toast.makeText(x.app(), error.getString("msg"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return;

                }

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        noteManager.stopSSDK();
    }
}
