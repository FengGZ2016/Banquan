package wjx.classmanager.view;

import java.util.List;

import wjx.classmanager.model.ClassPhoto;

/**
 * Created by wjx on 2017/10/18.
 */

public interface ClassPhotoView {

    void onPicPostFailed();

    void onPicPostSuccess();

    void onStartLoadPic();

    void getBmobDataFail();

    void getBmobDataSuccess(List<ClassPhoto> list);
}
