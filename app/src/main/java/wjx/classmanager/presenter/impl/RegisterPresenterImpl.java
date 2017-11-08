package wjx.classmanager.presenter.impl;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Constant;
import wjx.classmanager.presenter.RegisterPresenter;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.RegisterView;

/**
 * Created by wjx on 2017/9/18.
 */

public class RegisterPresenterImpl implements RegisterPresenter {

    private static final int NUM_LENGTH = 11;
    private static final int CODE_LENGTH = 6;

    private RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView registerView) {
        mRegisterView = registerView;
    }

    @Override
    public void getCode(String phone) {
        sendSmsCode(phone);  //发送短信
    }

    @Override
    public void signIn(final String name, final String pwd, final String phone, final String code) {
        mRegisterView.showDialog();
        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                registerInBmob(name, pwd);
            }
        });
    }

    /**
     * 根据手机号发送验证码
     *
     * @param phone
     */
    private void sendSmsCode(String phone) {
        BmobSMS.requestSMSCode(phone, "class", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {
                    mRegisterView.makeToast("发送短信成功");
                    mRegisterView.onGetCodeSleep();
                } else {
                    mRegisterView.makeToast(ex.toString());
                }
            }
        });
    }

    /**
     * 验证码和手机号匹配
     *
     * @param name
     * @param pwd
     * @param phone
     * @param code
     */
    private void verifySmsCode(final String name, final String pwd, String phone, String code) {
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                if (ex == null) {  //短信验证码已验证成功
                    registerInBmob(name, pwd);
                }else{
                    //处理异常
                }
            }
        });
    }

    /**
     * 注册到环信
     *
     * @param username
     * @param password
     */
    private void registerInHuanXin(final String username, final String password) {
        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, password);

                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e( "run: ","环信注册成功" );
                            mRegisterView.hideDialog();
                            mRegisterView.onRegisterSuccess();
                        }
                    });
                } catch (final HyphenateException e) {
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e( "run: ","环信注册不成功" );
                            mRegisterView.hideDialog();
                            if(e.getErrorCode()== EMError.USER_ALREADY_EXIST){
                                mRegisterView.onRegisterFailed("用户已存在");
                            }else{
                                mRegisterView.onRegisterFailed(e.toString());
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 注册到比目
     *
     * @param username
     * @param password
     */
    private void registerInBmob(final String username, final String password) {

        ClassUser user=new ClassUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<ClassUser>() {

            @Override
            public void done(ClassUser classUser, BmobException e) {
                if (e==null){
                    registerInHuanXin(username, password);
                }else {
                    mRegisterView.hideDialog();
                    notifyRegisterFailed(e);
                }
            }
        });

    }

    private void notifyRegisterFailed(BmobException e) {
        if (e.getErrorCode() == Constant.ErrorCode.USER_ALREADY_EXIST) {
            mRegisterView.onRegisterFailed("用户名已存在");
        } else {
            mRegisterView.onRegisterFailed("..====");
        }
    }

    /**
     * 手机号输入错误信息
     *
     * @param number
     * @return
     */
    public String getPhoneErrorText(String number) {
        if (number.length() < NUM_LENGTH) {
            return null;
        } else if (number.length() > NUM_LENGTH) {
            return "手机号过长";
        } else {
            if (isPhoneNumCorrect(number)) {
                return null;
            } else {
                return "手机号不正确";
            }
        }
    }

    /**
     * 验证码输入错误信息
     *
     * @param code
     * @return
     */
    public String getCodeErrorText(String code) {
        if (code.length() > CODE_LENGTH) {
            return "验证码过长";
        } else {
            return null;
        }
    }

    /**
     * 手机号码验证
     *
     * @param phone
     * @return
     */
    public boolean isPhoneNumCorrect(String phone) {
        //电话格式表达式
        String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        //查看模式
        Pattern pattern = Pattern.compile(regex);
        //判断str是否匹配，返回匹配结果
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }

    /**
     * 禁止输入空格
     *
     * @param editText
     */
    public void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止输入特殊符号
     *
     * @param editText
     */
    public void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
    }
}
