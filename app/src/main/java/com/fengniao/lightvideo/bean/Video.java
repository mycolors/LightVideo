package com.fengniao.lightvideo.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    private String title;
    private String path;

    public Video(){

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(path);
    }

    public Video(Parcel in) {
        this.title = in.readString();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
