package wjx.classmanager.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by wjx on 2017/10/15.
 *
 * 班级表
 */

public class BmobClass extends BmobObject{

    private String groupOwner;
    private String className;
    private String groupId;
    private String classDescription;

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }
}
