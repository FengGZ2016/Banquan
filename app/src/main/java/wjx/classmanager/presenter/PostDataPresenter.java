package wjx.classmanager.presenter;

import android.net.Uri;

import com.hyphenate.chat.EMMucSharedFile;

import java.io.File;
import java.util.List;

/**
 * Created by wjx on 2017/10/16.
 */

public interface PostDataPresenter {
    void getServerFile(String groupId, int pageNum, int pageSize, boolean isRefresh);

    void postData();

    void uploadFileWithUri(Uri uri);

    void downFile(String groupId, EMMucSharedFile emMucSharedFile, File localFile);

    void deleteFile(int position, String groupId, List<EMMucSharedFile> fileList);
}
