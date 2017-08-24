package com.new_jew.ui.frament;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.new_jew.BaseFrament;
import com.new_jew.R;
import com.new_jew.bean.LeveDebtBean;
import com.new_jew.adpter.ScreenAdpter;
import com.new_jew.bean.DebListBean;
import com.new_jew.bean.DebtAdpter;
import com.new_jew.bean.ScreenBean;
import com.new_jew.customview.ConstantDialog;
import com.new_jew.customview.CustomDialog;
import com.new_jew.customview.GrapeGridview;
import com.new_jew.global.Api;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.toolkit.AppJsonFileReader;
import com.new_jew.toolkit.ButtonClicklistener;
import com.new_jew.toolkit.MyItemClickListener;
import com.new_jew.toolkit.TimeUtil;
import com.new_jew.toolkit.UpdateManager;
import com.new_jew.ui.activity.DebtDetailsActivity;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangpei on 2016/11/9.
 */
public class DebtFrament extends BaseFrament implements View.OnClickListener, MyItemClickListener, SwipeRefreshLayout.OnRefreshListener, ButtonClicklistener {
    private RecyclerView id_recyclerview;
    private AppBarLayout appbar;
    private List<DebListBean> mDatas;
    private DebtAdpter mAdapter;
    private Toolbar toolbar;
    private ImageView backdrop;
    private CollapsingToolbarLayout collapsing_toolbar;
    private DrawerLayout drawerlayout;
    private TextView province_text, city_text;
    private TextView chose_text, chose_city;
    private WheelView wv;
    private String city_name = "";
    private List<String> list = new ArrayList<>();
    private List<String> citylist = new ArrayList<>();
    private ArrayList<List> city_list = null;
    private AlertDialog mdialog;
    private GrapeGridview money_debt;
    private GrapeGridview level_debt;
    private ScreenAdpter adpter, isgps_adpter, isrecord_adpter;
    private LeveDebtBean levelAdpter;
    private ArrayList<ScreenBean> screen_list;
    private ArrayList<ScreenBean> level_debt_list;
    private ArrayList<ScreenBean> is_gps_list, is_record_list;
    private Button reset, complete_button;
    private int collection_commission_min = 0;//催收范围小
    private int collection_commission_max = 0;//催收范围大
    private String loan_start_overdue_time_min = "";//佣金范围小
    private String loan_start_overdue_time_max = "";//佣金范围大
    private JSONObject area_json; //省市解析根json对象
    private ArrayList<String> key_list; //省市解析键名
    private SwipeRefreshLayout refresh;//刷新控件
    private ArrayList<String> orderlist_id = null;//委单id；
    private ConstantDialog constantDialogdialog;
    private GrapeGridview is_gps, is_record;
    private int pagers = 1;
    private boolean screen = false;
    private CustomDialog customdialog;

    //筛选
    private TextView screen_text;

    private String gps_number = "";//筛选GPS 1为全 2为有 3为无
    private String record_number = "";//筛选违章 1为全 2为有 3为无
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

                mAdapter.notifyDataSetChanged();
            } else {
                if (screen == true) {
                    RequestParams params = null;
                    pagers++;
                    getdebtitem(params, pagers);
                } else {
                    pagers++;
                    getdebtitem(pagers);
                }
            }
        }
    };

    @Override
    protected void initLayout() {
        customdialog = new CustomDialog(getActivity());
        UpdateManager.Update(getActivity(), false, customdialog);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        backdrop = (ImageView) view.findViewById(R.id.backdrop);
        collapsing_toolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        drawerlayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        province_text = (TextView) getActivity().findViewById(R.id.province_text);
        city_text = (TextView) getActivity().findViewById(R.id.city_text);
        money_debt = (GrapeGridview) getActivity().findViewById(R.id.money_debt);
        level_debt = (GrapeGridview) getActivity().findViewById(R.id.level_debt);
        is_gps = (GrapeGridview) getActivity().findViewById(R.id.is_gps);
        is_record = (GrapeGridview) getActivity().findViewById(R.id.is_record);
        reset = (Button) getActivity().findViewById(R.id.reset);
        complete_button = (Button) getActivity().findViewById(R.id.complete_button);
        id_recyclerview = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        screen_text = (TextView) view.findViewById(R.id.screen);


    }

    @Override
    protected void initValue() {
        orderlist_id = new ArrayList<>();
        getProvincesandcities();//加载省市
        toolbar.setTitle("");
//        setHasOptionsMenu(true);
        collapsing_toolbar.setTitle("债务大厅");
        collapsing_toolbar.setCollapsedTitleGravity(Gravity.CENTER);
        collapsing_toolbar.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.textsize);
        collapsing_toolbar.setExpandedTitleGravity(Gravity.TOP);
        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.ExpandedTextSize);
        collapsing_toolbar.setExpandedTitleColor(Color.parseColor("#00000000"));
        appbar.setExpanded(true, true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mDatas = new ArrayList<>();
        id_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DebtAdpter(getActivity(), mDatas);
        id_recyclerview.setAdapter(mAdapter);


        //筛选
        //是否有GPS
        is_gps_list = new ArrayList<>();
        is_gps_list.add(new ScreenBean("不限", false));
        is_gps_list.add(new ScreenBean("有", false));
        is_gps_list.add(new ScreenBean("无", false));
        isgps_adpter = new ScreenAdpter(is_gps_list, getActivity());
        is_gps.setAdapter(isgps_adpter);
        //是否有违章
        is_record_list = new ArrayList<>();
        is_record_list.add(new ScreenBean("不限", false));
        is_record_list.add(new ScreenBean("有", false));
        is_record_list.add(new ScreenBean("无", false));
        isrecord_adpter = new ScreenAdpter(is_record_list, getActivity());
        is_record.setAdapter(isrecord_adpter);
        //佣金范围
        screen_list = new ArrayList<>();
        adpter = new ScreenAdpter(screen_list, getActivity());
        money_debt.setAdapter(adpter);
        screen_list.add(new ScreenBean("不限", false));
        screen_list.add(new ScreenBean("5千元以下", false));
        screen_list.add(new ScreenBean("5千元-1万元", false));
        screen_list.add(new ScreenBean("1万元-5万元", false));
        screen_list.add(new ScreenBean("5万元以上", false));
//逾期级别
        level_debt_list = new ArrayList<>();
        levelAdpter = new LeveDebtBean(level_debt_list, getActivity());
        level_debt.setAdapter(levelAdpter);
        level_debt_list.add(new ScreenBean("不限", false));
        level_debt_list.add(new ScreenBean("M1-M3", false));
        level_debt_list.add(new ScreenBean("M4-M6", false));
        level_debt_list.add(new ScreenBean("M7-M12", false));
        level_debt_list.add(new ScreenBean("M12以上", false));
        Glide.with(this).load(R.drawable.banner).centerCrop().into(backdrop);


    }

    @Override
    protected void initListener() {
        refresh.setOnRefreshListener(this);
//        is_gps.setOnClickListener(this);
//        is_record.setOnClickListener(this);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_text:
                        drawerlayout.openDrawer((GravityCompat.END));

                        break;


                }
                return true;
            }
        });
        province_text.setOnClickListener(this);
        city_text.setOnClickListener(this);
        reset.setOnClickListener(this);
        complete_button.setOnClickListener(this);
        money_debt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adpter.setSeclection(position);
                adpter.notifyDataSetChanged();
                switch (position) {
                    case 0:

                        collection_commission_min = 0;
                        collection_commission_max = 0;
                        break;
                    case 1:
                        collection_commission_min = 0;
                        collection_commission_max = 5000;

                        break;
                    case 2:

                        collection_commission_min = 5000;
                        collection_commission_max = 10000;
                        break;
                    case 3:

                        collection_commission_min = 10000;
                        collection_commission_max = 50000;
                        break;
                    case 4:
                        collection_commission_min = 50000;
                        collection_commission_max = 0;

                        break;


                }

            }
        });

        level_debt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        loan_start_overdue_time_max = "";
                        loan_start_overdue_time_min = "";
                        break;
                    case 1:
                        loan_start_overdue_time_max = TimeUtil.getMothvalue(0);
                        loan_start_overdue_time_min = TimeUtil.getMothvalue(-90);
                        break;
                    case 2:
                        loan_start_overdue_time_max = TimeUtil.getMothvalue(-91);
                        loan_start_overdue_time_min = TimeUtil.getMothvalue(-180);
                        break;
                    case 3:
                        loan_start_overdue_time_max = TimeUtil.getMothvalue(-181);
                        loan_start_overdue_time_min = TimeUtil.getMothvalue(-360);
                        break;
                    case 4:
                        loan_start_overdue_time_max = TimeUtil.getMothvalue(-361);
                        loan_start_overdue_time_min = "";
                        break;
                }

                levelAdpter.setSeclection(position);
                levelAdpter.notifyDataSetChanged();
            }
        });
        this.mAdapter.setOnItemClickListener(this);
        this.mAdapter.setButtonClicklistener(this);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    refresh.setEnabled(true);

//                        Window w = getActivity().getWindow();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        w.setStatusBarColor(Color.parseColor("#ffcd42"));
//                    }


                } else {
                    refresh.setEnabled(false);
//                    Window w = getActivity().getWindow();
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                        w.setStatusBarColor(Color.parseColor("#3290f2"));
//                    }
                }

//                if( state == State.EXPANDED ) {
//
//                    //展开状态
//
//                }else if(state == State.COLLAPSED){
//
//                    //折叠状态
//
//                }else {
//
//                    //中间状态
//
//                }
            }
        });
//判断上啦加载
        id_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                mAdapter.setishow(0);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //如果到了底部
                if (isSlideToBottom(recyclerView)) {
                    mAdapter.setishow(0);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setishow(1);
                    mAdapter.notifyDataSetChanged();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                }
            }
        });
//GPS 筛选
        is_gps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isgps_adpter.setSeclection(position);
                isgps_adpter.notifyDataSetChanged();
                switch (position) {
                    case 0:
                        gps_number = "1";
                        break;

                    case 1:
                        gps_number = "2";
                        break;
                    case 2:
                        gps_number = "3";
                        break;
                }
            }
        });
        //违章筛选
        is_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isrecord_adpter.setSeclection(position);
                isrecord_adpter.notifyDataSetChanged();
                switch (position) {
                    case 0:
                        record_number = "1";
                        break;

                    case 1:
                        record_number = "2";
                        break;
                    case 2:
                        record_number = "3";
                        break;
                }
            }
        });

        screen_text.setOnClickListener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_dabtframent;
    }

    protected void initData() {
        mDatas = new ArrayList<>();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toobar_right, menu);//加载menu文件到布局
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //选择省
            case R.id.province_text:
                WheelViewDialog wheelViewDialog = new WheelViewDialog(getActivity());
                wheelViewDialog.setDialogStyle(Color.parseColor("#ffff00"));
                View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_whleeview, null);
                chose_text = (TextView) outerView.findViewById(R.id.chose_text);
                chose_text.setText("请选择省");
                wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setWheelAdapter(new ArrayWheelAdapter(getActivity())); // 文本数据源
                wv.setSkin(WheelView.Skin.Holo); // common皮肤
                wv.setWheelClickable(true);
                wv.setLoop(false);
                wv.setWheelData(list);
                // 数据集合
                wv.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object o) {
                        citylist.clear();
                        province_text.setText(list.get(position).toString());
                        try {
                            JSONObject city_json = new JSONObject(area_json.getString(key_list.get(position)));
                            Iterator iterator = city_json.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                citylist.add(city_json.getString(key));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mdialog.dismiss();
                        if (citylist.size() == 0) {

                            city_text.setText("全部");
                        }
                        WheelViewDialog wheelViewDialog_city = new WheelViewDialog(getActivity());
                        wheelViewDialog_city.setDialogStyle(Color.parseColor("#ffff00"));
                        View outerView_city = LayoutInflater.from(getActivity()).inflate(R.layout.layout_whleeview, null);
                        chose_city = (TextView) outerView_city.findViewById(R.id.chose_text);
                        chose_city.setText("请选择市");
                        wv = (WheelView) outerView_city.findViewById(R.id.wheel_view_wv);
                        wv.setWheelAdapter(new ArrayWheelAdapter(getActivity())); // 文本数据源
                        wv.setSkin(WheelView.Skin.Holo); // common皮肤
                        wv.setWheelClickable(true);
//        wv.setWheelSize(5);
                        wv.setLoop(false);
                        if (citylist.size() != 0) {
                            wv.setWheelData(citylist);
                        } else {

                            Toast.makeText(getActivity(), "城市列表为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 数据集合
                        wv.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
                            @Override
                            public void onItemClick(int position, Object o) {

                                city_name = citylist.get(position);
                                city_text.setText(citylist.get(position));

                                mdialog.dismiss();


                            }
                        });
                        wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                            @Override
                            public void onItemSelected(int position, Object o) {

                            }

                        });

                        mdialog = new AlertDialog.Builder(getActivity())
                                .setView(outerView_city)
                                .show();

                    }
                });
                wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, Object o) {


                    }

                });

                mdialog = new AlertDialog.Builder(getActivity())
                        .setView(outerView)
                        .show();
                break;
//选择城市
            case R.id.city_text:
                if (citylist.size() == 0) {

                    Toast.makeText(getActivity(), "请选择省或城市列表为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                WheelViewDialog wheelViewDialog_city = new WheelViewDialog(getActivity());
                wheelViewDialog_city.setDialogStyle(Color.parseColor("#ffff00"));
                View outerView_city = LayoutInflater.from(getActivity()).inflate(R.layout.layout_whleeview, null);
                wv = (WheelView) outerView_city.findViewById(R.id.wheel_view_wv);
                wv.setWheelAdapter(new ArrayWheelAdapter(getActivity())); // 文本数据源
                wv.setSkin(WheelView.Skin.Holo); // common皮肤
                wv.setWheelClickable(true);
                wv.setLoop(false);
                wv.setWheelData(citylist);
                // 数据集合
                wv.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object o) {
                        city_name = citylist.get(position);
                        city_text.setText(citylist.get(position));
                        mdialog.dismiss();


                    }
                });
                wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, Object o) {

                    }

                });

                mdialog = new AlertDialog.Builder(getActivity())
                        .setView(outerView_city)
                        .show();

                break;
            //重置
            case R.id.reset:
                isgps_adpter.setSeclection(-1);
                isgps_adpter.notifyDataSetChanged();

                isrecord_adpter.setSeclection(-1);
                isrecord_adpter.notifyDataSetChanged();

                adpter.setSeclection(-1);
                adpter.notifyDataSetChanged();
                levelAdpter.setSeclection(-1);
                levelAdpter.notifyDataSetChanged();
                province_text.setText("选择");
                city_text.setText("选择");
                city_name = "";
                collection_commission_max = 0;
                collection_commission_min = 0;
                loan_start_overdue_time_max = "";
                loan_start_overdue_time_min = "";

                record_number = "";
                gps_number = "";
                break;
            case R.id.complete_button://确定
                screen = true;
                drawerlayout.closeDrawer((GravityCompat.END));
                mDatas.clear();
                orderlist_id.clear();
                RequestParams params = null;
                pagers = 1;
                getdebtitem(params, pagers);
                break;

            case R.id.screen:
                drawerlayout.openDrawer((GravityCompat.END));

                break;

        }

    }

    //省市选择解析
    void getProvincesandcities() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                key_list = new ArrayList<String>();
                String json = AppJsonFileReader.getJson(getActivity(), "area.json");
                try {
                    area_json = new JSONObject(json);
                    JSONObject province_json = new JSONObject(area_json.get("86").toString());

                    Iterator iterator = province_json.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
//                        Log.e("值",province_json.getString(key));
                        key_list.add(key);
                        //    value = jsonObject.getString(key);
                        list.add(province_json.getString(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                list = date.getString();
//                city_list = date.getCityString();
            }
        }).start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        date = null;
        list = null;
        city_list = null;
    }

    //数据加载
    void getdebtitem(final int pages) {

        RequestParams params = new RequestParams(Api.debt.debt);
        params.addParameter("limit", 7);
        params.addParameter("offset", (pages - 1) * 7);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {

//                mAdapter.notifyItemRemoved(mDatas.size()+1);
                refresh.setRefreshing(false);
                Log.e("result", result.toString());
                if (pages == 1) {
                    mDatas.clear();
                    orderlist_id.clear();
                }


                try {
                    JSONObject json = new JSONObject(result.toString());
                    JSONArray ajson = new JSONArray(json.getString("results"));
                    if (ajson.length() == 0) {
                        mAdapter.setishow(2);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    Message message = new Message();
                                    message.what = 0;
                                    handler.sendMessage(message);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {
                        JSONObject json1 = new JSONObject(ajson.get(i).toString());
                        mDatas.add(new DebListBean(json1.getString("get_level"), json1.getString("loan_type"),
                                json1.getString("demands_of_collection"), json1.getString("collection_commission"),
                                json1.getString("loan_start_overdue_time"), json1.getString("collecting_days"),
                                json1.getString("vehicle_possible_address"), "", "", json1.getBoolean("vehicle_has_gps"),
                                json1.getBoolean("vehicle_has_illegal")));
                        orderlist_id.add(json1.getString("id"));
                        ;


                    }
                    mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {
            }
        });


    }

    //数据加载筛选
    void getdebtitem(RequestParams params, int pages) {
        refresh.setRefreshing(true);
//        dialog.show();
        params = new RequestParams(Api.debt.debt + "?");
        params.addParameter("limit", 7);
        params.addParameter("offset", (pages - 1) * 7);
        params.addBodyParameter("vehicle_possible_address", city_name);
        if (collection_commission_max != 0) {
            params.addParameter("collection_commission_max", collection_commission_max);
        }
        if (collection_commission_min != 0) {
            params.addParameter("collection_commission_min", collection_commission_min);
        }
        if (!loan_start_overdue_time_max.equals("")) {
            Log.e("loaue_time_max", loan_start_overdue_time_max.toString());

            params.addParameter("loan_start_overdue_time_max", loan_start_overdue_time_max);
        }
        if (!loan_start_overdue_time_min.equals("")) {

            params.addParameter("loan_start_overdue_time_min", loan_start_overdue_time_min);
        }
        if (gps_number.equals("2")) {
            params.addParameter("vehicle_has_gps", true);
        }
        if (gps_number.equals("3")) {
            params.addParameter("vehicle_has_gps", false);
        }


        if (record_number.equals("2")) {
            params.addParameter("vehicle_has_illegal", true);
        }
        if (record_number.equals("3")) {
            params.addParameter("vehicle_has_illegal", false);
        }


        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
//                dialog.dismiss();
                refresh.setRefreshing(false);
                Log.e("筛选", result.toString());
                try {
                    JSONObject json = new JSONObject(result.toString());
                    JSONArray ajson = new JSONArray(json.getString("results"));
                    if (ajson.length() == 0) {
                        mAdapter.setishow(2);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    Message message = new Message();
                                    message.what = 0;
                                    handler.sendMessage(message);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        return;
                    }
                    for (int i = 0; i < ajson.length(); i++) {
                        JSONObject json1 = new JSONObject(ajson.get(i).toString());
                        mDatas.add(new DebListBean(json1.getString("get_level"), json1.getString("loan_type"),
                                json1.getString("demands_of_collection"), json1.getString("collection_commission"),
                                json1.getString("loan_start_overdue_time"), json1.getString("collecting_days"),
                                json1.getString("vehicle_possible_address"), "", "", json1.getBoolean("vehicle_has_gps"),
                                json1.getBoolean("vehicle_has_illegal")));
                        orderlist_id.add(json1.getString("id"));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {
//                dialog.dismiss();
                Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();

            }
        });


    }

    //跳转详情
    @Override
    public void setItemClickListener(View v, int position) {
        Intent intent = new Intent(getActivity(), DebtDetailsActivity.class);
        intent.putExtra("id", orderlist_id.get(position));
        startActivity(intent);
    }

    //刷新
    @Override
    public void onRefresh() {
        pagers = 1;
        getdebtitem(pagers);
        screen = false;


    }

    //抢单
    @Override
    public void setButtonClicklistener(View v, final int position) {
        constantDialogdialog = new ConstantDialog(getActivity(), R.style.Dialog);
        final int number = position;
        constantDialogdialog.setText("确认抢单？");
        constantDialogdialog.setnegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_orders(orderlist_id.get(number).toString());
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

    //摘单
    void pick_orders(String id) {
        dialog.show();
        RequestParams params = new RequestParams(Api.debt.debt + id + "/obtain/");
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
                Toast.makeText(getActivity(), "抢单成功", Toast.LENGTH_SHORT).show();
                constantDialogdialog.dismiss();

            }

            @Override
            public void onFailure() {

            }
        });


    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (screen == false) {
            pagers = 1;
            getdebtitem(pagers);
        } else {

        }
    }


}
