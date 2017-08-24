package com.new_jew.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.MainActivity;
import com.new_jew.R;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.toolkit.UserUtil;
import com.new_jew.ui.collectoractivity.CollectorMainActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by zhangpei on 2016/11/7.
 */
public class LogInActivity extends BaseActivity implements View.OnClickListener {
    private Button sign_in, button_register;
    private EditText account_number;
    private EditText login_password;
    private TextView password_text;

    @Override
    protected void initLayout() {
        //设置全屏显示
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sign_in = (Button) this.findViewById(R.id.sign_in);
        button_register = (Button) this.findViewById(R.id.button_register);
        account_number = (EditText) this.findViewById(R.id.account_number);
        login_password = (EditText) this.findViewById(R.id.login_password);
        password_text = (TextView) this.findViewById(R.id.password_text);


    }

    @Override
    protected void initValue() {

    }

    @Override
    protected void initListener() {
        sign_in.setOnClickListener(this);
        button_register.setOnClickListener(this);
        password_text.setOnClickListener(this);

    }

    @Override
    protected int setRootView() {
        return R.layout.login_activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_in:
                if (account_number.getText().length() != 11) {
                    Toast.makeText(getApplicationContext(), "请输入11位手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (login_password.getText().length() == 0) {

                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                overridePendingTransition(R.anim.fade, R.anim.hold);
                //overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
                LogIn();

                break;
            case R.id.button_register:
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));

                break;

            case R.id.password_text:
                Intent intent = new Intent(LogInActivity.this, Retrieve_password.class);
                intent.putExtra("id", 0);
                startActivity(intent);
                break;
        }

    }

    private void LogIn() {
        dialog.show();
        RequestParams params = new RequestParams(Api.LogIn.LogIn);
        params.addBodyParameter("telephone", account_number.getText().toString());
        params.addBodyParameter("password", login_password.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {

                try {
                    JSONObject mjs = new JSONObject(s.toString());
                    int type = 0;
                    type = mjs.getInt("user_type");
                    UserUtil.setUserToken(getApplicationContext(), mjs.getString("token").toString(), "Token");
                    Constants.token = UserUtil.getUserToken(getApplicationContext(), "Token");
                    Log.e(" Constants.token", Constants.token);
                    Log.e("type", String.valueOf(type));
                    if (type == 3) {
                        UserUtil.setUserToken(getApplicationContext(), "3", "type");
                        UserUtil.setLoginState(getApplicationContext(), true);
                        Constants.type = UserUtil.getUserToken(getApplicationContext(), "type");
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "登录成功即将跳转主页!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                        finish();
                    } else if (type == 4) {
                        UserUtil.setUserToken(getApplicationContext(), "4", "type");
                        UserUtil.setLoginState(getApplicationContext(), true);
                        Constants.type = UserUtil.getUserToken(getApplicationContext(), "type");
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "登录成功即将跳转主页!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, CollectorMainActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                Log.e("s", s.toString());
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                dialog.dismiss();
                Log.e("throwable", String.valueOf(throwable));
                if (throwable instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) throwable;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
//                    Toast.makeText(x.app(), responseMsg, Toast.LENGTH_SHORT).show();
                    if (responseCode == 500 || responseCode == 501 || responseCode == 502 || responseCode == 503) {

                        Toast.makeText(x.app(), "服务器异常！", Toast.LENGTH_SHORT).show();
                    }
                    if (responseCode == 403 || responseCode == 401) {

                        Toast.makeText(x.app(), "登录状态异常请重新登录", Toast.LENGTH_SHORT).show();
                    }
                    if (responseCode == 400) {

                        Toast.makeText(x.app(), "用户名或密码错误", Toast.LENGTH_SHORT).show();

                    }

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
}
