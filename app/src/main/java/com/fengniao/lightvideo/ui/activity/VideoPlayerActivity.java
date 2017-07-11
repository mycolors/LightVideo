package com.fengniao.lightvideo.ui.activity;

import android.content.pm.PackageManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fengniao.lightvideo.R;
import com.fengniao.lightvideo.manager.VideoPlayerManager;
import com.fengniao.lightvideo.ui.base.BaseActivity;
import com.fengniao.lightvideo.ui.presenter.VideoPlayerAtPresenter;
import com.fengniao.lightvideo.ui.view.IVideoPlayerAtView;
import com.fengniao.lightvideo.ui.widget.media.IjkVideoView;

import butterknife.BindView;


public class VideoPlayerActivity extends BaseActivity<IVideoPlayerAtView, VideoPlayerAtPresenter> implements IVideoPlayerAtView,
        IjkVideoView.MediaControl {
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

    private VideoPlayerManager mManager;


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

    @Override
    public void init() {
        super.init();
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initView() {
        super.initView();
        mPresenter.loadData();
        mPresenter.initVideoPlayerManager();
        mManager = mPresenter.getVideoPlayerManager();
    }

    @Override
    public VideoPlayerAtPresenter createPresent() {
        return new VideoPlayerAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_video_player;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    mPresenter.playVideo();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    showToast("读取内存卡失败");
                }
            }
        }
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

    @Override
    public TableLayout getHubView() {
        return mHudView;
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }
}
