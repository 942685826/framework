package com.yaxon.frameWork.view.videoview;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import com.yaxon.frameWork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频播放
 *
 * @author guojiaping
 * @version 2016-11-16 创建<br>
 */
public class VideoActivity extends Activity implements View.OnTouchListener {
    private VideoView videoView;
    RelativeLayout videoLayout;
    private boolean isFullScreen = false;
    private GestureDetector mGesture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        initView();
        setVideoParam();
        startVideo();
    }

    private void initView() {
        videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    private void setVideoParam() {
//        String videoUrl = Environment.getExternalStorageDirectory().getPath() + "/test.mp4";//从SD卡获取视频资源
        String videoUrl = "android.resource://" + getPackageName() + "/" + R.raw.test;//从资源文件获取
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        videoView.setMediaController(new android.widget.MediaController(this));//设置播放控制条
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(VideoActivity.this, "播放完了", Toast.LENGTH_LONG).show();
            }
        });

        videoView.requestFocus();//获取焦点
        videoView.setOnTouchListener(this);//设置触摸监听单击和双击事件
    }

    //开始播放
    private void startVideo() {
        videoView.start();
    }

    //停止播放
    private void stopVideo() {
        videoView.pause();
    }

    /**
     * 跳到某时间播放
     *
     * @param time 时间（单位：s）
     */
    private void seekTo(int time) {
        videoView.seekTo(time);
    }


    //全屏切换
    private void fullScreen() {
        if (!isFullScreen) {

            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.FILL_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoLayout.setLayoutParams(layoutParams);
            videoView.setLayoutParams(layoutParams);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isFullScreen = true;
        } else {

            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT,
                            800);
            videoLayout.setLayoutParams(layoutParams);
            videoView.setLayoutParams(layoutParams);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            isFullScreen = false;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGesture == null) {
            mGesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    //返回false的话只能响应长摁事件
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }
            });
            mGesture.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
//                    ToastUtils.showShort(MainActivity.this,"单击事件");
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
//                    ToastUtils.showShort(MainActivity.this,"双击事件");
                    fullScreen();
                    return true;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });
        }

        return mGesture.onTouchEvent(event);
    }
}
