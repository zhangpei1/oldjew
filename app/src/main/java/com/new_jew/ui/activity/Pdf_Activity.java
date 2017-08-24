package com.new_jew.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.barteksc.pdfviewer.PDFView;
import com.new_jew.BaseActivity;
import com.new_jew.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;


/**
 * Created by zhangpei on 2016/7/27.
 */
public class Pdf_Activity extends BaseActivity {
    private PDFView pdfview;
    private String murl;
    private String path;
    private LinearLayout back;
    private TextView title_text;

    @Override
    protected void initLayout() {
        Intent intent = getIntent();
        murl = intent.getStringExtra("path");
        Log.e("path", murl);
        pdfview = (PDFView) this.findViewById(R.id.pdfview);
        back = (LinearLayout) this.findViewById(R.id.back);
        title_text = (TextView) this.findViewById(R.id.title_text);


    }

    @Override
    protected void initValue() {
        title_text.setText("PDF");
//        pdfview.fromUri(Uri.parse(murl));
//        pdfview.loadUrl(murl);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有授权，则请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        } else {

            download(murl);
        }

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断请求码
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                download(murl);

            } else {

                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected int setRootView() {
        return R.layout.layout_webview;
    }

    void download(String url) {
        dialog.show();
        RequestParams requestParams = new RequestParams(url);
        path = Environment.getExternalStorageDirectory() + "/jew/PDF/jew.pdf";
        requestParams.setSaveFilePath(path);
        requestParams.setCacheMaxAge(0);
        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                dialog.dismiss();
                pdfview.fromFile(result).load();


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("throwable", String.valueOf(ex));
                dialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       File file=new File(path);
        if (file.exists()){

            file.delete();
        }
        pdfview.recycle();
    }
}
