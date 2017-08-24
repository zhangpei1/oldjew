package com.new_jew.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.bean.AttestationDataBean;
import com.new_jew.customview.RoundImageView;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.net.HttpBean;
import com.new_jew.net.Load_image;
import com.new_jew.toolkit.GetMebean;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangpei on 2016/11/14.
 */
public class BasicsActivity extends BaseActivity implements View.OnClickListener, GetMebean {
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.imageiv)
    private RoundImageView imageiv;
    @ViewInject(R.id.change_header)
    private RelativeLayout change_header, change_password;

    private PopupWindow popupWindow;
    private View popview;
    private LinearLayout pop_linear;
    private Button Photograph, chose_picture;
    private String paths = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jew/basics/";
    private Intent mintent = null;
    private Bitmap head = null;
    private File file;
    private HttpBean bean;
    private TextView business_name, principal_man;
    private Uri imageUri = Uri.parse(paths+"/head.jpg");
//    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    protected void initLayout() {
        x.view().inject(this);
        Log.e("path", paths);
//        paths=Environment.getExternalStorageDirectory().getAbsolutePath()+"/jew/basics/";
        title_text.setText("基本资料");
        popview = getLayoutInflater().inflate(R.layout.layout_popwindow, null, false);
        pop_linear = (LinearLayout) this.findViewById(R.id.pop_linear);
        Photograph = (Button) popview.findViewById(R.id.Photograph);
        chose_picture = (Button) popview.findViewById(R.id.chose_picture);
        business_name = (TextView) this.findViewById(R.id.business_name);
        principal_man = (TextView) this.findViewById(R.id.principal_man);
        change_password = (RelativeLayout) this.findViewById(R.id.change_password);

    }

    @Override
    protected void initValue() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {

            file = new File(paths);
            if (!file.exists()) {

                file.mkdir();
            }
        }

        bean = new HttpBean();
        bean.get_attestation_data();

    }

    @Override
    protected void initListener() {
        bean.setGetMebean(this);
        back.setOnClickListener(this);
        change_header.setOnClickListener(this);
        Photograph.setOnClickListener(this);
        chose_picture.setOnClickListener(this);
        change_password.setOnClickListener(this);
    }

    @Override
    protected int setRootView() {
        return R.layout.layout_basics;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.change_header:
                showpop();
                break;
            case R.id.Photograph:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {

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
                }


                break;
            case R.id.chose_picture:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                } else {

                    file = new File(paths);
                    if (!file.exists()) {

                        file.mkdir();
                    }
                    Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                    intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent1, 1);

//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//                    intent.setType("image/*");
//                    intent.putExtra("crop", "true");
//                    intent.putExtra("aspectX", 2);
//                    intent.putExtra("aspectY", 1);
//                    intent.putExtra("outputX", 600);
//                    intent.putExtra("outputY", 300);
//                    intent.putExtra("scale", true);
//                    intent.putExtra("return-data", false);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                    intent.putExtra("noFaceDetection", true); // no face detection
//                    startActivityForResult(intent, 6);
//                    Toast.makeText(getApplicationContext(), "相册", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.change_password:
                Intent intent = new Intent(this, Retrieve_password.class);
                intent.putExtra("id", 0);
                startActivity(intent);
                break;

        }

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
                file = new File(paths);
                if (!file.exists()) {

                    file.mkdir();
                }
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
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启摄像头
                file = new File(paths);
                if (!file.exists()) {

                    file.mkdir();
                }
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
//                Toast.makeText(getApplicationContext(), "相册", Toast.LENGTH_SHORT).show();
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void callCamera() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "just.jpg")));
        startActivityForResult(intent2, 2);//采用ForResult打开
//        Toast.makeText(getApplicationContext(), "相机", Toast.LENGTH_SHORT).show();

    }

    void showpop() {


        popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置动画效果
        popupWindow.showAtLocation(pop_linear, Gravity.BOTTOM, 0, 0);
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
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
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

                        setPicToView(head, "just.jpg");//保存在SD卡中
                        file = new File(paths + "just.jpg");
                        Path_head(new File(paths + "just.jpg"));
//                            business_license_img.setImageBitmap(head);//用ImageView显示出来


                        popupWindow.dismiss();//关闭弹出框
                    }
                }
                break;

            case 6:
                if (imageUri!=null){

                    Log.d("CHOOSE_BIG_PICTURE", "CHOOSE_BIG_PICTURE: data = " + data);//it seems to be null

                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ;
                    imageiv.setImageBitmap(bitmap);
                }else {

                    Toast.makeText(this,"空",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "SD不可用", Toast.LENGTH_SHORT).show();
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

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, 3);

//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 11);
//        intent.putExtra("aspectY", 16);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, 3);
    }

    //修改头像
    void Path_head(File file) {

        RequestParams params = new RequestParams(Api.my_profile.my_profile);
        params.setMultipart(true);
        params.setConnectTimeout(5000);
        params.addHeader("Authorization", "Token" + " " + Constants.token);
        params.addHeader("x-http-method-override", "PATCH");
        params.addParameter("thumbnail", file);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                dialog.dismiss();
                Log.e("result", "成功");
                Toast.makeText(getApplicationContext(), "设置成功！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "设置失败！", Toast.LENGTH_SHORT).show();
                Log.e("ex", String.valueOf(ex));
            }


            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                Toast.makeText(getApplicationContext(),"设置成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getresult(AttestationDataBean bean) {
        Log.e("result", bean.toString());
        Load_image.Setimage(Api.pic + bean.getThumbnail(), imageiv);
        if (Constants.type.equals("3")) {

            business_name.setText(bean.getContact_person_name());
        } else {
            business_name.setText(bean.getFullname().toString());
        }
        principal_man.setText(String.valueOf(bean.getTelephone()));


    }
}
