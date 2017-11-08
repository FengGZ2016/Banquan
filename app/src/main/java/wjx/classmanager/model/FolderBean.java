package wjx.classmanager.model;

/**
 * Created by wjx on 2017/8/11.
 */

public class FolderBean {
    //当前文件夹的路径
    private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;

        int lastIndexOf =this.dir.lastIndexOf("/");
        this.name =this.dir.substring(lastIndexOf);
    }

    public String getFirstPath() {
        return firstPath;
    }

    public void setFirstPath(String firstPath) {
        this.firstPath = firstPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String firstPath;
    private String name;
    private int count;
}
