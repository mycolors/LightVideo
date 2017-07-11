package com.fengniao.lightvideo.ui.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.fengniao.lightvideo.bean.Video;
import com.fengniao.lightvideo.manager.VideoPlayerManager;
import com.fengniao.lightvideo.ui.activity.VideoPlayerActivity;
import com.fengniao.lightvideo.ui.base.BaseActivity;
import com.fengniao.lightvideo.ui.base.BasePresenter;
import com.fengniao.lightvideo.ui.view.IVideoPlayerAtView;
import com.fengniao.lightvideo.ui.widget.media.AndroidMediaController;


public class VideoPlayerAtPresenter extends BasePresenter<IVideoPlayerAtView> {

    private Video mVideo;

    private AndroidMediaController mMediaController;

    private VideoPlayerManager mManager;

    public VideoPlayerAtPresenter(BaseActivity mContext) {
        super(mContext);
    }

    public void loadData() {
        mVideo = mContext.getIntent().getParcelableExtra("video");
    }

    public void initVideoPlayerManager() {
        if (mVideo == null) return;
        mManager = new VideoPlayerManager(mContext);
        mManager.fullChangeScreen();
        mMediaController = new AndroidMediaController(mContext);
        if (mContext.getSupportActionBar() != null) {
            getView().setTitle(mVideo.getTitle());
            mMediaController.setSupportActionBar(mContext.getSupportActionBar());
        }
        mManager.setMediaController(mMediaController);
        ((VideoPlayerActivity) mContext).hideMediaControl();
        mManager.setHudView(getView().getHubView());
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            playVideo();
        }
    }

    public void playVideo() {
        if (mVideo == null) return;
        mManager.setVideoPath(mVideo.getPath());
        mManager.start();
    }


    public VideoPlayerManager getVideoPlayerManager() {
        return mManager;
    }


}
