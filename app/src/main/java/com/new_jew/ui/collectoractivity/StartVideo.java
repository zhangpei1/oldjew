package com.new_jew.ui.collectoractivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.new_jew.BaseActivity;
import com.new_jew.R;


/**
 * Created by zhangpei on 2016/9/8.
 */
public class StartVideo extends BaseActivity {
    private VideoView videoView;
    private ImageView imageishow;
    @Override
    protected void initLayout() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        Intent intent=getIntent();
        String path=intent.getExtras().getString("video");
        videoView= (VideoView) this.findViewById(R.id.startvideo);
//        isplay= (ImageView) this.findViewById(R.id.isplay);
        imageishow= (ImageView) this.findViewById(R.id.imageishow);

        MediaController mediaController=new MediaController(this);
        videoView.setVideoPath(path);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.seekTo(500);

//        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class,path, "嫂子辛苦了");

    }

    @Override
    protected void initValue() {



    }

    @Override
    protected void initListener() {
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                     finish();
            }
        });
        imageishow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()==true){
//                    isplay.setVisibility(View.GONE);
                    videoView.start();

                }else {

//                                       isplay.setVisibility(View.VISIBLE);

                    videoView.stopPlayback();
                }

            }
        });
    }

    @Override
    protected int setRootView() {
        return R.layout.startvideo_layout;
    }
    @Override
    public void onResume() {
        /**
         * 设置为横屏
         */
//        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        super.onResume();
    }

}
