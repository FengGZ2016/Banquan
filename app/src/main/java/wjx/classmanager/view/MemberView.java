package wjx.classmanager.view;

import java.util.List;

import wjx.classmanager.model.ClassUser;

/**
 * Created by wjx on 2017/10/9.
 */

public interface MemberView {

    void getServerDataSuccess(List<ClassUser> members);

    void getServerDataFail();

    void noClass();
}
