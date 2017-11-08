package wjx.classmanager.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 作者：国富小哥
 * 日期：2017/11/3
 * Created by Administrator
 *
 * 通知的表
 */

public class Notification extends BmobObject implements Serializable{

    /**
     * 班级id
     */
    private String classId;
    /**
     * 通知标题
     */
    private String title;
    /**
     * 通知的时间
     */
    private String time;
    /**
     * 通知的内容
     */
    private String content;
    /**
     * 附件的路径
     */
    private String filePath;



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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
