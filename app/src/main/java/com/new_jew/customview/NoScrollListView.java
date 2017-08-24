package com.new_jew.customview;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by zhangpei on 2016/12/9.
 */
public class NoScrollListView extends PullToRefreshListView{
    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context, Mode mode) {
        super(context, mode);
    }

//    public NoScrollListView(Context context, Mode mode, AnimationStyle style) {
//        super(context, mode, style);
//    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
