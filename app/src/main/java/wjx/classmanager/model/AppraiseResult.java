package wjx.classmanager.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 作者：国富小哥
 * 日期：2017/10/22
 * Created by Administrator
 *
 * 评优项目的结果集
 */

public class AppraiseResult extends BmobObject implements Serializable{

    private String classId;//班级的id
    private String nameForCreate;//创建评优的人
    private String timeForCreate;//创建评优的时间
    private String backgroundImage;//背景图
    private String appraiseTitle;//评优的标题
    private String introduction;//评优的简介
    private int likeCount;//投票的总数


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTimeForCreate() {
        return timeForCreate;
    }

    public void setTimeForCreate(String timeForCreate) {
        this.timeForCreate = timeForCreate;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getNameForCreate() {
        return nameForCreate;
    }

    public void setNameForCreate(String nameForCreate) {
        this.nameForCreate = nameForCreate;
    }

    public String getAppraiseTitle() {
        return appraiseTitle;
    }

    public void setAppraiseTitle(String appraiseTitle) {
        this.appraiseTitle = appraiseTitle;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
