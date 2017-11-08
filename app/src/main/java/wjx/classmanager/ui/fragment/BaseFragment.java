package wjx.classmanager.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import wjx.classmanager.application.MyApplication;

/**
 * Created by wjx on 2017/9/16.
 */

public abstract class BaseFragment extends Fragment {

    protected View mView;
    private Toast mToast;
    protected Context mContext;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutResourceId(), container, false);
            mContext = MyApplication.getMyContext();
            initView();
            initListener();
        }
        return mView;
    }

    protected abstract void initListener();

    protected abstract void initView();

    public abstract int getLayoutResourceId();

    protected void startActivity(Class activity) {
        startActivity(activity, false);
    }

    protected void startActivity(Class activity, String key, String extra) {
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra(key, extra);
        startActivity(intent);
    }

    protected void startActivity(Class activity, boolean finish) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
        if (finish) {
            getActivity().finish();
        }
    }

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        //直接刷新显示内容
        mToast.setText(msg);
        mToast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mToast=null;
        mProgressDialog = null;
    }
}
