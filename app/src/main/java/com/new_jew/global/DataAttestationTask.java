package com.new_jew.global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.new_jew.bean.Add_picture_Adpter;
import com.new_jew.customview.CustomDialog;
import com.new_jew.toolkit.BitmapCompressUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by zhangpei on 16-12-29.
 */

public class DataAttestationTask extends AsyncTask<File,Integer,Bitmap>{
    private File mfile;
    private Context context;
    private Bitmap bitmap;
    private ImageView imageView;
    private CustomDialog dialog;
    private int id=0;
    private File imagefile;
    private String paths="";

    public DataAttestationTask(File mfile, Context context, ImageView imageView,
                               CustomDialog dialog, int id, File imagefile, String paths) {
        this.mfile = mfile;
        this.context = context;
        this.imageView = imageView;
        this.dialog = dialog;
        this.id = id;
        this.imagefile = imagefile;
        this.paths = paths;
    }

    @Override
    protected Bitmap doInBackground(File... params) {
        Luban.get(context).
                load(mfile).
                     putGear(3).
                putGear(Luban.THIRD_GEAR).
                setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                        Log.e("luban", "开始了");

                    }

                    @Override
                    public void onSuccess(File file) {
//                        bitmap=  BitmapCompressUtils.getImageThumbnail(file.getAbsolutePath(), 100, 70);
                        Bitmap bitmap1= BitmapFactory.decodeFile(file.getAbsolutePath().toString());
//                        imageView.setImageBitmap(bitmap);
                        if (id == 1) {
                            setPicToView(bitmap1, "just.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来
//                            bitmap.recycle();
                        } else if (id == 2) {
                            setPicToView(bitmap1, "just1.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just1.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来


                        } else if (id == 3) {
                            setPicToView(bitmap1, "just2.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just2.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来



                        } else if (id == 4) {
                            setPicToView(bitmap1, "just3.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just3.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来

//
                        } else if (id == 5) {

                            setPicToView(bitmap1, "just4.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just4.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来

                        } else if (id == 6) {

                            setPicToView(bitmap1, "just5.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just5.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来


                        } else {

                            setPicToView(bitmap1, "just6.jpg");//保存在SD卡中
                            imagefile = new File(paths + "just6.jpg");
                            imageView.setImageBitmap(bitmap1);//用ImageView显示出来

                        }
//                    popupWindow.dismiss();//关闭弹出框


                        Log.e("luban", String.valueOf(file.length() / 1024) + "KB");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Throwable",String.valueOf(e));
                        Toast.makeText(context.getApplicationContext(),"图片压缩失败!",Toast.LENGTH_SHORT).show();
                    }
                }).launch();

        return bitmap;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
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
}
