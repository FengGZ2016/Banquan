package wjx.classmanager.view;

import java.util.List;

import wjx.classmanager.model.Notification;

/**
 * Created by wjx on 2017/10/9.
 */

public interface MessageView {

    void onGetBmobData();

    void getBmobDataFail();

    void getBmobDataSuccess(List<Notification> list);
}
