package wjx.classmanager.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 作者：国富小哥
 * 日期：2017/10/22
 * Created by Administrator
 *
 * 推优评优的model
 */

public class Appraising extends BmobObject{

    private String classId;//班级id
    private String title;//标题
    private String name;//姓名
    private String avatar;//头像
    private String motto;//座右铭
    private List<String> likeNameList;//投赞成票的人名
    private int likeNum;//投赞成票的数目

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public List<String> getLikeNameList() {
        return likeNameList;
    }

    public void setLikeNameList(List<String> likeNameList) {
        this.likeNameList = likeNameList;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
