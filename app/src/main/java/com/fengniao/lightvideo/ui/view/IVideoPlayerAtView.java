package com.fengniao.lightvideo.ui.view;


import android.widget.TableLayout;

import com.fengniao.lightvideo.manager.VideoPlayerManager;

public interface IVideoPlayerAtView extends VideoPlayerManager.PlayerControl {

    TableLayout getHubView();

    void setTitle(String title);

}
