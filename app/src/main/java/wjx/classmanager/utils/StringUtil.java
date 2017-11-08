package wjx.classmanager.utils;

/**
 * Created by wjx on 2017/9/27.
 */

public class StringUtil {

    //用户名、密码的匹配规则
    private static final String USER_NAME_REGEX = "^[a-zA-Z]\\w{2,19}$";
    private static final String PASSWORD_REGEX = "^[0-9]{3,20}$";

    public static boolean checkUserName(String userName){
        return userName.matches(USER_NAME_REGEX);
    }

    public static boolean checkPassword(String password){
        return password.matches(PASSWORD_REGEX);
    }
}
