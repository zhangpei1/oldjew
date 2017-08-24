package com.new_jew.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.CarSituationAdpter;
import com.new_jew.adpter.ScreenAdpter;
import com.new_jew.bean.HandoverEvidenceBean;
import com.new_jew.bean.ScreenBean;
import com.new_jew.customview.ConstantDialog;
import com.new_jew.customview.GrapeGridview;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/30.
 */
public class AssociateProveActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.submit)
    private Button submit;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.associate_gridview)
    private GrapeGridview associate_gridview;
    private CarSituationAdpter screenAdpter;
    private ArrayList<ScreenBean> mlist;
    private HandoverEvidenceBean mbean;
    private RadioButton has_driver_license, has_driver_license1;
    private RadioButton has_driving_license, has_driving_license1;
    private RadioButton has_keys, has_keys1;
    private RadioButton has_insurence_policy, has_insurence_policy1;
    private RadioButton has_id_card, has_id_card1;

    private EditText mileage;//公里数

    private EditText extra_goods;//其他物品

    private EditText extra_message;//其他事项

    private ConstantDialog mdialog;


    @Override
    protected void initLayout() {
        x.view().inject(this);
        mlist = new ArrayList<>();
        mbean = new HandoverEvidenceBean();
//        list=new ArrayList<>();
        //车辆相关物件

        has_driver_license = (RadioButton) this.findViewById(R.id.has_driver_license);
        has_driver_license1 = (RadioButton) this.findViewById(R.id.has_driver_license1);

        has_driving_license = (RadioButton) this.findViewById(R.id.has_driving_license);
        has_driving_license1 = (RadioButton) this.findViewById(R.id.has_driving_license1);

        has_keys = (RadioButton) this.findViewById(R.id.has_keys);
        has_keys1 = (RadioButton) this.findViewById(R.id.has_keys1);

        has_insurence_policy = (RadioButton) this.findViewById(R.id.has_insurence_policy);
        has_insurence_policy1 = (RadioButton) this.findViewById(R.id.has_insurence_policy1);

        has_id_card = (RadioButton) this.findViewById(R.id.has_id_card);
        has_id_card1 = (RadioButton) this.findViewById(R.id.has_id_card1);

        mileage = (EditText) this.findViewById(R.id.mileage);
        extra_goods = (EditText) this.findViewById(R.id.extra_goods);
        extra_message = (EditText) this.findViewById(R.id.extra_message);
    }

    @Override
    protected void initValue() {
        title_text.setText("交接证明");
        mlist.add(new ScreenBean("前保险扛", false));
        mlist.add(new ScreenBean("后保险扛", false));
        mlist.add(new ScreenBean("前挡风玻璃", false));
        mlist.add(new ScreenBean("后档风玻璃", false));
        mlist.add(new ScreenBean("右前门", false));
        mlist.add(new ScreenBean("右后门", false));
        mlist.add(new ScreenBean("左前门", false));
        mlist.add(new ScreenBean("左后门", false));
        mlist.add(new ScreenBean("左前翼子板", false));
        mlist.add(new ScreenBean("左后翼子板", false));
        mlist.add(new ScreenBean("右前翼子板", false));
        mlist.add(new ScreenBean("右后翼子板", false));
        mlist.add(new ScreenBean("引擎盖", false));
        mlist.add(new ScreenBean("车顶", false));
        mlist.add(new ScreenBean("尾箱", false));

        screenAdpter = new CarSituationAdpter(mlist, this);
        associate_gridview.setAdapter(screenAdpter);


    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        associate_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                screenAdpter.setSeclection(position);
                screenAdpter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_associateprove;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();

                break;
            case R.id.submit:
                mdialog = new ConstantDialog(this, R.style.Dialog);
                mdialog.setText("请您确认信息是否有误,提交后\n" +
                        "\n系统将自动为您申请结算");
                mdialog.setpositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdialog.dismiss();
                    }
                });
                mdialog.setnegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_handover_evidence();
                    }
                });
                mdialog.show();
                break;


        }
    }

    void post_handover_evidence() {
        mdialog.dismiss();
        dialog.show();
        for (int i = 0; i < mlist.size(); i++) {
            if (i == 0) {
                mbean.setNormal_front_bumper(mlist.get(i).ishave());
            }
            if (i == 1) {
                mbean.setNormal_rear_bumper(mlist.get(i).ishave());
            }
            if (i == 2) {

                mbean.setNormal_front_windshield(mlist.get(i).ishave());
            }
            if (i == 3) {

                mbean.setNormal_back_windshield(mlist.get(i).ishave());
            }
            if (i == 4) {

                mbean.setNormal_right_front_door(mlist.get(i).ishave());
            }
            if (i == 5) {

                mbean.setNormal_right_back_door(mlist.get(i).ishave());
            }
            if (i == 6) {

                mbean.setNormal_left_front_door(mlist.get(i).ishave());
            }
            if (i == 7) {

                mbean.setNormal_left_back_door(mlist.get(i).ishave());
            }
            if (i == 8) {

                mbean.setNormal_left_front_fender(mlist.get(i).ishave());
            }
            if (i == 9) {

                mbean.setNormal_left_back_fender(mlist.get(i).ishave());
            }
            if (i == 10) {

                mbean.setNormal_right_front_fender(mlist.get(i).ishave());
            }
            if (i == 11) {

                mbean.setNormal_right_back_fender(mlist.get(i).ishave());
            }
            if (i == 12) {

                mbean.setNormal_the_hood(mlist.get(i).ishave());
            }
            if (i == 13) {

                mbean.setNormal_roof(mlist.get(i).ishave());
            }
            if (i == 14) {

                mbean.setNormal_tail_box(mlist.get(i).ishave());
            }

        }
        if (has_driver_license.isChecked() == true) {
            mbean.setHas_driver_license(false);
        } else {
            mbean.setHas_driver_license(true);
        }
        if (has_driving_license.isChecked() == true) {

            mbean.setHas_driving_license(false);
        } else {

            mbean.setHas_driving_license(true);
        }
        if (has_keys.isChecked() == true) {

            mbean.setHas_keys(false);
        } else {
            mbean.setHas_keys(true);
        }
        if (has_insurence_policy.isChecked() == true) {

            mbean.setHas_insurence_policy(false);
        } else {
            mbean.setHas_insurence_policy(true);
        }
        if (has_id_card.isChecked() == true) {

            mbean.setHas_id_card(false);
        } else {
            mbean.setHas_id_card(true);
        }
        if (mileage != null) {
            mbean.setMileage(Integer.parseInt(mileage.getText().toString()));
        }
        if (!extra_goods.equals("")) {
            mbean.setExtra_goods(extra_goods.getText().toString());
        }
        if (!extra_message.equals("")) {
            mbean.setExtra_message(extra_message.getText().toString());

        }

        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + Constants.id + "/handover_evidence/");

        Gson gson = new Gson();
        String json = gson.toJson(mbean);
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        params.setConnectTimeout(5000);
        Log.e("bean", json);
        HttpUtils.Posthttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                Log.e("result", result.toString());
                apply_settlement();


            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"提交交接证明失败！",Toast.LENGTH_SHORT).show();

            }
        });

    }

    void apply_settlement() {

        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + Constants.id + "/apply_settlement/");
        HttpUtils.Posthttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                Toast.makeText(getApplicationContext(),"申请已提交！",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), AssociateProveActivity1.class));
                Log.e("申请结算", result.toString());
                finish();
            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"申请结算失败！",Toast.LENGTH_SHORT).show();
            }
        });

    }
}