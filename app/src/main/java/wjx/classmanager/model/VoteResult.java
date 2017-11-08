package wjx.classmanager.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 作者：国富小哥
 * 日期：2017/10/26
 * Created by Administrator
 */

public class VoteResult extends BmobObject implements Serializable {

    private String nameForCreate;//创建活动的人
    private String timeForCreate;//创建活动的时间
    private String backgroundImage;//背景图
    private String appraiseTitle;//活动的标题
    private String introduction;//活动的简介
    private int likeCount;//投票的总数

    public String getNameForCreate() {
        return nameForCreate;
    }

    public void setNameForCreate(String nameForCreate) {
        this.nameForCreate = nameForCreate;
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

    public String getAppraiseTitle() {
        return appraiseTitle;
    }

    public void setAppraiseTitle(String appraiseTitle) {
        this.appraiseTitle = appraiseTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
