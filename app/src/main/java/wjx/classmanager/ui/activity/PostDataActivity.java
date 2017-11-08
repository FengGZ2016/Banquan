package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.util.FileUtils;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.adapter.FilesAdapter;
import wjx.classmanager.presenter.impl.PostDataPresenterImpl;
import wjx.classmanager.utils.FileUtil;
import wjx.classmanager.view.PostDataView;

import static wjx.classmanager.model.Constant.MyClass.CLASS_GROUP_ID;

public class PostDataActivity extends BaseActivity implements PostDataView{

    private ImageView mImageViewBack;
    private TextView mTextViewTitle;
    private ImageView mImageViewAdd;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private FilesAdapter mFilesAdapter;
    private boolean isLoading;
    private boolean hasMoreData;
    private String groupId;
    private EMGroup group;
    private List<EMMucSharedFile> fileList;

    private int pageSize = 10;
    private int pageNum = 1;

    private PostDataPresenterImpl mPostDataPresenter;
    private static final int REQUEST_CODE_SELECT_FILE = 1;
    private ProgressDialog uploadPd;
    private ProgressDialog downloadPd;
    private ProgressDialog deletePd;

    @Override
    protected void init() {
        groupId = getIntent().getStringExtra(CLASS_GROUP_ID);
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        if(group == null){
            finish();
            return;
        }
    }

    @Override
    public void initView() {
        mPostDataPresenter=new PostDataPresenterImpl(this,this);

        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mListView= (ListView) findViewById(R.id.list_view);
        //为ListView注册ContextMenu
        registerForContextMenu(mListView);
        mProgressBar= (ProgressBar) findViewById(R.id.pb_load_more);

        mTextViewTitle.setText("班级资料");
        fileList=new ArrayList<>();
        mFilesAdapter=new FilesAdapter(this,R.layout.item_shared_file,fileList);

        mListView.setAdapter(mFilesAdapter);
    }

    @Override
    public void initListener() {
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加资料
                mPostDataPresenter.postData();

            }
        });

        //子项点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showFile(fileList.get(position));
            }
        });


        //监听listview滑动变化
        /**
         *监听着ListView的滑动状态改变。
         * 官方的有三种状态SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING、SCROLL_STATE_IDLE：
         * SCROLL_STATE_TOUCH_SCROLL：手指正拖着ListView滑动
         * SCROLL_STATE_FLING：ListView正自由滑动
         * SCROLL_STATE_IDLE：ListView滑动后静止
         * */
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lasPos = view.getLastVisiblePosition();
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && hasMoreData
                        && !isLoading
                        && lasPos == fileList.size() -1){
                    showFileList(false);
                }
            }

            /**
             * firstVisibleItem: 表示在屏幕中第一条显示的数据在adapter中的位置
             * visibleItemCount：则表示屏幕中最后一条数据在adapter中的数据，
             * totalItemCount则是在adapter中的总条数
             * */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //监听刷新的事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFileList(true);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("删除此文件");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        deletePd=new ProgressDialog(this);
        deletePd.setMessage("Deleting...");
        deletePd.setCanceledOnTouchOutside(false);
        deletePd.show();

        final int position=((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        mPostDataPresenter.deleteFile(position,groupId,fileList);

        return super.onContextItemSelected(item);
    }

    /**
     * 查看文件
     * @param emMucSharedFile
     */
    private void showFile(EMMucSharedFile emMucSharedFile) {

        //获取文件的本地路径
        final File localFile=new File(PathUtil.getInstance().getFilePath(),emMucSharedFile.getFileName());
        if (localFile.exists()){
            //如果本地文件存在，可以直接打开
            openFile(localFile);
        }else {
            //如果本地文件不存在，就先下载
            mPostDataPresenter.downFile(groupId,emMucSharedFile,localFile);
        }
    }


    /**
     * 打开文件
     * @param localFile
     */
    private void openFile(File localFile) {
        if(localFile != null && localFile.exists()){
            // FileUtils.openFile(localFile, this);
            String fileName=localFile.getName();
            String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
            if ("wps".equals(fileType)||"docx".equals(fileType)){
                startActivity( FileUtil.getWordFileIntent(localFile));
            }
            else if ("jpg".equals(fileType)||"png".equals(fileType)||"gif".equals(fileType)){
                startActivity(FileUtil.getImageFileIntent(localFile));
            }
            else if ("xlsx".equals(fileType)){
                startActivity(FileUtil.getExcelFileIntent(localFile));
            }
            else if ("pptx".equals(fileType)){
                startActivity(FileUtil.getPPTFileIntent(localFile));
            }
            else if ("apk".equals(fileType)){
                startActivity(FileUtil.getApkFileIntent(localFile));
            }
            else if ("chm".equals(fileType)){
                startActivity(FileUtil.getChmFileIntent(localFile));
            }
            else if ("mp4".equals(fileType)){
                startActivity(FileUtil.getVideoFileIntent(localFile));
            }
            else if ("mp3".equals(fileType)){
                startActivity(FileUtil.getAudioFileIntent(localFile));
            }
            else if ("txt".equals(fileType)){
                startActivity(FileUtil.getTextFileIntent(localFile));
            }
            else if ("pdf".equals(fileType)){
                startActivity(FileUtil.getPdfFileIntent(localFile));
            }
            else if ("html".equals(fileType)){
                startActivity(FileUtil.getHtmlFileIntent(localFile));
            }else {
                FileUtils.openFile(localFile, this);
            }

        }



    }


    private void showFileList(boolean isRefresh) {

        isLoading=true;
        if (isRefresh){
            pageNum=1;
            mSwipeRefreshLayout.setRefreshing(true);
        }else {
            pageNum++;
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mPostDataPresenter.getServerFile(groupId,pageNum,pageSize,isRefresh);

    }



    @Override
    public void initData() {
//        groupId= SPUtil.getGroupId(mContext);
//        group= EMClient.getInstance().groupManager().getGroup(groupId);
        showFileList(true);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_post_data;
    }

    /**
     * 成功获取服务器文件数据
     * @param emMucSharedFiles
     * @param isRefresh
     */
    @Override
    public void getServerFileSuccess(List<EMMucSharedFile> emMucSharedFiles, boolean isRefresh) {
        if (isRefresh){
            mSwipeRefreshLayout.setRefreshing(false);

        }else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        isLoading=false;
        if(isRefresh)
            fileList.clear();
        fileList.addAll(emMucSharedFiles);
        if(emMucSharedFiles.size() == pageSize){
            hasMoreData = true;
        }else{
            hasMoreData = false;
        }
        if(mFilesAdapter == null){
            mFilesAdapter=new FilesAdapter(this,R.layout.item_shared_file,fileList);
            mListView.setAdapter(mFilesAdapter);
        }else{
            mFilesAdapter.notifyDataSetChanged();
        }


    }

    /**
     * 获取服务器文件数据失败
     * @param s
     */
    @Override
    public void getServerError(String s) {
        Toast.makeText(mContext, "刷新失败！"+s.toString(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void startActivityForResult(Intent intent) {
        startActivityForResult(intent,REQUEST_CODE_SELECT_FILE);
    }

    @Override
    public void filePathNull() {
        Toast.makeText(this, "only support upload image when android os >= 4.4", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void filePathExists() {
        Toast.makeText(this, "file no exists !", Toast.LENGTH_SHORT).show();
    }

    /**
     * 正在上传
     */
    @Override
    public void onUpload() {
        uploadPd = new ProgressDialog(this);
        uploadPd.setCanceledOnTouchOutside(false);
        uploadPd.setMessage("Uploading...");
        uploadPd.show();
    }

    /**
     * 上传成功
     */
    @Override
    public void uploadSuccess() {
        uploadPd.dismiss();
        Toast.makeText(mContext, "upload success !", Toast.LENGTH_SHORT).show();
        showFileList(true);
    }

    /**
     * 上传失败
     */
    @Override
    public void uploadError(String error) {
        uploadPd.dismiss();
        Toast.makeText(this, "Upload fail, " + error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 正在下载文件
     */
    @Override
    public void onDownLoadFile() {
        downloadPd = new ProgressDialog(this);
        downloadPd.setMessage("Downloading...");
        downloadPd.setCanceledOnTouchOutside(false);
        downloadPd.show();
    }

    /**
     * 下载文件成功
     * @param localFile
     */
    @Override
    public void downloadFileSuccess(File localFile) {
        downloadPd.dismiss();
        openFile(localFile);
    }

    /**
     * 下载文件失败
     */
    @Override
    public void downloadFileError() {
        downloadPd.dismiss();
        Toast.makeText(mContext, "download fail !", Toast.LENGTH_SHORT).show();
    }


    /**
     * 删除成功
     * @param position
     */
    @Override
    public void deleteSuccess(int position) {
        deletePd.dismiss();
        fileList.remove(position);
        mFilesAdapter.notifyDataSetChanged();
        Toast.makeText(mContext, "Delete Success !", Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除失败
     */
    @Override
    public void deleteError() {
        deletePd.dismiss();
        Toast.makeText(mContext, "Delete Fail !", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择上传文件返回的结果
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_SELECT_FILE){
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        //解析uri
                        mPostDataPresenter.uploadFileWithUri(uri);
                    }
                }
            }
        }
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
