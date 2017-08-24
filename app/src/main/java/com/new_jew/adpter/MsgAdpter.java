package com.new_jew.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.bean.MsgBean;
import com.new_jew.toolkit.TimeUtil;

import java.util.List;

/**
 * Created by zhangpei on 2016/12/1.
 */
public class MsgAdpter extends RecyclerView.Adapter<MsgAdpter.MyViewHold>{
    private Context context;
    private List<MsgBean> mlist;

    public MsgAdpter(Context context, List<MsgBean> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public MsgAdpter.MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHold holder = new MyViewHold(LayoutInflater.from(context).inflate(R.layout.layout_msg_item,
                parent, false));
        return holder;


    }

    @Override
    public void onBindViewHolder(MsgAdpter.MyViewHold hold, int position) {
             hold.title.setText(mlist.get(position).getTitle());
        String format_date=TimeUtil.getformatdata1(mlist.get(position).getCreated());
            hold.msg_data.setText(format_date);
           hold.msg_content.setText("\t\t"+mlist.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    class MyViewHold extends RecyclerView.ViewHolder{
         private TextView  title,msg_data,msg_content;

        public MyViewHold(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.title);
            msg_data= (TextView) itemView.findViewById(R.id.msg_data);
            msg_content= (TextView) itemView.findViewById(R.id.msg_content);
        }
    }
}
