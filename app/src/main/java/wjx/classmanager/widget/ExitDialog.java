package wjx.classmanager.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.R;

/**
 * Created by wjx on 2017/10/8.
 */

public class ExitDialog extends AlertDialog implements View.OnClickListener{
    public TextView mContentText;
    private Button mPositiveButton;
    private Button mNegativeButton;

    public ExitDialog(@NonNull Context context) {
        super(context);
    }

    public ExitDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        setCancelable(false);

        initView();
        initListener();
    }

    private void initView() {

        mContentText = (TextView) findViewById(R.id.dialog_content);
        mPositiveButton = (Button)findViewById(R.id.dialog_positive_btn);
        mNegativeButton = (Button)findViewById(R.id.dialog_negative_btn);
        mContentText.setText("您确定要退出("+ BmobUser.getCurrentUser().getUsername()+")登录吗");
    }

    private void initListener() {
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_positive_btn:
                if(mOnPositiveButtonClickListener!=null){
                    mOnPositiveButtonClickListener.onPositiveButtonClick(this);
                }
                break;
            case R.id.dialog_negative_btn:
                this.dismiss();
                break;
        }
    }

    private onPositiveButtonClickListener mOnPositiveButtonClickListener;

    public interface onPositiveButtonClickListener{
        void onPositiveButtonClick(ExitDialog exitDialog);
    }

    public void setPositiveButtonClick(onPositiveButtonClickListener onPositiveButtonClickListener){
        mOnPositiveButtonClickListener=onPositiveButtonClickListener;
    }
}
