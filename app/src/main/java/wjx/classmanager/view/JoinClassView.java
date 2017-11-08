package wjx.classmanager.view;

import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;

import java.util.List;

/**
 * Created by wjx on 2017/10/14.
 */

public interface JoinClassView {
    void onStartSearch();

    void getServerDataSuccess(EMCursorResult<EMGroupInfo> result, List<EMGroupInfo> returnGroups);

    void getServerDataFail();
}
