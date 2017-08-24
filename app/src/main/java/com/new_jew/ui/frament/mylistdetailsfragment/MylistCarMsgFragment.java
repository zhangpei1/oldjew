package com.new_jew.ui.frament.mylistdetailsfragment;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.bean.DebtDetailsBean;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/23.
 */
public class MylistCarMsgFragment extends BaseFrament {
    private TextView vehicle_brand_name, vehicle_ownership_name, vehicle_plate_year,
            vehicle_plate_number, vehicle_color, vehicle_engine_number, vehicle_frame_number, vehicle_status,
            vehicle_has_gps, normal_key, vehicle_has_illegal, vehicle_illegal_day, vehicle_possible_address, vehicle_illegal_details, vehicle_style_name,
            vehicle_type_name;

    @Override
    protected void initLayout() {
        vehicle_brand_name = (TextView) view.findViewById(R.id.vehicle_brand_name);
        vehicle_ownership_name = (TextView) view.findViewById(R.id.vehicle_ownership_name);
        vehicle_plate_year = (TextView) view.findViewById(R.id.vehicle_plate_year);
        vehicle_plate_number = (TextView) view.findViewById(R.id.vehicle_plate_number);
        vehicle_color = (TextView) view.findViewById(R.id.vehicle_color);
        vehicle_engine_number = (TextView) view.findViewById(R.id.vehicle_engine_number);
        vehicle_frame_number = (TextView) view.findViewById(R.id.vehicle_frame_number);
        vehicle_status = (TextView) view.findViewById(R.id.vehicle_status);
        vehicle_has_gps = (TextView) view.findViewById(R.id.vehicle_has_gps);
        normal_key = (TextView) view.findViewById(R.id.normal_key);
        vehicle_has_illegal = (TextView) view.findViewById(R.id.vehicle_has_illegal);
        vehicle_illegal_day = (TextView) view.findViewById(R.id.vehicle_illegal_day);
        vehicle_possible_address = (TextView) view.findViewById(R.id.vehicle_possible_address);
        vehicle_illegal_details = (TextView) view.findViewById(R.id.vehicle_illegal_details);
        vehicle_style_name = (TextView) view.findViewById(R.id.vehicle_style_name);
        vehicle_type_name = (TextView) view.findViewById(R.id.vehicle_type_name);
    }

    @Override
    protected void initValue() {
        getdebtdetail(Constants.id);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_mylistcarmsg;
    }

    void getdebtdetail(String id) {
        String url = null;
        if (Constants.type.equals("3")) {

            url = Api.my_car_orders.my_car_orders;
        } else if (Constants.type.equals("4")) {
            url = Api.staff_car_orders.staff_car_orders;
        } else {
            Toast.makeText(getActivity(), "没有相对应的url", Toast.LENGTH_SHORT).show();
        }
        try {
            JSONObject jsonObject = new JSONObject(UserUtil.getUserToken(getActivity(), "details").toString());
            String str = jsonObject.toString();
            ;
            Gson gson = new Gson();
            DebtDetailsBean detailsBean = gson.fromJson(str, DebtDetailsBean.class);
            vehicle_brand_name.setText(detailsBean.getVehicle_brand_name());
            vehicle_ownership_name.setText(detailsBean.getVehicle_ownership_name());
            vehicle_plate_year.setText(detailsBean.getVehicle_plate_year());
            vehicle_plate_number.setText(detailsBean.getVehicle_plate_number());
            vehicle_color.setText(detailsBean.getVehicle_color());
            vehicle_engine_number.setText(detailsBean.getVehicle_engine_number());
            vehicle_frame_number.setText(detailsBean.getVehicle_frame_number());
            vehicle_status.setText(detailsBean.getVehicle_status());
            vehicle_style_name.setText(","+detailsBean.getVehicle_style_name());
            vehicle_type_name.setText(","+detailsBean.getVehicle_type_name());
            if (detailsBean.getVehicle_has_gps().equals("true")) {

                vehicle_has_gps.setText("有");
            } else {
                vehicle_has_gps.setText("无");
            }
            if (detailsBean.getVehicle_has_key().equals("true")) {

                normal_key.setText("有");
            } else {
                normal_key.setText("无");
            }
            if (detailsBean.getVehicle_has_illegal().equals("true")) {

                vehicle_has_illegal.setText("有");
            } else {
                vehicle_has_illegal.setText("无");
            }
            vehicle_illegal_day.setText(detailsBean.getVehicle_illegal_day());
            vehicle_possible_address.setText(detailsBean.getVehicle_possible_address());
            vehicle_illegal_details.setText(detailsBean.getVehicle_illegal_details());
            Log.e("detailsBean", detailsBean.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestParams params = new RequestParams(url + id + "/");
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                try {
                    Log.e("result", result.toString());
                    JSONObject json = new JSONObject(result.toString());

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
