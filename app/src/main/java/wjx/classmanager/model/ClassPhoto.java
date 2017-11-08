package wjx.classmanager.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 作者：国富小哥
 * 日期：2017/11/1
 * Created by Administrator
 */

public class ClassPhoto extends BmobObject{

    private String photoPath;
    private List<String> photoPaths;
    private String classId;
    private String sourse;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }
}
