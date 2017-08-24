package com.new_jew.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.bean.HistoryTrajectoryBean;
import com.new_jew.toolkit.ButtonClicklistener;
import com.new_jew.toolkit.TimeUtil;

import java.util.List;

/**
 * Created by zhangpei on 2016/12/5.
 */
public class HistoryTrajectoryAdpter extends RecyclerView.Adapter<HistoryTrajectoryAdpter.MyHoldView>{
       private Context context;
      private List<HistoryTrajectoryBean> mlist;

    public HistoryTrajectoryAdpter(Context context, List<HistoryTrajectoryBean> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    private ButtonClicklistener buttonClicklistener;
    @Override
    public MyHoldView onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHoldView holdView=new MyHoldView(LayoutInflater.from(context).inflate(R.layout.layout_history_item,parent,false));
        return holdView;
    }

    @Override
    public void onBindViewHolder(MyHoldView holder, int position) {
       String str= TimeUtil.getformatdata1(mlist.get(position).getCreated());
        holder.history_time.setText(str);
        holder.history_content.setText(mlist.get(position).getDetail());
        holder.history_adress.setText(mlist.get(position).getPosition());

    }
 public void setButtonClicklistener(ButtonClicklistener buttonClicklistener){
    this.buttonClicklistener=buttonClicklistener;

 }
    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyHoldView extends RecyclerView.ViewHolder{

         private TextView history_time,history_content,history_adress;
        private LinearLayout examine_more;
        public MyHoldView(View view) {
            super(view);

            history_time= (TextView) view.findViewById(R.id.history_time);
            history_content= (TextView) view.findViewById(R.id.history_content);
            history_adress= (TextView) view.findViewById(R.id.history_adress);
            examine_more= (LinearLayout) view.findViewById(R.id.examine_more);
            examine_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicklistener.setButtonClicklistener(v,getPosition());
                }
            });
        }
    }
}
