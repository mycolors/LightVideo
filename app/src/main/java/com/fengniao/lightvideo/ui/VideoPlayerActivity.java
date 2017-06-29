package com.fengniao.lightvideo.ui;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fengniao.lightvideo.R;
import com.fengniao.lightvideo.base.BaseActivity;
import com.fengniao.lightvideo.bean.Video;
import com.fengniao.lightvideo.manager.VideoPlayerManager;
import com.fengniao.lightvideo.ui.widget.media.AndroidMediaController;
import com.fengniao.lightvideo.ui.widget.media.IjkVideoView;

import butterknife.BindView;


public class VideoPlayerActivity extends BaseActivity implements VideoPlayerManager.MediaControl, VideoPlayerManager.PlayerControl {
    @BindView(R.id.video_view)
    IjkVideoView videoView;

    @BindView(R.id.linear_media_control)
    LinearLayout linearMediaControl;

    @BindView(R.id.text_current_position)
    TextView textCurremtPosition;

    @BindView(R.id.text_total_lengh)
    TextView textTotalLengh;

    @BindView(R.id.seekbar)
    SeekBar seekBar;

    @BindView(R.id.hud_view)
    TableLayout mHudView;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;


    private AndroidMediaController mMediaController;


    private Video mVideo;

    private VideoPlayerManager mManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mManager != null)
            mManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mManager != null)
            mManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mManager != null)
            mManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mManager != null)
            mManager.onDestroy();
    }

    public void initView() {
        mVideo = getIntent().getParcelableExtra("video");
        if (mVideo == null) {
            return;
        }
        setSupportActionBar(toolbar);
        initVideoPlayerManager();
    }

    public void initVideoPlayerManager() {
        mManager = new VideoPlayerManager(this);
        mManager.fullChangeScreen();
        mMediaController = new AndroidMediaController(this);
        if (getSupportActionBar() != null) {
            title.setText(mVideo.getTitle());
            mMediaController.setSupportActionBar(getSupportActionBar());
        }
        mManager.setMediaController(mMediaController);
        hideMediaControl();
        mManager.setHudView(mHudView);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            playVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    playVideo();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    showToast("读取内存卡失败");
                }
            }
        }
    }

    public void playVideo() {
//        mManager.setVideoURI(Uri.parse("http://playertest.longtailvideo.com/adaptive/wowzaid3/playlist.m3u8"));
        mManager.setVideoPath(mVideo.getPath());
        mManager.start();
    }


    private void fullChangeScreen() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {// 切换为竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("currentPosition", videoView.getCurrentPosition());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mManager == null) return false;
        if (mManager.getGestureDetector().onTouchEvent(event)) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mManager.onTouchActionUp();
                break;
        }
        return super.onTouchEvent(event);
    }


    private boolean isMediaControlShowing;

    @Override
    public void showMediaControl() {
        linearMediaControl.setVisibility(View.VISIBLE);
        isMediaControlShowing = true;
    }

    @Override
    public void hideMediaControl() {
        linearMediaControl.setVisibility(View.GONE);
        isMediaControlShowing = false;
    }


    @Override
    public boolean isShowing() {
        return isMediaControlShowing;
    }

    @Override
    public void showCurrentPosition(int currentPosition) {
        textCurremtPosition.setText(forMatTime(currentPosition));
    }

    @Override
    public void showTotalLengh(int totalLengh) {
        textCurremtPosition.setText(forMatTime(videoView.getDuration()));
    }


    public String forMatTime(int time) {
        int second = time / 1000;
        int hour = second / 3600;
        second %= 3600;
        int minute = second / 60;
        second %= 60;
        return (hour < 10 ? ("0" + hour) : hour) +
                ":" + (minute < 10 ? ("0" + minute) : minute) +
                ":" + (second < 10 ? ("0" + second) : second);
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void error() {

    }

    @Override
    public void onBackPressed() {
        if (mManager != null)
            mManager.onBackPressed();
        super.onBackPressed();
    }
}
