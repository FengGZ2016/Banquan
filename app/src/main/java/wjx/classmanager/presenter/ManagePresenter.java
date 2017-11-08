package wjx.classmanager.presenter;

import java.util.List;

import wjx.classmanager.model.Manage;

/**
 * Created by wjx on 2017/10/9.
 */

public interface ManagePresenter {
    
    List<Manage> getManageList();

    void checkItemType(String title);
}
