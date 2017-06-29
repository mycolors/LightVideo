package com.fengniao.lightvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fengniao.lightvideo.base.BaseActivity;
import com.fengniao.lightvideo.bean.Video;
import com.fengniao.lightvideo.ui.VideoPlayerActivity;
import com.fengniao.lightvideo.ui.adapter.VideoListAdapter;
import com.fengniao.lightvideo.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.video_list)
    RecyclerView videoList;
    private List<Video> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public void initData() {
        mList = Utils.getVideoList(this);
    }

    public void initView() {
        VideoListAdapter adapter = new VideoListAdapter(this, mList);
        videoList.setLayoutManager(new LinearLayoutManager(this));
        videoList.setAdapter(adapter);
        adapter.setmOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                intent.putExtra("video", mList.get(position));
                startActivity(intent);
            }
        });
    }


}
