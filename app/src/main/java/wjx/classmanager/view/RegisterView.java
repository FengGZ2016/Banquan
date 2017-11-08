package wjx.classmanager.view;

/**
 * Created by wjx on 2017/9/19.
 */

public interface RegisterView {

    //获取验证码后休眠
    void onGetCodeSleep();

    //显示一个对话框
    void showDialog();

    //隐藏一个对话框
    void hideDialog();

    //用户提示
    void makeToast(String string);

    //注册成功
    void onRegisterSuccess();

    //注册失败
    void onRegisterFailed(String message);
}
