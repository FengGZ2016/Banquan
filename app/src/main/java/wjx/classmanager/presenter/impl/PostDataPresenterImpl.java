package wjx.classmanager.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;

import java.io.File;
import java.util.List;

import wjx.classmanager.presenter.PostDataPresenter;
import wjx.classmanager.utils.FileUtil;
import wjx.classmanager.utils.SPUtil;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.PostDataView;

import static wjx.classmanager.utils.ThreadUtil.runOnUiThread;

/**
 * Created by wjx on 2017/10/16.
 */

public class PostDataPresenterImpl implements PostDataPresenter {

    private PostDataView mPostDataView;
    private Context mContext;


    public PostDataPresenterImpl(PostDataView postDataView, Context context) {
        mPostDataView = postDataView;
        mContext = context;
    }

    @Override
    public void getServerFile(String groupId, int pageNum, int pageSize, final boolean isRefresh) {
        EMClient.getInstance().groupManager().asyncFetchGroupSharedFileList(groupId, pageNum, pageSize, new EMValueCallBack<List<EMMucSharedFile>>() {
            @Override
            public void onSuccess(final List<EMMucSharedFile> emMucSharedFiles) {
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.getServerFileSuccess(emMucSharedFiles, isRefresh);
                    }
                });

            }

            @Override
            public void onError(int i, final String s) {
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.getServerError(s);
                    }
                });
            }
        });
    }

    /**
     * 上传资料
     */
    @Override
    public void postData() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            // intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        }
        mPostDataView.startActivityForResult(intent);

    }

    /**
     * 上传文件
     *
     * @param uri
     */
    @Override
    public void uploadFileWithUri(Uri uri) {
        String filePath = FileUtil.getFilePath(uri,mContext);
        if (filePath == null) {
            mPostDataView.filePathNull();
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            mPostDataView.filePathExists();
            return;
        }
        mPostDataView.onUpload();

        String groupId = SPUtil.getGroupId(mContext);
        EMClient.getInstance().groupManager().asyncUploadGroupSharedFile(groupId, filePath, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.uploadSuccess();
                    }
                });
            }

            @Override
            public void onError(int code, final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.uploadError(error);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    /**
     * 下载文件
     *
     * @param groupId
     * @param emMucSharedFile
     * @param localFile
     */
    @Override
    public void downFile(String groupId, EMMucSharedFile emMucSharedFile, final File localFile) {
        mPostDataView.onDownLoadFile();
        EMClient.getInstance().groupManager().asyncDownloadGroupSharedFile(groupId, emMucSharedFile.getFileId(), localFile.getAbsolutePath(), new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.downloadFileSuccess(localFile);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.downloadFileError();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    /**
     * 删除文件
     *
     * @param position
     * @param groupId
     * @param fileList
     */
    @Override
    public void deleteFile(final int position, String groupId, List<EMMucSharedFile> fileList) {
        EMClient.getInstance().groupManager().asyncDeleteGroupSharedFile(groupId, fileList.get(position).getFileId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.deleteSuccess(position);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPostDataView.deleteError();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}
