package wjx.classmanager.model;

/**
 * Created by wjx on 2017/10/14.
 */

public class JoinClass {
    private String className;

    public JoinClass(){

    }

    public JoinClass(String name){
        className=name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
