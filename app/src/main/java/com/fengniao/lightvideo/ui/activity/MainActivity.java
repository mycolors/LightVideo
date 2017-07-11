package com.fengniao.lightvideo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fengniao.lightvideo.R;
import com.fengniao.lightvideo.bean.Video;
import com.fengniao.lightvideo.ui.adapter.VideoListAdapter;
import com.fengniao.lightvideo.ui.base.BaseActivity;
import com.fengniao.lightvideo.ui.presenter.MainAtPresenter;
import com.fengniao.lightvideo.ui.view.IMainAtView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<IMainAtView, MainAtPresenter> implements IMainAtView {
    @BindView(R.id.video_list)
    RecyclerView videoList;

    private VideoListAdapter mAdapter;

    private List<Video> mList;

    @Override
    public void initView() {
        super.initView();
        mList = new ArrayList<>();
        mAdapter = new VideoListAdapter(this, mList);
        videoList.setLayoutManager(new LinearLayoutManager(this));
        videoList.setAdapter(mAdapter);
        mAdapter.setmOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                intent.putExtra("video", mList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            showVideoList(mPresenter.getVideo());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    showVideoList(mPresenter.getVideo());
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    showToast("读取内存卡失败");
                }
            }
        }
    }

    @Override
    public MainAtPresenter createPresent() {
        return new MainAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    public void showVideoList(List<Video> list) {
        if (!list.isEmpty()) {
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}
