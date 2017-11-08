package wjx.classmanager.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by wjx on 2017/10/18.
 */

public interface ClassPhotoPresenter {
    void handleAlbumImage(Activity activity, Intent data);
    void handleCameraImage(Activity activity, Uri imageUri);
    void handleSelectorImage(String[] stringExtra);
    void getBmobData();
}
