package wjx.classmanager.presenter;

/**
 * Created by wjx on 2017/9/18.
 */

public interface RegisterPresenter {

    //获取验证码
    void getCode(String phone);

    //登录
    void signIn(String username,String password,String phone, String code);
}
