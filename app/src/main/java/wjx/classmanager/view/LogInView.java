package wjx.classmanager.view;

/**
 * Created by wjx on 2017/9/26.
 */

public interface LogInView {

    //用户名错误
    void onUsernameError(String error);

    //密码错误
    void onPasswordError(String error);

    //登录成功
    void onLoginSuccess();

    //登录失败
    void onLoginFailed();

    //登录过程，显示对话框
    void onStartLogin();
}
