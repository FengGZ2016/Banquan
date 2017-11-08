package wjx.classmanager.model;

import java.io.Serializable;

/**
 * Created by wjx on 2017/10/3.
 */

public class Message implements Serializable{
    private int icon;
    private String title;
    private String time;

    public Message(){}

    public Message(int icon,String title,String time,int type){
        this.icon=icon;
        this.title=title;
        this.time=time;
        this.type=type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count=1;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
