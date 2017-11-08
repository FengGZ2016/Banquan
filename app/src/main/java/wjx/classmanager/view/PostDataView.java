package wjx.classmanager.view;

import android.content.Intent;

import com.hyphenate.chat.EMMucSharedFile;

import java.io.File;
import java.util.List;

/**
 * Created by wjx on 2017/10/16.
 */

public interface PostDataView {

    void getServerFileSuccess(List<EMMucSharedFile> emMucSharedFiles, boolean isRefresh);

    void getServerError(String s);

    void startActivityForResult(Intent intent);

    void filePathNull();

    void filePathExists();

    void onUpload();

    void uploadSuccess();

    void uploadError(String error);

    void onDownLoadFile();

    void downloadFileSuccess(File localFile);

    void downloadFileError();

    void deleteSuccess(int position);

    void deleteError();
}
