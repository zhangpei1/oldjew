package com.new_jew.customview;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.adpter.SendOrderAdpter;
import com.new_jew.toolkit.MyItemClickListener;

import java.util.List;

/**
 * Created by zhangpei on 2016/12/2.
 */
public class SendOderDialog extends Dialog{
    private TextView cancel,send_order;
    private RecyclerView dialog_recyclerView;
    public SendOrderAdpter adpter;

    public SendOderDialog(Context context,List<String> list) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_send_orders, null);
        cancel= (TextView) view.findViewById(R.id.cancel);
        send_order= (TextView) view.findViewById(R.id.send_order);
        dialog_recyclerView= (RecyclerView) view.findViewById(R.id.dialog_recyclerView);
        adpter=new SendOrderAdpter(context,list);
        dialog_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dialog_recyclerView.setAdapter(adpter);
        setContentView(view);
    }

    public void setpositiveButton(View.OnClickListener onClickListener) {
        this.cancel.setOnClickListener(onClickListener);
    }

    public void setnegativeButton(View.OnClickListener onClickListener) {
        this.send_order.setOnClickListener(onClickListener);
    }
    public  void setitemClick(MyItemClickListener onClickListener){
        this.adpter.setMyItemClickListener(onClickListener);
    }

}
