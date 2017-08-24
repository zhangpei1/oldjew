package com.new_jew.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.bean.AttestationDataBean;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.DataAttestationTask;
import com.new_jew.global.IOCallback;
import com.new_jew.net.HttpUtils;
import com.new_jew.net.Load_image;
import com.new_jew.toolkit.AppJsonFileReader;
import com.new_jew.toolkit.BitMapUtil;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangpei on 2016/11/11.
 */
public class DataAttestationActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.linear_ishow)
    private LinearLayout linear_ishow;
    @ViewInject(R.id.linear_ishow1)
    private LinearLayout linear_ishow1;
    @ViewInject(R.id.linear_ishow2)
    private LinearLayout linear_ishow2;
    @ViewInject(R.id.linear_ishow3)
    private LinearLayout linear_ishow3;
    @ViewInject(R.id.linear_ishow4)
    private LinearLayout linear_ishow4;
    @ViewInject(R.id.linear_ishow5)
    private LinearLayout linear_ishow5;
    @ViewInject(R.id.company_data)
    private RelativeLayout company_data;
    @ViewInject(R.id.company_data1)
    private RelativeLayout company_data1;
    @ViewInject(R.id.company_data2)
    private RelativeLayout company_data2;
    @ViewInject(R.id.company_data3)
    private RelativeLayout company_data3;
    @ViewInject(R.id.company_data4)
    private RelativeLayout company_data4;
    @ViewInject(R.id.company_data5)
    private RelativeLayout company_data5;
    @ViewInject(R.id.arrows)
    private ImageView arrows;
    @ViewInject(R.id.arrows1)
    private ImageView arrows1;
    @ViewInject(R.id.arrows2)
    private ImageView arrows2;
    @ViewInject(R.id.arrows3)
    private ImageView arrows3;
    @ViewInject(R.id.arrows4)
    private ImageView arrows4;
    @ViewInject(R.id.arrows5)
    private ImageView arrows5;
    @ViewInject(R.id.business_name)
    private EditText business_name;
    @ViewInject(R.id.set_up_time)
    private EditText set_up_time;
    @ViewInject(R.id.registered_capital)
    private EditText registered_capital;
    @ViewInject(R.id.credit_cooperative_number)
    private EditText credit_cooperative_number;
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.title_image)
    private TextView title_image;
    private TextView chose_text, chose_city;
    private ArrayList<String> key_list; //省市解析键名
    private WheelView wv;
    private List<String> list = new ArrayList<>();
    private List<String> citylist = new ArrayList<>();
    private JSONObject area_json; //省市解析根json对象
    private String adress = "";
    private AlertDialog mdialog;
    private EditText
            bank_license_number, contact_address, collect_type, collection_area,
            phone_collection_count, visit_collection_count, has_channel_of_complete_information, has_cooperation_lawyer,
            has_channel_of_asset_disposal, legal_name, legal_phone, legal_id_card, charge_name, charge_phone, charge_id_card,
            contact_person_name, contact_person_phone, contact_person_id_card, public_bank_number,
            public_bank_name, public_bank_person_name, private_bank_number,
            private_bank_name, private_bank_person_name, contact_email;
    private AnimationSet animationSet;
    private PopupWindow popupWindow;
    private View popview;
    private LinearLayout data_linear;
    @ViewInject(R.id.Photograph)
    private Button Photograph;
    @ViewInject(R.id.chose_picture)
    private Button chose_picture;
    @ViewInject(R.id.business_license_img)
    private ImageView business_license_img;
    private ImageView legal_number, legal_number_back,
            docking_person, docking_person_back, id_person, lawyer_certificate;
    private File file, file1, file2, file3, file4, file5, file6;
    private Intent mintent;
    private static String paths = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jew/authentication/";//sd路径
    private Bitmap head;//头像Bitmap
    private int id = 0;
    @ViewInject(R.id.sure)
    private Button sure;
    private AttestationDataBean bean;
    private CheckBox car_loan, house_loan;

    private RadioButton provincial_level, city_level, land_level, is_have, no_have, is_have_1, no_have_1, is_have_2, no_have_2;
    private int year;
    private int month;
    private int day;
    private List<String> Imalist;


    private RadioGroup business_scope, business_scope1, business_scope2, business_scope3;
    private boolean ismodify = false;

    @Override
    protected void initLayout() {
        x.view().inject(this);

        credit_cooperative_number = (EditText) this.findViewById(R.id.credit_cooperative_number);
        bank_license_number = (EditText) this.findViewById(R.id.bank_license_number);
        contact_address = (EditText) this.findViewById(R.id.contact_address);
//        collect_type = (EditText) this.findViewById(R.id.collect_type);
        collection_area = (EditText) this.findViewById(R.id.collection_area);
        phone_collection_count = (EditText) this.findViewById(R.id.phone_collection_count);
        visit_collection_count = (EditText) this.findViewById(R.id.visit_collection_count);
//        has_channel_of_complete_information = (EditText) this.findViewById(R.id.has_channel_of_complete_information);
//        has_cooperation_lawyer = (EditText) this.findViewById(R.id.has_cooperation_lawyer);
        has_channel_of_asset_disposal = (EditText) this.findViewById(R.id.has_channel_of_asset_disposal);
        legal_name = (EditText) this.findViewById(R.id.legal_name);
        legal_phone = (EditText) this.findViewById(R.id.legal_phone);
        legal_id_card = (EditText) this.findViewById(R.id.legal_id_card);
        charge_name = (EditText) this.findViewById(R.id.charge_name);
        charge_phone = (EditText) this.findViewById(R.id.charge_phone);
        charge_id_card = (EditText) this.findViewById(R.id.charge_id_card);
        contact_person_name = (EditText) this.findViewById(R.id.contact_person_name);
        contact_person_phone = (EditText) this.findViewById(R.id.contact_person_phone);
        contact_person_id_card = (EditText) this.findViewById(R.id.contact_person_id_card);
        legal_number = (ImageView) this.findViewById(R.id.legal_number);
        legal_number_back = (ImageView) this.findViewById(R.id.legal_number_back);
        docking_person = (ImageView) this.findViewById(R.id.docking_person);
        docking_person_back = (ImageView) this.findViewById(R.id.docking_person_back);
        id_person = (ImageView) this.findViewById(R.id.id_person);
        lawyer_certificate = (ImageView) this.findViewById(R.id.lawyer_certificate);
        public_bank_number = (EditText) this.findViewById(R.id.public_bank_number);
        public_bank_name = (EditText) this.findViewById(R.id.public_bank_name);
        public_bank_person_name = (EditText) this.findViewById(R.id.public_bank_person_name);
        private_bank_number = (EditText) this.findViewById(R.id.private_bank_number);
        private_bank_name = (EditText) this.findViewById(R.id.private_bank_name);
        private_bank_person_name = (EditText) this.findViewById(R.id.private_bank_person_name);
        contact_email = (EditText) this.findViewById(R.id.contact_email);

        car_loan = (CheckBox) this.findViewById(R.id.car_loan);
        house_loan = (CheckBox) this.findViewById(R.id.house_loan);
        provincial_level = (RadioButton) this.findViewById(R.id.provincial_level);
        city_level = (RadioButton) this.findViewById(R.id.city_level);
        land_level = (RadioButton) this.findViewById(R.id.land_level);
        is_have = (RadioButton) this.findViewById(R.id.is_have);
        no_have = (RadioButton) this.findViewById(R.id.no_have);
        is_have_1 = (RadioButton) this.findViewById(R.id.is_have_1);
        no_have_1 = (RadioButton) this.findViewById(R.id.no_have_1);
        is_have_2 = (RadioButton) this.findViewById(R.id.is_have_2);
        no_have_2 = (RadioButton) this.findViewById(R.id.no_have_2);

        popview = getLayoutInflater().inflate(R.layout.layout_popwindow, null, false);
        data_linear = (LinearLayout) this.findViewById(R.id.data_linear);
        Photograph = (Button) popview.findViewById(R.id.Photograph);
        chose_picture = (Button) popview.findViewById(R.id.chose_picture);
        File mfile = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {

            mfile = new File(paths);
            if (!mfile.exists()) {
                try {
                    mfile.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        file = new File(paths + "just.jpg");
        file1 = new File(paths + "just1.jpg");
        file2 = new File(paths + "just2.jpg");
        file3 = new File(paths + "just3.jpg");
        file4 = new File(paths + "just4.jpg");
        file5 = new File(paths + "just5.jpg");
        file6 = new File(paths + "just6.jpg");


        business_scope = (RadioGroup) this.findViewById(R.id.business_scope);
        business_scope1 = (RadioGroup) this.findViewById(R.id.business_scope1);
        business_scope2 = (RadioGroup) this.findViewById(R.id.business_scope2);
        business_scope3 = (RadioGroup) this.findViewById(R.id.business_scope3);

    }

    void callCamera() {
        popupWindow.dismiss();
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "/" + "just.jpg")));
        startActivityForResult(intent2, 2);//采用ForResult打开
//        Toast.makeText(getApplicationContext(), "相机", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断请求码
        if (requestCode == 1) {
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启摄像头
                callCamera();
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 0) {
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启摄像头

            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，创建文件夹
                file = new File(paths);
                if (!file.exists()) {

                    file.mkdir();
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    //有授权，直接开启摄像头
                    callCamera();
                }
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                popupWindow.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_PICK, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "/" + "just.jpg")));
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 2);
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initValue() {
        Imalist = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Imalist.add("");
        }

        title_text.setText("资料认证");
        title_image.setText("修改");
        getProvincesandcities();
        Log.e("子控件", String.valueOf(linear_ishow.getChildCount()));
//        business_license_img.setEnabled(false);
//        legal_number.setEnabled(false);
//        legal_number_back.setEnabled(false);
//        docking_person.setEnabled(false);
//        docking_person_back.setEnabled(false);
//        id_person.setEnabled(false);
//        lawyer_certificate.setEnabled(false);
        for (int i = 0; i < linear_ishow.getChildCount(); i++) {
            ViewGroup v = (ViewGroup) linear_ishow.getChildAt(i);
            for (int a = 0; a < v.getChildCount(); a++) {
                View v1 = v.getChildAt(a);
                v1.setEnabled(false);
            }
        }
        for (int i = 0; i < linear_ishow1.getChildCount(); i++) {
            ViewGroup v = (ViewGroup) linear_ishow1.getChildAt(i);
            for (int a = 0; a < v.getChildCount(); a++) {
                View v1 = v.getChildAt(a);
                v1.setEnabled(false);
            }
        }
        for (int i = 0; i < linear_ishow2.getChildCount(); i++) {
            ViewGroup v = (ViewGroup) linear_ishow2.getChildAt(i);
            for (int a = 0; a < v.getChildCount(); a++) {
                View v1 = v.getChildAt(a);
                v1.setEnabled(false);
            }
        }
        for (int i = 0; i < linear_ishow3.getChildCount(); i++) {
            ViewGroup v = (ViewGroup) linear_ishow3.getChildAt(i);
            for (int a = 0; a < v.getChildCount(); a++) {
                View v1 = v.getChildAt(a);
                v1.setEnabled(false);
            }
        }
        for (int i = 0; i < linear_ishow4.getChildCount(); i++) {
            ViewGroup v = (ViewGroup) linear_ishow4.getChildAt(i);
            for (int a = 0; a < v.getChildCount(); a++) {
                View v1 = v.getChildAt(a);
                v1.setEnabled(false);
            }
        }
        //设置选项不可点击
        disableRadioGroup(business_scope);
        disableRadioGroup(business_scope1);
        disableRadioGroup(business_scope2);
        disableRadioGroup(business_scope3);

        //创建一个AnimationSet对象，参数为Boolean型，
        //true表示使用Animation的interpolator，false则是使用自己的
        animationSet = new AnimationSet(true);
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //设置动画执行的时间
        alphaAnimation.setDuration(500);
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
        get_attestation_data();


    }

    @Override
    protected void initListener() {
        contact_address.setOnClickListener(this);//地址选择
        set_up_time.setOnClickListener(this);//日期选择
        company_data.setOnClickListener(this);
        company_data1.setOnClickListener(this);
        company_data2.setOnClickListener(this);
        company_data3.setOnClickListener(this);
        company_data4.setOnClickListener(this);
        company_data5.setOnClickListener(this);
        business_license_img.setOnClickListener(this);
        legal_number.setOnClickListener(this);
        legal_number_back.setOnClickListener(this);
        docking_person.setOnClickListener(this);
        docking_person_back.setOnClickListener(this);
        id_person.setOnClickListener(this);
        lawyer_certificate.setOnClickListener(this);
        Photograph.setOnClickListener(this);
        chose_picture.setOnClickListener(this);
        sure.setOnClickListener(this);
        back.setOnClickListener(this);
        title_image.setOnClickListener(this);
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期

        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_data_attestation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//修改
            case R.id.title_image:
                ismodify = true;
                enableRadioGroup(business_scope);
                enableRadioGroup(business_scope1);
                enableRadioGroup(business_scope2);
                enableRadioGroup(business_scope3);
                sure.setVisibility(View.VISIBLE);
//                business_license_img.setEnabled(true);
//                legal_number.setEnabled(true);
//                legal_number_back.setEnabled(true);
//                docking_person.setEnabled(true);
//                docking_person_back.setEnabled(true);
//                id_person.setEnabled(true);
//                lawyer_certificate.setEnabled(true);
                has_channel_of_asset_disposal.setEnabled(true);
                for (int i = 0; i < linear_ishow.getChildCount(); i++) {
                    ViewGroup v_a = (ViewGroup) linear_ishow.getChildAt(i);
                    for (int a = 0; a < v_a.getChildCount(); a++) {
                        View v1 = v_a.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow1.getChildCount(); i++) {
                    ViewGroup v_1 = (ViewGroup) linear_ishow1.getChildAt(i);
                    for (int a = 0; a < v_1.getChildCount(); a++) {
                        View v1 = v_1.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow2.getChildCount(); i++) {
                    ViewGroup v_2 = (ViewGroup) linear_ishow2.getChildAt(i);
                    for (int a = 0; a < v_2.getChildCount(); a++) {
                        View v1 = v_2.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow3.getChildCount(); i++) {
                    ViewGroup v_3 = (ViewGroup) linear_ishow3.getChildAt(i);
                    for (int a = 0; a < v_3.getChildCount(); a++) {
                        View v1 = v_3.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow4.getChildCount(); i++) {
                    ViewGroup v_4 = (ViewGroup) linear_ishow4.getChildAt(i);
                    for (int a = 0; a < v_4.getChildCount(); a++) {
                        View v1 = v_4.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.company_data:
                if (linear_ishow.getVisibility() == View.GONE) {
                    linear_ishow.setVisibility(View.VISIBLE);

                    //使用ImageView的startAnimation方法执行动画

                    linear_ishow.startAnimation(animationSet);
                    arrows.setPivotX(arrows.getWidth() / 2);
                    arrows.setPivotY(arrows.getHeight() / 2);//支点在图片中心
                    arrows.setRotation(90);
                } else {

                    linear_ishow.setVisibility(View.GONE);
                    arrows.setPivotX(arrows.getWidth() / 2);
                    arrows.setPivotY(arrows.getHeight() / 2);//支点在图片中心
                    arrows.setRotation(360);
                }

                break;

            case R.id.company_data1:
                if (linear_ishow1.getVisibility() == View.GONE) {
                    linear_ishow1.setVisibility(View.VISIBLE);
                    //使用ImageView的startAnimation方法执行动画
                    linear_ishow1.startAnimation(animationSet);
                    arrows1.setPivotX(arrows1.getWidth() / 2);
                    arrows1.setPivotY(arrows1.getHeight() / 2);//支点在图片中心
                    arrows1.setRotation(90);
                } else {

                    linear_ishow1.setVisibility(View.GONE);
                    arrows1.setPivotX(arrows1.getWidth() / 2);
                    arrows1.setPivotY(arrows1.getHeight() / 2);//支点在图片中心
                    arrows1.setRotation(360);
                }

                break;
            case R.id.company_data2:
                if (linear_ishow2.getVisibility() == View.GONE) {
                    linear_ishow2.setVisibility(View.VISIBLE);
                    //使用ImageView的startAnimation方法执行动画
                    linear_ishow2.startAnimation(animationSet);
                    arrows2.setPivotX(arrows2.getWidth() / 2);
                    arrows2.setPivotY(arrows2.getHeight() / 2);//支点在图片中心
                    arrows2.setRotation(90);
                } else {

                    linear_ishow2.setVisibility(View.GONE);
                    arrows2.setPivotX(arrows2.getWidth() / 2);
                    arrows2.setPivotY(arrows2.getHeight() / 2);//支点在图片中心
                    arrows2.setRotation(360);
                }

                break;
            case R.id.company_data3:
                if (linear_ishow3.getVisibility() == View.GONE) {
                    linear_ishow3.setVisibility(View.VISIBLE);
                    //使用ImageView的startAnimation方法执行动画
                    linear_ishow3.startAnimation(animationSet);
                    arrows3.setPivotX(arrows3.getWidth() / 2);
                    arrows3.setPivotY(arrows3.getHeight() / 2);//支点在图片中心
                    arrows3.setRotation(90);
                } else {

                    linear_ishow3.setVisibility(View.GONE);
                    arrows3.setPivotX(arrows3.getWidth() / 2);
                    arrows3.setPivotY(arrows3.getHeight() / 2);//支点在图片中心
                    arrows3.setRotation(360);
                }

                break;
            case R.id.company_data4:
                if (linear_ishow4.getVisibility() == View.GONE) {
                    linear_ishow4.setVisibility(View.VISIBLE);
                    //使用ImageView的startAnimation方法执行动画
                    linear_ishow4.startAnimation(animationSet);
                    arrows4.setPivotX(arrows4.getWidth() / 2);
                    arrows4.setPivotY(arrows4.getHeight() / 2);//支点在图片中心
                    arrows4.setRotation(90);
                } else {

                    linear_ishow4.setVisibility(View.GONE);
                    arrows4.setPivotX(arrows4.getWidth() / 2);
                    arrows4.setPivotY(arrows4.getHeight() / 2);//支点在图片中心
                    arrows4.setRotation(360);
                }

                break;

            case R.id.company_data5:
                if (linear_ishow5.getVisibility() == View.GONE) {
                    linear_ishow5.setVisibility(View.VISIBLE);
                    //使用ImageView的startAnimation方法执行动画
                    linear_ishow5.startAnimation(animationSet);
                    arrows5.setPivotX(arrows5.getWidth() / 2);
                    arrows5.setPivotY(arrows5.getHeight() / 2);//支点在图片中心
                    arrows5.setRotation(90);
                } else {

                    linear_ishow5.setVisibility(View.GONE);
                    arrows5.setPivotX(arrows5.getWidth() / 2);
                    arrows5.setPivotY(arrows5.getHeight() / 2);//支点在图片中心
                    arrows5.setRotation(360);
                }

                break;
            case R.id.business_license_img:
                //工商营业执照
                if (ismodify == false) {
                    if (!Imalist.get(0).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(0));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {

                    id = 1;
                    showpop();
                }


                break;
            case R.id.legal_number:
                //法人身份证正面
                if (ismodify == false) {
//
                    if (!Imalist.get(1).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(1));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {
                    id = 2;
                    showpop();
                }


                break;
            case R.id.legal_number_back:
                //法人身份证反面
                if (ismodify == false) {
                    if (!Imalist.get(2).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(2));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {
                    id = 3;
                    showpop();
                }


                break;
            case R.id.docking_person:
                //业务对接人身份证正面
                if (ismodify == false) {
                    if (!Imalist.get(3).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(3));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {
                    id = 4;
                    showpop();
                }


                break;
            case R.id.docking_person_back:
                //业务对接人身份证反面
                if (ismodify == false) {
                    if (!Imalist.get(4).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(4));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {
                    id = 5;
                    showpop();
                }


                break;
            case R.id.id_person:
                //业务对接人身份证手持
                if (ismodify == false) {
                    if (!Imalist.get(5).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(5));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {
                    id = 6;
                    showpop();
                }


                break;
            case R.id.lawyer_certificate:
                //律师资格证
                if (ismodify == false) {
                    if (!Imalist.get(6).equals("")) {
                        Intent intent = new Intent(this, PictureDetails.class);
                        intent.putExtra("path", Imalist.get(6));
                        intent.putExtra("id", 1);
                        startActivity(intent);
                    }
                } else {
                    id = 7;
                    showpop();
                }


                break;

            case R.id.Photograph:
                //相机
                File mfile = null;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {

                    mfile = new File(paths);
                    if (!mfile.exists()) {
                        try {
                            mfile.mkdir();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //如果没有授权，则请求授权
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        //有授权，直接开启摄像头
                        callCamera();
                    }

                }

                break;

            case R.id.chose_picture:
                //相册
                File picturefile = null;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                } else {

                    picturefile = new File(paths);
                    if (!picturefile.exists()) {
                        try {
                            picturefile.mkdir();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    popupWindow.dismiss();
                    Intent intent1 = new Intent(Intent.ACTION_PICK, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "/" + "just.jpg")));
                    intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent1, 2);
                }

                break;
            case R.id.sure:
                //提交
                ismodify = false;
                patch_data();
                disableRadioGroup(business_scope);
                disableRadioGroup(business_scope1);
                disableRadioGroup(business_scope2);
                disableRadioGroup(business_scope3);
                has_channel_of_asset_disposal.setEnabled(false);
//                business_license_img.setEnabled(false);
//                legal_number.setEnabled(false);
//                legal_number_back.setEnabled(false);
//                docking_person.setEnabled(false);
//                docking_person_back.setEnabled(false);
//                id_person.setEnabled(false);
//                lawyer_certificate.setEnabled(false);
                for (int i = 0; i < linear_ishow.getChildCount(); i++) {
                    ViewGroup mv = (ViewGroup) linear_ishow.getChildAt(i);
                    for (int a = 0; a < mv.getChildCount(); a++) {
                        View v1 = mv.getChildAt(a);
                        v1.setEnabled(false);
                    }
                }
                for (int i = 0; i < linear_ishow1.getChildCount(); i++) {
                    ViewGroup mv = (ViewGroup) linear_ishow1.getChildAt(i);
                    for (int a = 0; a < mv.getChildCount(); a++) {
                        View v1 = mv.getChildAt(a);
                        v1.setEnabled(false);
                    }
                }
                for (int i = 0; i < linear_ishow2.getChildCount(); i++) {
                    ViewGroup mv = (ViewGroup) linear_ishow2.getChildAt(i);
                    for (int a = 0; a < mv.getChildCount(); a++) {
                        View v1 = mv.getChildAt(a);
                        v1.setEnabled(false);
                    }
                }
                for (int i = 0; i < linear_ishow3.getChildCount(); i++) {
                    ViewGroup mv = (ViewGroup) linear_ishow3.getChildAt(i);
                    for (int a = 0; a < mv.getChildCount(); a++) {
                        View v1 = mv.getChildAt(a);
                        v1.setEnabled(false);
                    }
                }
                for (int i = 0; i < linear_ishow4.getChildCount(); i++) {
                    ViewGroup mv = (ViewGroup) linear_ishow4.getChildAt(i);
                    for (int a = 0; a < mv.getChildCount(); a++) {
                        View v1 = mv.getChildAt(a);
                        v1.setEnabled(false);
                    }
                }

                break;

            case R.id.set_up_time:
                //选择注册时间
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        set_up_time.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                    }
                }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.contact_address:
                //选择公司地址
                WheelViewDialog wheelViewDialog = new WheelViewDialog(this);
                wheelViewDialog.setDialogStyle(Color.parseColor("#ffff00"));
                View outerView = LayoutInflater.from(this).inflate(R.layout.layout_whleeview, null);
                chose_text = (TextView) outerView.findViewById(R.id.chose_text);
                chose_text.setText("请选择省");
                wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
                wv.setSkin(WheelView.Skin.Holo); // common皮肤
                wv.setWheelClickable(true);
                wv.setLoop(false);
                wv.setWheelData(list);
                // 数据集合
                wv.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object o) {
                        citylist.clear();
                        adress = list.get(position).toString();
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
                        WheelViewDialog wheelViewDialog_city = new WheelViewDialog(getApplicationContext());
                        wheelViewDialog_city.setDialogStyle(Color.parseColor("#ffff00"));
                        View outerView_city = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_whleeview, null);
                        chose_city = (TextView) outerView_city.findViewById(R.id.chose_text);
                        chose_city.setText("请选择市");
                        wv = (WheelView) outerView_city.findViewById(R.id.wheel_view_wv);
                        wv.setWheelAdapter(new ArrayWheelAdapter(getApplicationContext())); // 文本数据源
                        wv.setSkin(WheelView.Skin.Holo); // common皮肤
                        wv.setWheelClickable(true);
//        wv.setWheelSize(5);
                        wv.setLoop(false);
                        if (citylist.size() != 0) {
                            wv.setWheelData(citylist);
                        } else {

                            Toast.makeText(getApplicationContext(), "城市列表为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 数据集合
                        wv.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
                            @Override
                            public void onItemClick(int position, Object o) {
                                contact_address.setText(adress + citylist.get(position));

                                mdialog.dismiss();


                            }
                        });
                        wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                            @Override
                            public void onItemSelected(int position, Object o) {

                            }

                        });

                        mdialog = new AlertDialog.Builder(DataAttestationActivity.this)
                                .setView(outerView_city)
                                .show();

                    }
                });
                wv.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, Object o) {


                    }

                });

                mdialog = new AlertDialog.Builder(this)
                        .setView(outerView)
                        .show();
                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_authentication, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.modify_data:
                sure.setVisibility(View.VISIBLE);
                for (int i = 0; i < linear_ishow.getChildCount(); i++) {
                    ViewGroup v = (ViewGroup) linear_ishow.getChildAt(i);
                    for (int a = 0; a < v.getChildCount(); a++) {
                        View v1 = v.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow1.getChildCount(); i++) {
                    ViewGroup v = (ViewGroup) linear_ishow1.getChildAt(i);
                    for (int a = 0; a < v.getChildCount(); a++) {
                        View v1 = v.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow2.getChildCount(); i++) {
                    ViewGroup v = (ViewGroup) linear_ishow2.getChildAt(i);
                    for (int a = 0; a < v.getChildCount(); a++) {
                        View v1 = v.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow3.getChildCount(); i++) {
                    ViewGroup v = (ViewGroup) linear_ishow3.getChildAt(i);
                    for (int a = 0; a < v.getChildCount(); a++) {
                        View v1 = v.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                for (int i = 0; i < linear_ishow4.getChildCount(); i++) {
                    ViewGroup v = (ViewGroup) linear_ishow4.getChildAt(i);
                    for (int a = 0; a < v.getChildCount(); a++) {
                        View v1 = v.getChildAt(a);
                        v1.setEnabled(true);
                    }
                }
                break;

        }
        return super.onOptionsItemSelected(item);


    }

    void get_attestation_data() {
        dialog.show();
        RequestParams params = new RequestParams(Api.Me.Me);
        HttpUtils.Gethttp(params, new IOCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List result) {

            }

            @Override
            public void onSuccess(Object result) throws UnsupportedEncodingException {
                Log.e("result1111", result.toString());
                try {
                    dialog.dismiss();
                    JSONObject mjs = new JSONObject(result.toString());
//                    Imalist.clear();
//                    JSONObject profile_json = new JSONObject(mjs.getString("profile"));
                    Gson gson = new Gson();
                    gson = new GsonBuilder()
                            .setLenient()// json宽松
                            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                            .serializeNulls() //智能null
                            .setPrettyPrinting()// 调教格式
                            .disableHtmlEscaping() //默认是GSON把HTML 转义的
                            .create();
                    String str = mjs.toString();
                    AttestationDataBean bean = gson.fromJson(str, AttestationDataBean.class);
                    Log.e("实体", bean.toString());
                    business_name.setText(bean.getBusiness_name());
                    set_up_time.setText(bean.getSet_up_time());
                    registered_capital.setText(bean.getRegistered_capital());
                    credit_cooperative_number.setText(bean.getCredit_cooperative_number());
                    bank_license_number.setText(bean.getBank_license_number());
                    contact_address.setText(bean.getContact_address());
                    contact_email.setText(bean.getMail());
//                    collect_type.setText(bean.getCollect_type());
//                    Toast.makeText(getApplicationContext(),bean.getCollect_type().toString(),Toast.LENGTH_SHORT).show();
                    if (bean.getCollect_type() != null) {
                        if (bean.getCollect_type().equals("车贷,房贷")) {
                            car_loan.setChecked(true);
                            house_loan.setChecked(true);
                        }
                        if (bean.getCollect_type().equals("车贷")) {
                            car_loan.setChecked(true);
                        } else {

                            house_loan.setChecked(true);
                        }
                    }
                    //业务覆盖范围
                    if (bean.getCover_area() != null) {
                        if (bean.getCover_area().equals("省")) {

                            provincial_level.setChecked(true);
                        }
                        if (bean.getCover_area().equals("市")) {
                            city_level.setChecked(true);

                        }
                        if (bean.getCover_area().equals("地")) {
                            land_level.setChecked(true);

                        }
                    }
                    collection_area.setText(bean.getCollection_area().toString());
                    phone_collection_count.setText(String.valueOf(bean.getPhone_collection_count()));
                    visit_collection_count.setText(String.valueOf(bean.getVisit_collection_count()));
                    //有无债务人员信息补全渠道
                    if (bean.isHas_channel_of_complete_information() == true) {
                        is_have_1.setChecked(true);

                    } else {
                        no_have_1.setChecked(true);

                    }
//                    has_channel_of_complete_information.setText(String.valueOf(bean.isHas_channel_of_complete_information()));
//有无合作律师或事务所
                    if (bean.isHas_cooperation_lawyer() == true) {
                        is_have_2.setChecked(true);

                    } else {
                        no_have_2.setChecked(true);
                    }
//                    has_cooperation_lawyer.setText(String.valueOf(bean.isHas_cooperation_lawyer()));
//有无不良资产处理渠道
                    if (bean.isHas_channel_of_asset_disposal() == true) {
                        is_have.setChecked(true);

                    } else {

                        no_have.setChecked(true);
                    }
                    has_channel_of_asset_disposal.setText(bean.getPast_cooperation_company());
                    legal_name.setText(bean.getLegal_name());
                    legal_phone.setText(bean.getLegal_phone());
                    legal_id_card.setText(bean.getLegal_id_card());
                    charge_name.setText(bean.getCharge_name());
                    charge_phone.setText(bean.getCharge_phone());
                    charge_id_card.setText(bean.getCharge_id_card());
                    contact_person_name.setText(bean.getContact_person_name());
                    contact_person_phone.setText(bean.getContact_person_phone());
                    contact_person_id_card.setText(bean.getContact_person_id_card());
                    public_bank_number.setText(bean.getPublic_bank_number());
                    public_bank_name.setText(bean.getPublic_bank_name());
                    public_bank_person_name.setText(bean.getPublic_bank_person_name());
                    private_bank_number.setText(bean.getPrivate_bank_number());
                    private_bank_name.setText(bean.getPrivate_bank_name());
                    private_bank_person_name.setText(bean.getPrivate_bank_person_name());
                    if (bean.getBusiness_license_img() != null) {
                        Imalist.set(0, Api.pic + bean.getBusiness_license_img().toString());
//                        Imalist.get(0)=Api.pic+bean.getBusiness_license_img().toString();
                        Load_image.Setimagem(Api.pic + bean.getBusiness_license_img(), business_license_img);


                    }
                    if (bean.getLegal_front() != null) {
                        Imalist.set(1, Api.pic + bean.getLegal_front().toString());
//                        Imalist.add(Api.pic+bean.getLegal_front().toString());
                        Load_image.Setimagem(Api.pic + bean.getLegal_front(), legal_number);

                    }
                    if (bean.getLegal_back() != null) {
                        Imalist.set(2, Api.pic + bean.getLegal_back().toString());
//                        Imalist.add(Api.pic+bean.getLegal_back().toString());
                        Load_image.Setimagem(Api.pic + bean.getLegal_back(), legal_number_back);

                    }

                    if (bean.getCharge_front() != null) {
                        Imalist.set(3, Api.pic + bean.getCharge_front().toString());
//                        Imalist.add(Api.pic+bean.getCharge_front().toString());
                        Load_image.Setimagem(Api.pic + bean.getCharge_front(), docking_person);
                    }
                    if (bean.getCharge_back() != null) {
                        Imalist.set(4, Api.pic + bean.getCharge_back().toString());
//                        Imalist.add(Api.pic+bean.getCharge_back().toString());
                        Load_image.Setimagem(Api.pic + bean.getCharge_back(), docking_person_back);

                    }

                    if (bean.getCharge_hand() != null) {
                        Imalist.set(5, Api.pic + bean.getCharge_hand().toString());
//                        Imalist.add(Api.pic+bean.getCharge_hand().toString());
                        Load_image.Setimagem(Api.pic + bean.getCharge_hand(), id_person);
                    }
                    if (bean.getLawyer_certificate() != null) {
                        Imalist.set(6, Api.pic + bean.getLawyer_certificate().toString());
//                        Imalist.add(Api.pic+bean.getLawyer_certificate() .toString());
                        Load_image.Setimagem(Api.pic + bean.getLawyer_certificate(), lawyer_certificate);
                    }


                    Log.e(">>>>>>", String.valueOf(bean.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {
                Log.e(">>>>>>>>>>>>", "有问题");
            }
        });


    }


    public void cropPhoto(Uri uri) {
//        Intent intent=new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 100);
//        intent.putExtra("outputY", 70);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    void showpop() {
        popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置动画效果
        popupWindow.showAtLocation(data_linear, Gravity.BOTTOM, 0, 0);
        popview.getBackground().setAlpha(140);
        popview.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/just.jpg");

                    dialog.setLoadingText("正在压缩,请稍后!");
                    if (id == 1) {
                        DataAttestationTask task = new DataAttestationTask(temp, this, business_license_img, dialog, id, file, paths);

                        dialog.show();
                        task.execute();
                    } else if (id == 2) {
                        DataAttestationTask task = new DataAttestationTask(temp, this, legal_number, dialog, id, file1, paths);
                        dialog.show();
                        task.execute();


                    } else if (id == 3) {
                        DataAttestationTask task = new DataAttestationTask(temp, this, legal_number_back, dialog, id, file2, paths);
                        dialog.show();
                        task.execute();


                    } else if (id == 4) {
                        DataAttestationTask task = new DataAttestationTask(temp, this, docking_person, dialog, id, file3, paths);
                        dialog.show();
                        task.execute();

//
                    } else if (id == 5) {
                        DataAttestationTask task = new DataAttestationTask(temp, this, docking_person_back, dialog, id, file4, paths);
                        dialog.show();
                        task.execute();
                    } else if (id == 6) {
                        DataAttestationTask task = new DataAttestationTask(temp, this, id_person, dialog, id, file5, paths);
                        dialog.show();
                        task.execute();

                    } else {
                        DataAttestationTask task = new DataAttestationTask(temp, this, lawyer_certificate, dialog, id, file6, paths);
                        dialog.show();
                        task.execute();
                    }
////                    popupWindow.dismiss();//关闭弹出框
//

                }
                break;
            case 3:
                if (data != null) {
                    mintent = data;
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */


                        if (id == 1) {
                            setPicToView(head, "just.jpg");//保存在SD卡中
                            file = new File(paths + "just.jpg");
                            business_license_img.setImageBitmap(head);//用ImageView显示出来
                        } else if (id == 2) {
                            setPicToView(head, "just1.jpg");//保存在SD卡中
                            file1 = new File(paths + "just1.jpg");
                            legal_number.setImageBitmap(head);//用ImageView显示出来


                        } else if (id == 3) {
                            setPicToView(head, "just2.jpg");//保存在SD卡中
                            file2 = new File(paths + "just2.jpg");
                            legal_number_back.setImageBitmap(head);//用ImageView显示出来


                        } else if (id == 4) {
                            setPicToView(head, "just3.jpg");//保存在SD卡中
                            file3 = new File(paths + "just3.jpg");
                            docking_person.setImageBitmap(head);//用ImageView显示出来
//
                        } else if (id == 5) {

                            setPicToView(head, "just4.jpg");//保存在SD卡中
                            file4 = new File(paths + "just4.jpg");
                            docking_person_back.setImageBitmap(head);//用ImageView显示出来
                        } else if (id == 6) {

                            setPicToView(head, "just5.jpg");//保存在SD卡中
                            file5 = new File(paths + "just5.jpg");
                            id_person.setImageBitmap(head);//用ImageView显示出来

                        } else {

                            setPicToView(head, "just6.jpg");//保存在SD卡中
                            file6 = new File(paths + "just6.jpg");
                            lawyer_certificate.setImageBitmap(head);//用ImageView显示出来
                        }
//                        popupWindow.dismiss();//关闭弹出框
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Bitmap mBitmap, String str) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(paths);
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        String fileName = paths + str;//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (file.exists()) {

            file.delete();
        }
        if (file1.exists()) {

            file1.delete();
        }
        if (file2.exists()) {

            file2.delete();
        }
        if (file3.exists()) {

            file3.delete();
        }
        if (file4.exists()) {

            file4.delete();
        }
        if (file5.exists()) {

            file5.delete();
        }
        if (file6.exists()) {

            file6.delete();
        }

    }

    /****
     * 上传认证资料
     */
    void patch_data() {
        dialog.setLoadingText("正在上传!");
        dialog.show();
        bean = new AttestationDataBean();
        RequestParams params = new RequestParams(Api.my_profile.my_profile);
        params.addBodyParameter("business_name", business_name.getText().toString());
        params.addBodyParameter("credit_cooperative_number", credit_cooperative_number.getText().toString());
        params.addBodyParameter("bank_license_number", bank_license_number.getText().toString());
        params.addBodyParameter("set_up_time", set_up_time.getText().toString());
        params.addBodyParameter("private_bank_number", private_bank_number.getText().toString());
        params.addBodyParameter("legal_name", legal_name.getText().toString());

//        bean.setCollect_type(collect_type.getText().toString());
        params.addBodyParameter("legal_id_card", legal_id_card.getText().toString());
        params.addBodyParameter("legal_phone", legal_phone.getText().toString());
        params.addBodyParameter("charge_name", charge_name.getText().toString());
        params.addBodyParameter("registered_capital", registered_capital.getText().toString());
        params.addBodyParameter("charge_id_card", charge_id_card.getText().toString());
        params.addBodyParameter("charge_phone", charge_phone.getText().toString());
        params.addBodyParameter("contact_person_name", contact_person_name.getText().toString());
        params.addBodyParameter("contact_person_phone", contact_person_phone.getText().toString());
        params.addBodyParameter("contact_person_id_card", contact_person_id_card.getText().toString());
        params.addBodyParameter("contact_address", contact_address.getText().toString());

        params.addBodyParameter("mail", contact_email.getText().toString());

        params.addBodyParameter("private_bank_name", private_bank_name.getText().toString());

        params.addBodyParameter("private_bank_person_name", private_bank_person_name.getText().toString());
        params.addBodyParameter("collection_area", collection_area.getText().toString());

        if (provincial_level.isChecked() == true) {
            params.addBodyParameter("cover_area", "省");

        }
        if (city_level.isChecked() == true) {
            params.addBodyParameter("cover_area", "市级");

        }
        if (land_level.isChecked() == true) {
            params.addBodyParameter("cover_area", "地级");
        }
        if (!phone_collection_count.getText().toString().equals("")) {
            params.addBodyParameter("phone_collection_count", phone_collection_count.getText().toString());

        }

        if (!visit_collection_count.getText().toString().equals("")) {
            params.addBodyParameter("visit_collection_count", visit_collection_count.getText().toString());

        }
        if (car_loan.isChecked() == true) {
            params.addBodyParameter("collect_type", "车贷");

        }
        if (house_loan.isChecked() == true) {
            params.addBodyParameter("collect_type", "房贷");

        }
        if (car_loan.isChecked() == true && house_loan.isChecked() == true) {

            params.addBodyParameter("collect_type", "车贷,房贷");
        }
//                bean.setMail();

        params.addBodyParameter("public_bank_name", public_bank_name.getText().toString());
        params.addBodyParameter("public_bank_number", public_bank_number.getText().toString());
        params.addBodyParameter("public_bank_person_name", public_bank_person_name.getText().toString());
        params.addBodyParameter("past_cooperation_company", has_channel_of_asset_disposal.getText().toString());
        if (is_have_1.isChecked() == true) {
            params.addParameter("has_channel_of_complete_information", true);
            bean.setHas_channel_of_complete_information(true);
        }
        if (no_have_1.isChecked() == true) {
            params.addParameter("has_channel_of_complete_information", false);

        }


//        bean.setHas_channel_of_complete_information();
        if (is_have.isChecked() == true) {
            params.addParameter("has_channel_of_asset_disposal", true);

        }
        if (no_have.isChecked() == true) {
            params.addParameter("has_channel_of_asset_disposal", false);
        }

        //bean.setHas_channel_of_asset_disposal(has_channel_of_complete_information);

        if (is_have_2.isChecked() == true) {
            params.addParameter("has_cooperation_lawyer", true);
        }
        if (no_have_2.isChecked() == true) {
            params.addParameter("has_cooperation_lawyer", false);

        }

        if (file.exists()) {
            params.addBodyParameter("business_license_img", file);

        }
        if (file1.exists()) {
            params.addBodyParameter("legal_front", file1);

        }
        if (file2.exists()) {
            params.addBodyParameter("legal_back", file2);

        }
        if (file3.exists()) {
            params.addBodyParameter("charge_front", file3);

        }
        if (file4.exists()) {
            params.addBodyParameter("charge_back", file4);

        }
        if (file5.exists()) {
            params.addBodyParameter("charge_hand", file5);

        }
        if (file6.exists()) {
            params.addBodyParameter("lawyer_certificate", file6);

        }
        params.setMultipart(true);
        params.setConnectTimeout(5000);
        params.addHeader("Authorization", "Token" + " " + Constants.token);
        x.http().request(HttpMethod.PATCH, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                sure.setVisibility(View.GONE);
                Log.e("result", result.toString());
                Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
//                get_attestation_data();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("ex", String.valueOf(ex));
                Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    //省市选择解析
    void getProvincesandcities() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                key_list = new ArrayList<String>();
                String json = AppJsonFileReader.getJson(getApplicationContext(), "area.json");
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

    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    public void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }
}
