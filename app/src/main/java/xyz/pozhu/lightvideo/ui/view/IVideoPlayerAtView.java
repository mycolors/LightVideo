package xyz.pozhu.lightvideo.ui.view;


import android.widget.TableLayout;

import xyz.pozhu.lightvideo.manager.VideoPlayerManager;

public interface IVideoPlayerAtView extends VideoPlayerManager.PlayerControl {

    TableLayout getHubView();

    void setTitle(String title);

}
