package wjx.classmanager.view;

/**
 * Created by wjx on 2017/9/30.
 */

public interface CreateClassView {

    void classNameErroe();

    void onCreateClass();

    void onCreateSuccess(String groupId,String objectId);

    void onCreateFailed(String error);

    void sendBroadcast();
}
