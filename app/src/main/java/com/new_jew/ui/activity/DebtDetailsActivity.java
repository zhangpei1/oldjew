package com.new_jew.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.adpter.DebtDetailsAdpter;
import com.new_jew.customview.ConstantDialog;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.TabLongUtil;
import com.new_jew.ui.frament.debtdetailsFrament.CarMsgFragment;
import com.new_jew.ui.frament.debtdetailsFrament.DebtMsgFragment;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/22.
 */
public class DebtDetailsActivity extends BaseActivity {
    @ViewInject(R.id.debt_details_tab)
    private android.support.design.widget.TabLayout debt_details_tab;
    @ViewInject(R.id.debt_details_viewpager)
    private ViewPager debt_details_viewpager;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.title_image)
    private TextView title_image;
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.ensure)
    private Button ensure;

    private List<Fragment> list;
    private String id = "";
    private String title[]={"债务详情","车辆信息"};
    private ConstantDialog   constantDialogdialog;


    @Override
    protected void initLayout() {
        x.view().inject(this);
        Intent intent = getIntent();
        Constants.id = intent.getExtras().getString("id");
        Log.e("id", id);
        title_text.setText("委单信息");


    }

    @Override
    protected void initValue() {
        list = new ArrayList<>();
        list.add(new DebtMsgFragment());
        list.add(new CarMsgFragment());
        debt_details_viewpager.setAdapter(new DebtDetailsAdpter(getSupportFragmentManager(), list,title));
        debt_details_viewpager.setOffscreenPageLimit(4);
        debt_details_tab.setupWithViewPager(debt_details_viewpager);
        debt_details_tab.setTabMode(TabLayout.MODE_FIXED);
        TabLongUtil.setlong(debt_details_tab, 10f, 10f);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constantDialogdialog = new ConstantDialog(DebtDetailsActivity.this, R.style.Dialog);
                constantDialogdialog.setText("确认抢单？");
                constantDialogdialog.setnegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pick_orders();
                    }
                });
                constantDialogdialog.setpositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        constantDialogdialog.dismiss();

                    }
                });
                constantDialogdialog.show();
            }
        });

    }
    //摘单
    void pick_orders() {
        dialog.show();
        RequestParams params = new RequestParams(Api.debt.debt +  Constants.id + "/obtain/");
        HttpUtils.Posthttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                dialog.dismiss();
                Log.e("摘单", result.toString());
                Toast.makeText(getApplicationContext(), "抢单成功", Toast.LENGTH_SHORT).show();
                finish();
                constantDialogdialog.dismiss();

            }

            @Override
            public void onFailure() {
                dialog.dismiss();
                constantDialogdialog.dismiss();
            }
        });


    }
    @Override
    protected int setRootView() {
        return R.layout.layout_debtdetail;
    }


}
