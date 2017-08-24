package com.new_jew.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.bean.HandoverEvidenceBean;
import com.new_jew.customview.GrapeGridview;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangpei on 2016/11/30.
 */
public class AssociateProveActivity1 extends BaseActivity {
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;

    private TextView has_driver_license, has_driving_license, has_keys,
            has_insurence_policy, has_id_card, mileage, extra_goods, extra_message;
    private GrapeGridview associate_gridview_now;
    private SimpleAdapter adapter;
    private List<Map<String, String>> mapList;

    @Override
    protected void initLayout() {
        x.view().inject(this);
        title_text.setText("交接证明");
        has_driver_license = (TextView) this.findViewById(R.id.has_driver_license_now);

        has_driving_license = (TextView) this.findViewById(R.id.has_driving_license_now);
        has_keys = (TextView) this.findViewById(R.id.has_keys_now);

        has_insurence_policy = (TextView) this.findViewById(R.id.has_insurence_policy_now);

        has_id_card = (TextView) this.findViewById(R.id.has_id_card_now);

        mileage = (TextView) this.findViewById(R.id.mileage_now);

        extra_goods = (TextView) this.findViewById(R.id.extra_goods_now);

        extra_message = (TextView) this.findViewById(R.id.extra_message_now);

        associate_gridview_now = (GrapeGridview) this.findViewById(R.id.associate_gridview_now);
    }

    @Override
    protected void initValue() {
        mapList = new ArrayList<>();
        adapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.layout_complete_associate, new String[]{"yes"}, new int[]{R.id.yes});
        associate_gridview_now.setAdapter(adapter);
        get_handover_evidence();

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_associateprove1;
    }

    void get_handover_evidence() {
        dialog.show();
        RequestParams params = new RequestParams(Api.my_car_orders.my_car_orders + Constants.id + "/handover_evidence/");
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
                try {
                    JSONObject json = new JSONObject(result.toString());
                    Gson gson = new Gson();
                    gson = new GsonBuilder()
                            .setLenient()// json宽松
                            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                            .serializeNulls() //智能null
                            .setPrettyPrinting()// 调教格式
                            .disableHtmlEscaping() //默认是GSON把HTML 转义的
                            .create();
                    String str = json.toString();
                    HandoverEvidenceBean bean = gson.fromJson(str, HandoverEvidenceBean.class);
                    Log.e("bean", bean.toString());
                    if (bean.isHas_driver_license() == true) {
                        has_driver_license.setText("有");
                    } else {
                        has_driver_license.setText("有");
                    }
                    if (bean.isHas_driving_license() == true) {
                        has_driving_license.setText("有");
                    } else {

                        has_driving_license.setText("无");
                    }

                    if (bean.isHas_keys() == true) {

                        has_keys.setText("有");
                    } else {
                        has_keys.setText("无");
                    }
                    if (bean.isHas_insurence_policy() == true) {
                        has_insurence_policy.setText("有");
                    } else {
                        has_insurence_policy.setText("无");
                    }
                    if (bean.isHas_id_card() == true) {
                        has_id_card.setText("有");
                    } else {
                        has_id_card.setText("无");
                    }
                    mileage.setText(String.valueOf(bean.getMileage()) + "公里");
                    extra_goods.setText("\t\t\t" + bean.getExtra_goods());
                    extra_message.setText("\t\t\t" + bean.getExtra_message());

                    if (bean.isNormal_front_bumper() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "前保险扛");
                        mapList.add(map);
                    }

                    if (bean.isNormal_rear_bumper() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "后保险扛");
                        mapList.add(map);
                    }

                    if (bean.isNormal_front_windshield() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "前挡风玻璃");
                        mapList.add(map);
                    }

                    if (bean.isNormal_back_windshield() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "后挡风玻璃");
                        mapList.add(map);
                    }

                    if (bean.isNormal_right_front_door() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "右前门");
                        mapList.add(map);
                    }

                    if (bean.isNormal_right_back_door() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "右后门");
                        mapList.add(map);
                    }
                    if (bean.isNormal_left_front_door() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "左前门");
                        mapList.add(map);
                    }
                    if (bean.isNormal_left_back_door() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "左后门");
                        mapList.add(map);
                    }
                    if (bean.isNormal_right_front_fender() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "右前翼子板");
                        mapList.add(map);
                    }
                    if (bean.isNormal_right_back_fender() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "右后翼子板");
                        mapList.add(map);
                    }
                    if (bean.isNormal_left_front_fender() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "左前翼子板");
                        mapList.add(map);
                    }
                    if (bean.isNormal_left_back_fender() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "左后翼子板");
                        mapList.add(map);
                    }
                    if (bean.isNormal_the_hood() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "引擎盖");
                        mapList.add(map);
                    }
                    if (bean.isNormal_roof() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "车顶");
                        mapList.add(map);
                    }
                    if (bean.isNormal_tail_box() == true) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yes", "尾箱");
                        mapList.add(map);
                    }
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("11111", result.toString());

            }

            @Override
            public void onFailure() {
                dialog.dismiss();

            }
        });

    }
}
