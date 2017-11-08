package wjx.classmanager.model;

import cn.bmob.v3.BmobObject;

/**
 * 作者：国富小哥
 * 日期：2017/11/7
 * Created by Administrator
 */

public class Grade extends BmobObject{

    /**
     * 班级id
     */
    private String classId;
    /**
     * 标题
     */
    private String title;
    /**
     * 时间
     */
    private String time;
    /**
     * 内容
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
