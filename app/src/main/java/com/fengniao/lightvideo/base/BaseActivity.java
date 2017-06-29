package com.fengniao.lightvideo.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    public Toolbar mToolbar;
    protected String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "正在加载数据...";
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }


    public void setTextViewText(int id, CharSequence text) {
        TextView tv = (TextView) findViewById(id);
        if (tv != null) {
            tv.setText(text);
        }
    }

    public Activity getActivity() {
        return this;
    }

}
