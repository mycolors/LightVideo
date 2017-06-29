package com.fengniao.lightvideo.util;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.fengniao.lightvideo.bean.Video;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    //获取手机中的视频,以list形式返回
    public static List<Video> getVideoList(Context context) {
        ArrayList<Video> list = new ArrayList<>();
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA};
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                Video video = new Video();
                video.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                list.add(video);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;

    }
}
