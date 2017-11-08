package wjx.classmanager.model;

import cn.bmob.v3.BmobUser;

/**
 * 作者：国富小哥
 * 日期：2017/11/1
 * Created by Administrator
 */

public class ClassUser extends BmobUser{

    /**
     * 班级id
     */
    private String groupId;
    /**
     * 班级名称
     */
    private String groupName;
    /**
     * 身份
     */
    private String indentity;
    /**
     * 头像
     */
    private String headImgPath;
    /**
     *  学院
     */
    private String school;
    /**
     * 院系
     */
    private String department;
    /**
     * 专业
     */
    private String major;
    /**
     * 姓名
     */
    private String  realName;
    /**
     * 手机
     */
    private String mobile;


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIndentity() {
        return indentity;
    }

    public void setIndentity(String indentity) {
        this.indentity = indentity;
    }

    public String getHeadImgPath() {
        return headImgPath;
    }

    public void setHeadImgPath(String headImgPath) {
        this.headImgPath = headImgPath;
    }
}
