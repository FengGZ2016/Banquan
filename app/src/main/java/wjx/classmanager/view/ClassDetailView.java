package wjx.classmanager.view;

import com.hyphenate.chat.EMGroup;

/**
 * 作者：国富小哥
 * 日期：2017/10/18
 * Created by Administrator
 */

public interface ClassDetailView {

    void getGroupDataSuccess(EMGroup group, String groupName, String groupOwner, String groupDescription);

    void getGroupDataFail();

    void applyFail();

    void applySuccess();
}
