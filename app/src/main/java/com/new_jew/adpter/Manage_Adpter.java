package com.new_jew.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.bean.Manage_Bean;
import com.new_jew.customview.RoundImageView;
import com.new_jew.net.Load_image;

import java.util.List;

/**
 * Created by zhangpei on 2016/11/25.
 */
public class Manage_Adpter extends RecyclerView.Adapter{
    private Context context;
    private List<Manage_Bean> list;

    public Manage_Adpter(Context context, List<Manage_Bean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHold viewHold=new MyViewHold(LayoutInflater.from(context).inflate(R.layout.layout_manage_item,parent,false));
        return viewHold;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         if (holder instanceof MyViewHold){

             Load_image.Setimage(list.get(position).getManage_list_img(),((MyViewHold) holder).manage_list_img);
             ((MyViewHold) holder).manage_name.setText(list.get(position).getManage_name());
             ((MyViewHold) holder).manage_phonenumber.setText(list.get(position).getManage_phonenumber());
             ((MyViewHold) holder).on_expedite.setText(list.get(position).getOn_expedite());
             ((MyViewHold) holder).comple_number.setText(list.get(position).getComple_number());

         }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHold extends RecyclerView.ViewHolder{
    private RoundImageView manage_list_img;
        private TextView manage_name,manage_phonenumber,on_expedite,comple_number;
        public MyViewHold(View itemView) {
            super(itemView);
            manage_list_img= (RoundImageView) itemView.findViewById(R.id.manage_list_img);
            manage_name= (TextView) itemView.findViewById(R.id.manage_name);
            manage_phonenumber= (TextView) itemView.findViewById(R.id.manage_phonenumber);
            on_expedite= (TextView) itemView.findViewById(R.id.on_expedite);
            comple_number= (TextView) itemView.findViewById(R.id.comple_number);

        }
    }
}
