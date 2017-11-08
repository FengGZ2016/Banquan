package wjx.classmanager.ui.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import wjx.classmanager.R;

import static wjx.classmanager.model.Constant.ErrorCode.ERROR_ACTIVITY;

public class ErrorActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTextTitle;
    private TextView mTextError;
    private ImageView mImageBack;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mTextTitle = (TextView) findViewById(R.id.back_title);
        mTextError = (TextView) findViewById(R.id.error_text);
        mImageBack = (ImageView) findViewById(R.id.back_image);
    }

    @Override
    public void initListener() {
        mImageBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTextTitle.setText("错误页");
        mTextError.setText(getIntent().getStringExtra(ERROR_ACTIVITY));
    }

    @Override
    public int getLayout() {
        return R.layout.activity_error;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
