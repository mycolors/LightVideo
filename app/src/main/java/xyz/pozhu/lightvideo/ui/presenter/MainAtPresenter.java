package xyz.pozhu.lightvideo.ui.presenter;

import xyz.pozhu.lightvideo.bean.Video;
import xyz.pozhu.lightvideo.ui.base.BaseActivity;
import xyz.pozhu.lightvideo.ui.base.BasePresenter;
import xyz.pozhu.lightvideo.ui.view.IMainAtView;
import xyz.pozhu.lightvideo.util.Utils;

import java.util.List;

public class MainAtPresenter extends BasePresenter<IMainAtView> {


    public MainAtPresenter(BaseActivity mContext) {
        super(mContext);
    }

    public List<Video> getVideo() {
        return Utils.getVideoList(mContext);
    }

}
