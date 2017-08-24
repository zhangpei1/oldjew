package com.new_jew.ui.frament.mylistdetailsfragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.toolkit.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/23.
 */
public class MylistDebtMsgFragment extends BaseFrament {
    private TextView loan_type, demands_of_collection, get_level,
            collecting_days, collection_commission, loan_amount,
            loan_compensatory_amount, loan_overdue_amount,
            loan_default_amount, loan_term, loan_start_overdue_time, historical_collection_record, collecting_days1;
    private LinearLayout collection_commission_linear;

    @Override
    protected void initLayout() {
        loan_type = (TextView) view.findViewById(R.id.loan_type);
        demands_of_collection = (TextView) view.findViewById(R.id.demands_of_collection);
        get_level = (TextView) view.findViewById(R.id.get_level);
        collecting_days = (TextView) view.findViewById(R.id.collecting_days);
        collection_commission = (TextView) view.findViewById(R.id.collection_commission);
        loan_amount = (TextView) view.findViewById(R.id.loan_amount);
        loan_compensatory_amount = (TextView) view.findViewById(R.id.loan_compensatory_amount);
        loan_overdue_amount = (TextView) view.findViewById(R.id.loan_overdue_amount);
        loan_default_amount = (TextView) view.findViewById(R.id.loan_default_amount);
        loan_term = (TextView) view.findViewById(R.id.loan_term);
        loan_start_overdue_time = (TextView) view.findViewById(R.id.loan_start_overdue_time);
        historical_collection_record = (TextView) view.findViewById(R.id.historical_collection_record);
        collecting_days1 = (TextView) view.findViewById(R.id.collecting_days1);
        collection_commission_linear = (LinearLayout) view.findViewById(R.id.collection_commission_linear);
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
        return R.layout.layout_mylistdebtmsg;
    }

    void getdebtdetail1(String id) {
        String url = null;

        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(UserUtil.getUserToken(getActivity(),"details").toString());
            String str = jsonObject.toString();
            Gson gson = new Gson();
            DebtDetailsBean detailsBean = gson.fromJson(str, DebtDetailsBean.class);
            loan_type.setText(detailsBean.getLoan_type());
            demands_of_collection.setText(detailsBean.getDemands_of_collection());
            get_level.setText(detailsBean.getGet_level() + "级");
            collecting_days.setText(detailsBean.getCollecting_days() + "天");

            String d = detailsBean.getCollecting_days();
            int i = Integer.parseInt(d) - TimeUtil.getformatdata(detailsBean.getCollecting_day().toString());
            collecting_days1.setText("(剩余" + i + "天)");
            if (Constants.type.equals("3")) {
                collection_commission.setText(detailsBean.getCollection_commission() + "元");
            }
            loan_amount.setText(detailsBean.getLoan_amount() + "元");
            loan_compensatory_amount.setText(detailsBean.getLoan_compensatory_amount() + "元");
            loan_overdue_amount.setText(detailsBean.getLoan_overdue_amount() + "元");
            loan_default_amount.setText(detailsBean.getLoan_default_amount() + "元");
            loan_term.setText(detailsBean.getLoan_term() + "期");
            long timestamp = 0;
            try {
                timestamp = TimeUtil.gettimestamp(detailsBean.getLoan_start_overdue_time().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long now_time = System.currentTimeMillis();
            double time_difference = (double) ((now_time - timestamp) / (1000 * 60 * 60 * 24));
            double periods = time_difference / 30;
            Log.e("....", String.valueOf(periods));
            int m = (int) Math.ceil(periods);
            loan_start_overdue_time.setText("M" + String.valueOf(m));
            historical_collection_record.setText(detailsBean.getHistorical_collection_record());
            Log.e("detailsBean", detailsBean.toString());
            dialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    void getdebtdetail(String id) {
        dialog.show();
        String url = null;

        if (Constants.type.equals("3")) {

            url = Api.my_car_orders.my_car_orders;
            collection_commission_linear.setVisibility(View.VISIBLE);
        } else if (Constants.type.equals("4")) {
            url = Api.staff_car_orders.staff_car_orders;
            collection_commission_linear.setVisibility(View.GONE);
        } else {
            Toast.makeText(getActivity(), "没有相对应的url", Toast.LENGTH_SHORT).show();
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
                      dialog.dismiss();
                    Log.e("result", result.toString());
                    JSONObject json = new JSONObject(result.toString());
                    UserUtil.setUserToken(getActivity(), json.toString(), "details");
                    getdebtdetail1("");

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
}
