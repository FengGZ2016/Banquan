package wjx.classmanager.ui.activity;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wjx.classmanager.R;
import wjx.classmanager.listener.SoftkeyListener;
import wjx.classmanager.presenter.impl.RegisterPresenterImpl;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.RegisterView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterView,SoftkeyListener.OnSoftKeyBoardChangeListener {

    private EditText mName;
    private EditText mPass;
    private EditText mPhone;    //手机号
    private EditText mCode;     //验证码
    private Button mGetCode;    //获取验证码
    private Button mSign;       //注册

    private TextInputLayout mInputPhone;
    private TextInputLayout mInputCode;
    private RegisterPresenterImpl mRegisterPresenter;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mName = (EditText) findViewById(R.id.register_name);
        mPass = (EditText) findViewById(R.id.register_pass);
        mPhone = (EditText) findViewById(R.id.register_phone);
        mCode = (EditText) findViewById(R.id.register_code);
        mGetCode = (Button) findViewById(R.id.register_getcode);
        mSign = (Button) findViewById(R.id.register_sign);
        mInputPhone = (TextInputLayout) findViewById(R.id.register_input_phone);
        mInputCode = (TextInputLayout) findViewById(R.id.register_input_code);
        mRegisterPresenter = new RegisterPresenterImpl(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.register_relative);

        //禁止输入空格
        mRegisterPresenter.setEditTextInhibitInputSpace(mName);
        mRegisterPresenter.setEditTextInhibitInputSpace(mPass);
        mRegisterPresenter.setEditTextInhibitInputSpace(mPhone);
        mRegisterPresenter.setEditTextInhibitInputSpace(mCode);
    }

    @Override
    public void initListener() {
        mGetCode.setOnClickListener(this);
        mSign.setOnClickListener(this);
        SoftkeyListener.setListener(this,this);
        mCode.setOnEditorActionListener(mOnEditorActionListener);

        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = s.toString().trim();
                mInputPhone.setError(mRegisterPresenter.getPhoneErrorText(number));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code = s.toString().trim();
                mInputCode.setError(mRegisterPresenter.getCodeErrorText(code));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    public boolean isImmersive() {
        return true;
    }

    @Override
    public void onClick(View v) {
        String phone = mPhone.getText().toString();
        String code = mGetCode.getText().toString();
        String name = mName.getText().toString();
        String pwd = mPass.getText().toString();

        switch (v.getId()) {
            case R.id.register_getcode:
                mRegisterPresenter.getCode(phone);
                break;
            case R.id.register_sign:
                mRegisterPresenter.signIn(name,pwd,phone, code);
                break;
        }
    }

    @Override
    public void onGetCodeSleep() {
        TimeCount timeCount = new TimeCount(60000, 1000);
        timeCount.start();
    }

    @Override
    public void showDialog() {
        showProgress("注册中...");
    }

    @Override
    public void hideDialog() {
        hideProgress();
    }

    @Override
    public void makeToast(String string) {
        showToast(string);
    }

    @Override
    public void onRegisterSuccess() {
        Log.e( "onRegisterSuccess: ","注册成功" );
        startActivity(LogInActivity.class);

        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void onRegisterFailed(String message) {
        Log.e( "onRegisterFailed: ","注册失败"+message );
    }

    @Override
    public void keyBoardShow(int height) {
        //软键盘弹出来时
        mRelativeLayout.scrollBy(0,height/2);
    }

    @Override
    public void keyBoardHide(int height) {
        //软键盘隐藏时
        mRelativeLayout.scrollBy(0,0-height/2);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetCode.setBackgroundColor(Color.parseColor("#50000000"));
            mGetCode.setClickable(false);
            mGetCode.setText("(" + millisUntilFinished / 1000 + ") 秒");
        }

        @Override
        public void onFinish() {
            mGetCode.setText("获取验证码");
            mGetCode.setClickable(true);
            mGetCode.setBackgroundColor(Color.parseColor("#ffff8800"));
        }
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String phone = mPhone.getText().toString();
                String code = mGetCode.getText().toString();
                String name = mName.getText().toString();
                String pwd = mPass.getText().toString();
                mRegisterPresenter.signIn(name,pwd,phone,code);
            }
            return true;
        }
    };
}
