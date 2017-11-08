package wjx.classmanager.view;

/**
 * Created by wjx on 2017/10/5.
 */

public interface MainView {

    void logoutSuccess();

    void logoutFailed();

    void onStartLogout();

    void toMyClass(String classId);

    void noClass();
}
