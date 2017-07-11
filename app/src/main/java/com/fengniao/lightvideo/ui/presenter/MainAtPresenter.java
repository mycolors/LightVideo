package com.fengniao.lightvideo.ui.presenter;

import com.fengniao.lightvideo.bean.Video;
import com.fengniao.lightvideo.ui.base.BaseActivity;
import com.fengniao.lightvideo.ui.base.BasePresenter;
import com.fengniao.lightvideo.ui.view.IMainAtView;
import com.fengniao.lightvideo.util.Utils;

import java.util.List;

public class MainAtPresenter extends BasePresenter<IMainAtView> {


    public MainAtPresenter(BaseActivity mContext) {
        super(mContext);
    }

    public List<Video> getVideo() {
        return Utils.getVideoList(mContext);
    }

}
