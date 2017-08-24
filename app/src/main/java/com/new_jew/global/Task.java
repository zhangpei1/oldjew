package com.new_jew.global;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.new_jew.bean.Add_picture_Adpter;
import com.new_jew.customview.CustomDialog;
import com.new_jew.toolkit.BitmapCompressUtils;
import com.new_jew.ui.collectoractivity.Add_trajectory_Activity;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by zhangpei on 2016/9/12.
 */
public class Task extends AsyncTask<File,Integer,Bitmap> {

    public Task(Context context, ArrayList<File> flist, ArrayList<Add_picture_Adpter> mlist, Add_trajectory_Activity.Add_picture add_picture, File mfile, CustomDialog dialog) {
        this.context = context;
        this.flist=flist;
        this.mlist=mlist;
        this.add_picture=add_picture;
        this.mfile=mfile;
        this.dialog=dialog;
    }

    private Context context;
    private ArrayList<File> flist;
    private ArrayList<Add_picture_Adpter> mlist;
    private Add_trajectory_Activity.Add_picture add_picture;
    private File mfile;
    private Bitmap bitmap;
    private CustomDialog dialog;



    //后台执行
    protected Bitmap doInBackground(File... params) {

        Luban.get(context).
                load(mfile).
                putGear(Luban.THIRD_GEAR).
                setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                        Log.e("luban", "开始了");

                    }

                    @Override
                    public void onSuccess(File file) {
                        flist.add(file);
                         bitmap=  BitmapCompressUtils.getImageThumbnail(file.getAbsolutePath(), 200, 250);
//                                Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                        mlist.add(new Add_picture_Adpter(bitmap));
                        add_picture.notifyDataSetChanged();

                        Log.e("luban", String.valueOf(file.length() / 1024) + "KB");
//                             bitmap.recycle();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Throwable",String.valueOf(e));
                    }
                }).launch();

        return bitmap;
    }


    @Override
    /**
     * 将后台操作与主UI线程联系起来的方法，完成时调用
     * @param bitmap  结果
     */
    protected void onPostExecute(Bitmap bitmap) {
        dialog.dismiss();

    }

    @Override
    /**
     * 将后台操作与主UI线程联系起来的方法,数据更新时调用
     * @param progress  完成度
     */
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
