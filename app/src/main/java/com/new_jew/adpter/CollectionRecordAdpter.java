package com.new_jew.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.bean.CollectionRecordBean;
import com.new_jew.toolkit.CollectionRecordItemOnClick;
import com.new_jew.toolkit.DetailsOnClick;
import com.new_jew.toolkit.TimeUtil;

import java.util.List;

/**
 * Created by zhangpei on 2016/11/28.
 */
public class CollectionRecordAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CollectionRecordBean> list;
    private Context context;
    private List<String> datas;
    private CollectionRecordItemOnClick collectionRecordItemOnClick;
    private DetailsOnClick detailsOnClick;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public CollectionRecordAdpter(List<CollectionRecordBean> list,List<String> datas, Context context) {
        this.list = list;
        this.context = context;
        this.datas=datas;
    }

    public void SetItemOnClick(CollectionRecordItemOnClick collectionRecordItemOnClick) {
        this.collectionRecordItemOnClick = collectionRecordItemOnClick;
    }
    public void SetDetailsOnClick(DetailsOnClick detailsOnClick) {
        this.detailsOnClick = detailsOnClick;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {

            return TYPE_ITEM;
        } else {


            return TYPE_FOOTER;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_collection_record,
                    parent, false));
            return holder;
        } else {

            MyheadViewHold holder1 = new MyheadViewHold(LayoutInflater.from(context).inflate(R.layout.layout_order_schedule_header,
                    parent, false));
            return holder1;

        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String d ;
        Log.e("dddd",String.valueOf(position));
        if (position==0){
            if (holder instanceof MyheadViewHold) {
                if (datas.size() == 0) {

                    return;
                }
                ((MyheadViewHold) holder).order_pick.setText(datas.get(0));
                ((MyheadViewHold) holder).collection_day.setText(datas.get(1));
                ((MyheadViewHold) holder).surplus_days.setText(datas.get(2));
            }


        }else {
            if (holder instanceof MyViewHolder){
//                if (!list.get(position).getCreated().equals("")||list.get(position).getCreated()!=null){
//
//                    d=TimeUtil.getformatdata1(list.get(position).getCreated());
//                }else {
//                    d="无";
//                }
                try {
                    d=TimeUtil.getformatdata1(list.get(position-1).getCreated());
                    ((MyViewHolder) holder).record_time.setText(d);
                    ((MyViewHolder) holder).record_content.setText(list.get(position-1).getDetail());
                    ((MyViewHolder) holder).record_adress.setText(list.get(position-1).getPosition());
                    ((MyViewHolder) holder).record_collection.setText(list.get(position-1).getCollection_name());

                } catch (Exception e){

                }

            }

        }



    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView record_time, record_collection, record_content, record_adress, examine_accessory;

        public MyViewHolder(View itemView) {
            super(itemView);
            record_time = (TextView) itemView.findViewById(R.id.record_time);
            record_collection = (TextView) itemView.findViewById(R.id.record_collection);
            record_content = (TextView) itemView.findViewById(R.id.record_content);
            record_adress = (TextView) itemView.findViewById(R.id.record_adress);
            examine_accessory = (TextView) itemView.findViewById(R.id.examine_accessory);
            examine_accessory.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            collectionRecordItemOnClick.ItemOnClick(v, getPosition());
        }
    }

    class MyheadViewHold extends RecyclerView.ViewHolder {

        private TextView order_pick, collection_day, surplus_days, order_details;

        public MyheadViewHold(View itemView) {
            super(itemView);
            order_pick = (TextView) itemView.findViewById(R.id.order_pick);
            collection_day = (TextView) itemView.findViewById(R.id.collection_day);
            surplus_days = (TextView) itemView.findViewById(R.id.surplus_days);
            order_details = (TextView) itemView.findViewById(R.id.order_details);
            order_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailsOnClick.detailsOnClick(v);
                }
            });
        }
    }
}
