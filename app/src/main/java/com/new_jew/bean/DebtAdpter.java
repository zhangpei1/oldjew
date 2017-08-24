package com.new_jew.bean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.toolkit.ButtonClicklistener;
import com.new_jew.toolkit.MyItemClickListener;
import com.new_jew.toolkit.MyItemLongClickListener;
import com.new_jew.toolkit.TimeUtil;

import java.text.ParseException;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/15.
 */
public class DebtAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DebListBean> mlist;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private ButtonClicklistener buttonClicklistener;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public int isshow = 3;//0为隐藏1为显示
    private String loading = "";


    public DebtAdpter(Context context, List<DebListBean> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    public void setishow(int isshow) {

        this.isshow = isshow;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setloading(String loading) {
        this.loading = loading;

    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public void setButtonClicklistener(ButtonClicklistener buttonClicklistener) {
        this.buttonClicklistener = buttonClicklistener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_debt_item,
                    parent, false), mItemClickListener, mItemLongClickListener);
            return holder;
        } else {
            FooterViewHolder fholder = new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_foot,
                    parent, false));
            return fholder;

        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHold, int position) {
        if (viewHold instanceof MyViewHolder) {
            ((MyViewHolder) viewHold).get_level.setText(mlist.get(position).getGet_level());
            ((MyViewHolder) viewHold).get_level.setText(mlist.get(position).getGet_level() + "级");
            ((MyViewHolder) viewHold).loan_type.setText("[" + mlist.get(position).getLoan_type() + "]");
            ((MyViewHolder) viewHold).demands_of_collection.setText(mlist.get(position).getDemands_of_collection() + "业务");
            if (mlist.get(position).isVehicle_has_gps() == true) {
                ((MyViewHolder) viewHold).vehicle_has_gps.setText("有");
            } else {
                ((MyViewHolder) viewHold).vehicle_has_gps.setText("无");

            }
            if (mlist.get(position).isVehicle_has_illegal() == true) {
                ((MyViewHolder) viewHold).vehicle_has_illegal.setText("有");
            } else {

                ((MyViewHolder) viewHold).vehicle_has_illegal.setText("无");
            }
            String commission = mlist.get(position).getCollection_commission();
            double commission_number = Double.parseDouble(commission);
            ((MyViewHolder) viewHold).collection_commission.setText(String.valueOf((double) Math.round(((commission_number) / 10000) * 100) / 100));

            String time = mlist.get(position).getLoan_start_overdue_time();
            try {
                long timestamp = TimeUtil.gettimestamp(time);
                long now_time = System.currentTimeMillis();
                double time_difference = (double) ((now_time - timestamp) / (1000 * 60 * 60 * 24));
                double periods = time_difference / 30;
                Log.e("....", String.valueOf(periods));
                int m = (int) Math.ceil(periods);
                ((MyViewHolder) viewHold).loan_start_overdue_time.setText("M" + String.valueOf(m));
                Log.e("...", String.valueOf(time_difference));
                Log.e("开始预期时间", String.valueOf(timestamp));
                Log.e("现在时间", String.valueOf(now_time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((MyViewHolder) viewHold).collecting_days.setText(mlist.get(position).getCollecting_days());
            ((MyViewHolder) viewHold).vehicle_possible_address.setText(mlist.get(position).getVehicle_possible_address());
        }
        if (viewHold instanceof FooterViewHolder) {

            if (isshow == 0) {
                ((FooterViewHolder) viewHold).loading.setVisibility(View.VISIBLE);
                ((FooterViewHolder) viewHold).firstBar.setVisibility(View.GONE);
                ((FooterViewHolder) viewHold).loading.setText("上拉加载更多");
            } else if (isshow==1){
                ((FooterViewHolder) viewHold).loading.setVisibility(View.VISIBLE);
                ((FooterViewHolder) viewHold).firstBar.setVisibility(View.VISIBLE);
                ((FooterViewHolder) viewHold).loading.setText("正在加载");
            }else if (isshow==2){
                ((FooterViewHolder) viewHold).loading.setVisibility(View.VISIBLE);
                ((FooterViewHolder) viewHold).firstBar.setVisibility(View.GONE);
                ((FooterViewHolder) viewHold).loading.setText("已全部加载");
            }
            else {
                ((FooterViewHolder) viewHold).firstBar.setVisibility(View.GONE);
                ((FooterViewHolder) viewHold).loading.setVisibility(View.GONE);

            }

        }
    }

    @Override
    public int getItemCount() {
        return mlist.size() + 1;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar firstBar;
        private TextView loading;

        public FooterViewHolder(View view) {
            super(view);
            firstBar = (ProgressBar) view.findViewById(R.id.firstBar);
            loading = (TextView) view.findViewById(R.id.loading);
        }

    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private MyItemClickListener myItemClickListener;
        private MyItemLongClickListener myItemLongClickListener;
        private TextView get_level, loan_type, demands_of_collection, vehicle_has_gps, vehicle_has_illegal,
                collection_commission, loan_start_overdue_time, collecting_days, vehicle_possible_address;

        public MyViewHolder(View view, MyItemClickListener myItemClickListener, MyItemLongClickListener myItemLongClickListener) {
            super(view);
            get_level = (TextView) view.findViewById(R.id.get_level);
            loan_type = (TextView) view.findViewById(R.id.loan_type);
            demands_of_collection = (TextView) view.findViewById(R.id.demands_of_collection);
            vehicle_has_gps = (TextView) view.findViewById(R.id.vehicle_has_gps);
            vehicle_has_illegal = (TextView) view.findViewById(R.id.vehicle_has_illegal);
            collection_commission = (TextView) view.findViewById(R.id.collection_commission);
            loan_start_overdue_time = (TextView) view.findViewById(R.id.loan_start_overdue_time);
            collecting_days = (TextView) view.findViewById(R.id.collecting_days);
            vehicle_possible_address = (TextView) view.findViewById(R.id.vehicle_possible_address);
            this.myItemClickListener = myItemClickListener;
            this.myItemLongClickListener = myItemLongClickListener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {


            if (myItemClickListener != null) {

                myItemClickListener.setItemClickListener(v, getPosition());
            }


        }

        @Override
        public boolean onLongClick(View v) {
            if (myItemLongClickListener != null) {

                myItemLongClickListener.setItemLongClickListener(v, getPosition());
            }
            return true;
        }
    }
}
