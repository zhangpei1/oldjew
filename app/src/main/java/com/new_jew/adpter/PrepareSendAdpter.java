package com.new_jew.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.new_jew.R;
import com.new_jew.bean.PrepareSendBean;
import com.new_jew.customview.MyCirclepRrogress;
import com.new_jew.toolkit.CollectionRecordItemOnClick;

import java.util.List;

/**
 * Created by zhangpei on 2016/11/17.
 */
public class PrepareSendAdpter extends BaseAdapter {
    private Context context;
    private List<PrepareSendBean> mlist;
    private LayoutInflater inflater;
    private CollectionRecordItemOnClick itemOnClick;

    public PrepareSendAdpter(Context context, List<PrepareSendBean> mlist) {
        this.context = context;
        this.mlist = mlist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

   public void setCollectionRecordItemOnClick(CollectionRecordItemOnClick itemOnClick){
                this.itemOnClick=itemOnClick;
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
            view = inflater.inflate(R.layout.layout_sendorder_item, null, false);
            viewHold.mylist_level = (TextView) view.findViewById(R.id.mylist_level);
            viewHold.mylist_type = (TextView) view.findViewById(R.id.mylist_type);
            viewHold.mylist_appeal = (TextView) view.findViewById(R.id.mylist_appeal);
            viewHold.car_type= (TextView) view.findViewById(R.id.car_type);
            viewHold.car_id_number= (TextView) view.findViewById(R.id.car_id_number);
            viewHold.car_location= (TextView) view.findViewById(R.id.car_location);
            viewHold.cpv_finance= (MyCirclepRrogress) view.findViewById(R.id.cpv_finance);
            view.setTag(viewHold);
        }else {
            viewHold= (ViewHold) view.getTag();

        }
        viewHold.mylist_level.setText(mlist.get(position).getMylist_level());
        viewHold.mylist_type.setText(mlist.get(position).getMylist_type());
        viewHold.mylist_appeal.setText(mlist.get(position).getMylist_appeal());
        viewHold.car_type.setText(mlist.get(position).getCar_type());
        viewHold.car_id_number.setText(mlist.get(position).getCar_id_number());
        viewHold.car_location.setText(mlist.get(position).getCar_location());
        viewHold.cpv_finance.setDown_text("剩余"+String.valueOf(mlist.get(position).getDay())+"天");
       viewHold.cpv_finance.setPercent((int) mlist.get(position).getPercentage());
        final int i=position;
        viewHold.cpv_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  itemOnClick.ItemOnClick(v,i);
            }
        });
        return view;
    }

    class ViewHold {
        TextView mylist_level, mylist_type, mylist_appeal, car_type, car_id_number, car_location;
        MyCirclepRrogress cpv_finance;


    }
}
