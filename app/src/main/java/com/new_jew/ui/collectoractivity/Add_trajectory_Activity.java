package com.new_jew.ui.collectoractivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new_jew.BaseActivity;
import com.new_jew.R;
import com.new_jew.bean.Add_picture_Adpter;
import com.new_jew.global.Api;
import com.new_jew.global.Constants;
import com.new_jew.global.IOCallback;
import com.new_jew.global.Task;
import com.new_jew.net.HttpUtils;
import com.new_jew.net.Location_service;
import com.new_jew.toolkit.FileUtils;
import com.new_jew.ui.activity.PictureDetails;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by zhangpei on 2016/7/19.
 */
public class Add_trajectory_Activity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    @ViewInject(R.id.back)
    private LinearLayout back;
    @ViewInject(R.id.title_text)
    private TextView title_text;
    @ViewInject(R.id.title_image)
    private TextView title_image;
    private Location_service location_service;
    private ImageButton takePhotoImageButton;
    private ImageButton voiceRecordImageButton;
    private static String picture_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jew/myphone";//sd路径
    private GridView attchmentList;
    private Task task;
    private ArrayList<Add_picture_Adpter> mlist = new ArrayList<>();
    private Add_picture add_picture;
    private JCVideoPlayerStandard video;
    MediaController mediaco;

    private String strRecorderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jew/video/";// 录视频文件的绝对路径
    private EditText newProgressEdit;
    private PopupWindow popupWindow;
    private View popview;
    private RelativeLayout pop_linear;
    private Button Photograph, chose_picture;
    private File file_video;
    private boolean isShowDelete = false;
    private ArrayList<File> flist = new ArrayList<>();
    private File mfile;

    @Override
    protected void initLayout() {
        x.view().inject(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            location_service = new Location_service(this);
            Location_service.getaddress();
        }

        takePhotoImageButton = (ImageButton) this.findViewById(R.id.takePhotoImageButton);//图片
        voiceRecordImageButton = (ImageButton) this.findViewById(R.id.voiceRecordImageButton);//语音
        attchmentList = (GridView) this.findViewById(R.id.attchmentList);
        video = (JCVideoPlayerStandard) this.findViewById(R.id.video);
        mediaco = new MediaController(this);
        newProgressEdit = (EditText) this.findViewById(R.id.newProgressEdit);
        popview = getLayoutInflater().inflate(R.layout.layout_popwindow, null, false);
        Photograph = (Button) popview.findViewById(R.id.Photograph);
        chose_picture = (Button) popview.findViewById(R.id.chose_picture);
        pop_linear = (RelativeLayout) this.findViewById(R.id.pop_rc);

    }

    @Override
    protected void initValue() {
        back.setOnClickListener(this);
        title_text.setText("上传催记");
        title_image.setText("上传");
        add_picture = new Add_picture(getApplication(), mlist, flist);
        attchmentList.setAdapter(add_picture);

    }

    @Override
    protected void initListener() {
        back.setOnClickListener(this);
        Photograph.setOnClickListener(this);
        chose_picture.setOnClickListener(this);
        takePhotoImageButton.setOnClickListener(this);
        voiceRecordImageButton.setOnClickListener(this);
        attchmentList.setOnItemLongClickListener(this);
        video.setOnClickListener(this);
        title_image.setOnClickListener(this);
        attchmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Add_trajectory_Activity.this, PictureDetails.class);
                intent.putExtra("path", flist.get(position).getAbsolutePath());
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_add_trajectory;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePhotoImageButton:
                if (flist.size() > 2) {
                    Toast.makeText(getApplicationContext(), "图片数量以超出限制！", Toast.LENGTH_SHORT).show();
                    return;

                }
                showpop();

                break;

            case R.id.voiceRecordImageButton:
                  //录像
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                } else {

                    File filev = new File(strRecorderPath);
                    if (!filev.exists()) {
                        filev.mkdir();

                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //如果没有授权，则请求授权
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        //有授权，直接开启摄像头
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(intent, 4);
                    }
                }


                break;
            case R.id.chose_picture:
                //相册选择图片
                popupWindow.dismiss();
                File chosefile = null;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                } else {

                    chosefile = new File(picture_path);
                    if (!chosefile.exists()) {
                        chosefile.mkdir();
                    }
                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent1.setType("image/*");
                    startActivityForResult(intent1, 6);
                }

                break;
            case R.id.Photograph:
                //照相机
                popupWindow.dismiss();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    File file = null;
                    popupWindow.dismiss();
                    file = new File(picture_path);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //如果没有授权，则请求授权
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                    } else {
                        //有授权，直接开启摄像头
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picture_path + "photo.png")));
                        startActivityForResult(intent2, 3);//采用ForResult打开
                    }

                }


                break;
            case R.id.video:


                break;
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.title_image:
                //上传催记
                PostHttp(Constants.id);
                break;
        }

    }
//授权回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断请求码
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //有授权，直接定位
                location_service = new Location_service(this);
                Location_service.getaddress();
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //有授权，直接开启摄像头
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picture_path + "photo.png")));
                startActivityForResult(intent2, 3);//采用ForResult打开
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 2) {
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File file = null;
                //成功，开启内存卡权限
                popupWindow.dismiss();
                file = new File(picture_path);
                if (!file.exists()) {
                    file.mkdir();
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    //有授权，直接开启摄像头
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picture_path + "photo.png")));
                    startActivityForResult(intent2, 3);//采用ForResult打开
                }
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，创建文件夹
                File chosefile = null;
                chosefile = new File(picture_path);
                if (!chosefile.exists()) {
                    chosefile.mkdir();
                }
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent1.setType("image/*");
                startActivityForResult(intent1, 6);
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }


        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启视频

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 4);
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }

        if (requestCode == 4) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启视频
                File filev = new File(strRecorderPath);
                if (!filev.exists()) {
                    filev.mkdir();
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //如果没有授权，则请求授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    //有授权，直接开启摄像头
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(intent, 4);
                }
            } else {
                //授权失败
                Toast.makeText(getApplicationContext(), "拒绝此权限，将无法使用此功能", Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != RESULT_OK) {
//            Toast.makeText(getApplicationContext(), "放弃了 ", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case 3:
                //相机
                mfile = new File(picture_path + "photo.png");
                if (!mfile.exists()) {
                    Toast.makeText(getApplicationContext(), "文件不存在请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                task = new Task(getApplicationContext(), flist, mlist, add_picture, mfile, dialog);

                task.execute();
                dialog.show();
                break;

            case 4://视频
                if (resultCode == RESULT_OK) {
                    Uri uriRecorder = data.getData();
//                    Toast.makeText(this, uriRecorder.toString(), Toast.LENGTH_SHORT).show();
                    Cursor cursor = this.getContentResolver().query(uriRecorder, null, null, null, null);
                    if (cursor.moveToNext()) {
                                         /* _data：文件的绝对路径 ，_display_name：文件名 */
                        strRecorderPath = cursor.getString(cursor.getColumnIndex("_data"));
                        file_video = new File(strRecorderPath);

                        if (file_video.length() / 1024 / 1024 <= 10) {

                            video.setVisibility(View.VISIBLE);
                            video.setUp(strRecorderPath
                                    , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, " ");
                            Log.e("strRecorderPath", strRecorderPath);
                        } else {
                            Toast.makeText(getApplicationContext(), "视频太大请重新录制！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case 6:
               //相册
                Uri uri = data.getData();
                if (uri != null) {
                    File file = new File(FileUtils.getRealFilePath(getApplicationContext(), uri));
                    dialog.setLoadingText("正在压缩, 请稍后");
                    dialog.show();
                    task = new Task(getApplicationContext(), flist, mlist, add_picture, file, dialog);

                    task.execute();

                    Bundle bundle = data.getExtras();
                    if (bundle == null) {

//                        Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                    } else {

                        //可以通过获取到的这个Bitmap对象设置到ImageView控件中，让他显示出来
                        Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                        if (photo == null) {
                            return;
                        }

                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();

                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (isShowDelete) {

            isShowDelete = false;
        } else {

            isShowDelete = true;
        }
        add_picture.setIsShowDelete(isShowDelete);


        return true;
    }
//图片Adpter
    public class Add_picture extends BaseAdapter {
        private Context mcontext;
        private ArrayList<Add_picture_Adpter> mlist;
        private LayoutInflater inflater;
        private boolean isShowDelete;
        private ArrayList<File> flist;

        public Add_picture(Context mcontext, ArrayList<Add_picture_Adpter> mlist, ArrayList<File> flist) {
            this.mcontext = mcontext;
            this.mlist = mlist;
            this.inflater = LayoutInflater.from(mcontext);
            this.flist = flist;
        }

        public void setIsShowDelete(boolean isShowDelete) {
            this.isShowDelete = isShowDelete;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHold hold = null;
            if (convertView == null) {
                hold = new ViewHold();
                convertView = inflater.inflate(R.layout.layout_add_picture, null);
                hold.imageView = (ImageView) convertView.findViewById(R.id.add_picture);
                hold.delete = (ImageView) convertView.findViewById(R.id.delete);

                convertView.setTag(hold);

            } else {
                hold = (ViewHold) convertView.getTag();


            }
            hold.imageView.setImageBitmap(mlist.get(position).getBitmap());
            hold.delete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);// 设置删除按钮是否显示
            hold.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlist.remove(position);
                    flist.remove(position);
                    isShowDelete = false;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHold {
            private ImageView imageView;
            private ImageView delete;


        }
    }


    /*****
     * 上传催记
     *
     * @param id
     */
    void PostHttp(String id) {
        dialog.show();
        RequestParams params = new RequestParams(Api.staff_car_orders.staff_car_orders + id + "/collection_records/");
        params.setMultipart(true);
        params.addBodyParameter("detail", newProgressEdit.getText().toString());
        params.addBodyParameter("position", location_service.adress);
        params.addBodyParameter("position_coordinate", location_service.location);

        if (file_video != null) {
            if (file_video.exists()) {

                params.addBodyParameter("video", file_video);
            }
        }
        if (flist.size() != 0) {
            try {

                for (int i = 0; i < flist.size(); i++) {
                    params.addBodyParameter("img" + (i + 1), flist.get(i));

                }

            } catch (Exception o) {

            }
        }


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
                Log.e("result", result.toString());
                try {
                    JSONObject mjs = new JSONObject(result.toString());
                    if (mjs.isNull("id") == true) {

                        Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT).show();

                    }
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {


            }

        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        location_service.StopLocation();
        if (file_video != null) {

            if (file_video.exists()) {

                file_video.delete();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        location_service.StopLocation();

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

}
