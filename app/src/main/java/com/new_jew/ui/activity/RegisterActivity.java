package com.new_jew.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.MainActivity;
import com.new_jew.R;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.NoteManager;
import com.new_jew.toolkit.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.smssdk.EventHandler;

/**
 * Created by zhangpei on 2016/11/8.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.text_edittext)//手机号码
            EditText text_edittext;
    @ViewInject(R.id.obtain_text)
    TextView obtain_text;//获取验证码按钮
    @ViewInject(R.id.input_code)
    EditText input_code;//输入验证码
    @ViewInject(R.id.edit_password)
    EditText edit_password;//输入密码
    @ViewInject(R.id.button_register1)
    Button button_register1;//注册按钮
    @ViewInject(R.id.checkbox1)
    CheckBox checkbox1;
    @ViewInject(R.id.agreement)
    private TextView agreement;
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
        if (checkbox1.isChecked() == false) {


        }


    }

    @Override
    protected void initValue() {

    }

    @Override
    protected void initListener() {
        obtain_text.setOnClickListener(this);
        button_register1.setOnClickListener(this);
        agreement.setOnClickListener(this);

    }

    @Override
    protected int setRootView() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.obtain_text:

                if (text_edittext.getText().length() != 11) {

                    Toast.makeText(getApplicationContext(), "请输入11位手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Verify();


                break;

            case R.id.button_register1:

                if (checkbox1.isChecked() == false) {

                    Toast.makeText(getApplicationContext(), "请阅读锦衣卫相关协议！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Register();

                break;

            case R.id.agreement:
                startActivity(new Intent(this, AgreementActivity.class));
                break;
        }


    }

    /***
     * 注册
     */
    private void Register() {
        RequestParams params = new RequestParams(Api.Register.Register);

        params.addBodyParameter("telephone", text_edittext.getText().toString());

        params.addBodyParameter("password", edit_password.getText().toString());

        params.addBodyParameter("password2", edit_password.getText().toString());

        params.addBodyParameter("code", input_code.getText().toString());

        params.addParameter("user_type", 3);
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("s", s.toString());
                        try {
                            JSONObject mjs = new JSONObject(s.toString());
//                            JSONObject mjs1 = new JSONObject(mjs.getString("user").toString());
                            Constants.token = mjs.getString("token");
                            UserUtil.setUserToken(getApplicationContext(), "3", "type");
                            UserUtil.setLoginState(getApplicationContext(), true);
                            Constants.type = UserUtil.getUserToken(getApplicationContext(), "type");
                            UserUtil.setUserToken(getApplicationContext(), mjs.getString("token"), "Token");
                            Toast.makeText(getApplicationContext(), "注册成功！即将跳转主页！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        Log.e("throwable", String.valueOf(throwable.getMessage()));
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


                        }
                    }

                    @Override
                    public void onCancelled(CancelledException e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                }

        );


    }

    /***
     * 手机号码是否注册过
     */
    private void Verify() {

        RequestParams params = new RequestParams(Api.Verify.Verify);
        params.addBodyParameter("telephone", text_edittext.getText().toString());
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
                    if (responseCode == 400) {

                        Toast.makeText(getApplicationContext(), "手机号码已注册！", Toast.LENGTH_SHORT).show();
                    }
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
    protected void onDestroy() {
        super.onDestroy();
        noteManager.stopSSDK();
    }

    /*****
     * 获取验证码！
     */
    private void sendcode() {
        obtain_text.setClickable(false);
        obtain_text.setText(String.valueOf(s) + "s ");
        noteManager.getCode(text_edittext.getText().toString());
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
}
