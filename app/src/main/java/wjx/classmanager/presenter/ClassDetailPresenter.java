package wjx.classmanager.presenter;

import com.hyphenate.chat.EMGroup;

/**
 * 作者：国富小哥
 * 日期：2017/10/18
 * Created by Administrator
 */

public interface ClassDetailPresenter {
    void getGroupData(String groupid);

    void addToClass(EMGroup emGroup);
}
