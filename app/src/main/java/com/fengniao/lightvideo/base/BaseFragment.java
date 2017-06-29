package com.fengniao.lightvideo.base;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public abstract int setLayoutId();


    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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



}
