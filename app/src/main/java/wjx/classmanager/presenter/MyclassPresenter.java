package wjx.classmanager.presenter;

/**
 * Created by wjx on 2017/9/20.
 */

public interface MyclassPresenter {
    void disbandClass(String groupId);
    void exitClass(String groupId);

    void changeClassDesc(String groupId,String desc);
    void changeClassName(String groupId,String name);
}
