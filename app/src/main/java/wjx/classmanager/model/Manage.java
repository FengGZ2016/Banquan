package wjx.classmanager.model;

/**
 * Created by wjx on 2017/9/18.
 */

public class Manage {
    private int icon;

    public Manage(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    private String title;

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
}
