package xyz.pozhu.lightvideo.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import xyz.pozhu.lightvideo.R;


public class VideoDialog extends Dialog {
    private TextView textPercent;

    public VideoDialog(@NonNull Context context) {
        super(context, R.style.VideoDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_dialog);
        textPercent = (TextView) findViewById(R.id.text_percent);
    }


    public void setPercent(float percent) {
        percent = Math.round(percent);
        setText(percent + "%");
    }

    public void setTime(float time) {
        int second = (int) (time / 1000);
        int hour = second / 3600;
        second %= 3600;
        int minute = second / 60;
        second %= 60;
        setText((hour < 10 ? ("0" + hour) : hour) +
                ":" + (minute < 10 ? ("0" + minute) : minute) +
                ":" + (second < 10 ? ("0" + second) : second));
    }


    public void setText(String str) {
        if (textPercent == null) return;
        textPercent.setText(str);
    }


}
