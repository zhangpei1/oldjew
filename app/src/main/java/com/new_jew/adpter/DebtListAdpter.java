package com.new_jew.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.bean.DebListBean;
import com.new_jew.toolkit.TimeUtil;

import java.text.ParseException;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/18.
 */
public class DebtListAdpter extends BaseAdapter {
    private List<DebListBean> mlist;
    private Context context;
    private LayoutInflater inflater;

    public DebtListAdpter(List<DebListBean> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHold viewHold = null;
        if (view == null) {
            viewHold = new ViewHold();
            view = inflater.inflate(R.layout.layout_debt_item, null);
            viewHold.get_level = (TextView) view.findViewById(R.id.get_level);
            viewHold.loan_type = (TextView) view.findViewById(R.id.loan_type);
            viewHold.demands_of_collection = (TextView) view.findViewById(R.id.demands_of_collection);
            viewHold.vehicle_has_gps = (TextView) view.findViewById(R.id.vehicle_has_gps);
            viewHold.vehicle_has_illegal = (TextView) view.findViewById(R.id.vehicle_has_illegal);
            viewHold.collection_commission = (TextView) view.findViewById(R.id.collection_commission);
            viewHold.loan_start_overdue_time = (TextView) view.findViewById(R.id.loan_start_overdue_time);
            viewHold.collecting_days = (TextView) view.findViewById(R.id.collecting_days);
            viewHold.vehicle_possible_address = (TextView) view.findViewById(R.id.vehicle_possible_address);
            viewHold.ensure = (Button) view.findViewById(R.id.ensure);
            view.setTag(viewHold);


        } else {
            viewHold = (ViewHold) view.getTag();

        }
        viewHold.get_level.setText(mlist.get(position).getGet_level() + "级");
        viewHold.loan_type.setText("[" + mlist.get(position).getLoan_type() + "]");
        viewHold.demands_of_collection.setText(mlist.get(position).getDemands_of_collection() + "业务");
        if (mlist.get(position).isVehicle_has_gps() == true) {
            viewHold.vehicle_has_gps.setText("有");
        } else {
            viewHold.vehicle_has_gps.setText("无");

        }
        if (mlist.get(position).isVehicle_has_illegal() == true) {
            viewHold.vehicle_has_illegal.setText("有");
        } else {

            viewHold.vehicle_has_illegal.setText("无");
        }
        viewHold.collection_commission.setText(mlist.get(position).getCollection_commission());
        String time=mlist.get(position).getLoan_start_overdue_time();
        try {
            long timestamp= TimeUtil.gettimestamp(time);
            long now_time=System.currentTimeMillis();
           long time_difference=now_time-timestamp;
            Log.e("...",String.valueOf(time_difference));
            Log.e("开始预期时间",String.valueOf(timestamp));
            Log.e("现在时间",String.valueOf(now_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHold.collecting_days.setText(mlist.get(position).getCollecting_days());
        viewHold.vehicle_possible_address.setText(mlist.get(position).getVehicle_possible_address());
        return view;
    }

    class ViewHold {
        private TextView get_level, loan_type, demands_of_collection, vehicle_has_gps, vehicle_has_illegal,
                collection_commission, loan_start_overdue_time, collecting_days, vehicle_possible_address;
        private Button ensure;


    }
}
