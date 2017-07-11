package com.fengniao.lightvideo.manager;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.Toast;

import com.fengniao.lightvideo.R;
import com.fengniao.lightvideo.ui.widget.VideoDialog;
import com.fengniao.lightvideo.ui.widget.media.IMediaController;
import com.fengniao.lightvideo.ui.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayerManager {

    private Activity mAcitvity;
    private IjkVideoView mVideoView;
    private VideoDialog mDialog;

    private IMediaController mMediaController;

    private IjkVideoView.MediaControl mMediaControl;

    private GestureDetector mGestureDetector;


    public VideoPlayerManager(Activity activity) {
        this.mAcitvity = activity;
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView = (IjkVideoView) mAcitvity.findViewById(R.id.video_view);
        mDialog = new VideoDialog(mAcitvity);
        mGestureDetector = new GestureDetector(activity, new MyDetectorListener());
    }

    public void setMediaController(IMediaController controller) {
        if (mMediaControl != null) return;
        mMediaController = controller;
        mVideoView.setMediaController(mMediaController);
    }

    public void setMeidaControl(IjkVideoView.MediaControl meidaControl) {
        if (mMediaController != null) return;
        this.mMediaControl = meidaControl;
    }

    public void setHudView(TableLayout tabLayout) {
        mVideoView.setHudView(tabLayout);
    }

    //本地视频
    public void setVideoPath(String path) {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(mAcitvity, "路径为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mVideoView.setVideoPath(path);

    }


    //网络视频
    public void setVideoURI(Uri uri) {
        mVideoView.setVideoURI(uri);
    }


    public void start() {
        mVideoView.start();
    }

    public void pause() {
        mVideoView.pause();
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mVideoView != null && mVideoView.isPlaying();
    }

    public void stop() {
        mVideoView.stopPlayback();
    }

    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    /**
     * get video duration
     *
     * @return
     */
    public int getDuration() {
        return mVideoView.getDuration();
    }

    public void seekTo(int msec) {
        mVideoView.seekTo(msec);

    }


    private int mLastPlayedTime;

    public void onPause() {
        pause();
        mLastPlayedTime = getCurrentPosition();
    }

    public void onResume() {
        if (mLastPlayedTime > 0) {
            seekTo(mLastPlayedTime);
            start();
        }
    }


    public void onDestroy() {
        mAcitvity = null;
        mVideoView = null;
        mDialog = null;
        mMediaController = null;
        mMediaControl = null;
        mGestureDetector = null;

    }

    private boolean mBackPressed;

    public void onStop() {
        //点击返回或不允许后台播放时 释放资源
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    public void onBackPressed() {
        mBackPressed = true;
    }

    //横竖屏转换
    public void fullChangeScreen() {
        if (mAcitvity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            mAcitvity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
//        if (mAcitvity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            mAcitvity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        } else {
//            mAcitvity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    public void onTouchActionUp() {
        if (mMediaController != null) {
            if (mMediaController.isShowing()) {
                mMediaController.hide();
            } else {
                mMediaController.show();
            }
        }
        if (isToSeek) {
            if (isPlayingBeforeToSeek) {
                start();
            }
            isToSeek = true;
        }
        oldX = 0;
        oldY = 0;
        if (mDialog.isShowing()) mDialog.dismiss();
    }

    public GestureDetector getGestureDetector() {
        return mGestureDetector;
    }


    private float oldX;
    private float oldY;
    private boolean isToSeek;
    private boolean isPlayingBeforeToSeek;

    private class MyDetectorListener extends GestureDetector.SimpleOnGestureListener {
        private boolean isFirstTouch;
        private boolean volumeControl;

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getRawX();
            if (oldY == 0) oldY = e1.getRawY();
            float deltaY = oldY - e2.getRawY();
            oldY = e2.getRawY();
            if (isFirstTouch) {
                isToSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = mOldX > mVideoView.getmVideoWidth() * 0.5f;
                isFirstTouch = false;
            }

            if (isToSeek) {
                if (oldX == 0) {
                    oldX = e1.getRawX();
                    isPlayingBeforeToSeek = isPlaying();
                }
                if (isPlaying())
                    pause();
                onVideoSpeed(e2.getRawX() - oldX);
                oldX = e2.getRawX();
            } else {
                float percent = deltaY / mVideoView.getmVideoHeight() / 2;
                if (volumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //双击暂停
            if (isPlaying()) {
                pause();
            } else
                start();
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isFirstTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }

    //滑动控制视频
    public void onVideoSpeed(float distanceX) {
        if (mMediaController != null) {
            if (!mMediaController.isShowing()) mMediaController.show();
        }
        int mVideoTotalLength = mVideoView.getDuration();
        int currentPositon = mVideoView.getCurrentPosition();
        currentPositon += 240000 * distanceX / mVideoView.getmVideoWidth();
        if (currentPositon > mVideoTotalLength)
            currentPositon = mVideoTotalLength;
        else if (currentPositon < 0)
            currentPositon = 0;
        mVideoView.seekTo(currentPositon);
        mDialog.setTime(currentPositon);
        if (!mDialog.isShowing())
            mDialog.show();
    }

    //滑动修改亮度
    public void onBrightnessSlide(float percent) {
        float brightness = mAcitvity.getWindow().getAttributes().screenBrightness;
        if (brightness < 0.00f) {
            brightness = 0.00f;
        }
        if (brightness > 1.0f)
            brightness = 1.0f;
        WindowManager.LayoutParams layoutParams = mAcitvity.getWindow().getAttributes();
        layoutParams.screenBrightness = brightness + percent;
        if (layoutParams.screenBrightness > 1.0f) {
            layoutParams.screenBrightness = 1.0f;
        } else if (layoutParams.screenBrightness < 0.00f) {
            layoutParams.screenBrightness = 0.00f;
        }
        mAcitvity.getWindow().setAttributes(layoutParams);
        mDialog.setPercent(layoutParams.screenBrightness * 100);
//        mDialog.setText(Math.round(layoutParams.screenBrightness)+"");
        if (!mDialog.isShowing())
            mDialog.show();
    }

    private AudioManager mAudioManager;

    private int maxVolume;


    private float index;

    public void onVolumeSlide(float percent) {
        if (mAudioManager == null)
            mAudioManager = (AudioManager) mAcitvity.getSystemService(Context.AUDIO_SERVICE);
        if (maxVolume == 0)
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume = volume < 0 ? 0 : volume;
        if (index == 0) index = volume;
        index += (percent * maxVolume);
        if (index > maxVolume)
            index = maxVolume;
        else if (index < 0)
            index = 0;
        if (Math.abs(index - volume) >= 1) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round(index), 0);
        }
//        mDialog.setPercent(Math.round(index) * 100 / maxVolume);
        mDialog.setText(Math.round(index) + "");
        if (!mDialog.isShowing())
            mDialog.show();
    }


    public interface PlayerControl {
        void play();

        void pause();

        void error();
    }


}


