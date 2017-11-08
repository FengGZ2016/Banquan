package wjx.classmanager.view;

import static android.R.attr.type;

/**
 * Created by wjx on 2017/10/14.
 */

public interface MyClassView {
    void onFailed(String s);
    void onExitSuccess(String s);
    void onStartDealWith(String s);
    void onChangeSuccess(String s);
    void sendBroadcast(String title,int type);
}
