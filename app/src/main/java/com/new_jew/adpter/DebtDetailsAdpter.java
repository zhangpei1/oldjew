package com.new_jew.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zhangpei on 2016/11/22.
 */
public class DebtDetailsAdpter extends FragmentPagerAdapter{
    private String title[]=null;

    private List<Fragment> list;
    public DebtDetailsAdpter(FragmentManager fm,List<Fragment> list,String title[]) {
        super(fm);
      this.list=list;
        this.title=title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }


}
