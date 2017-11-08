package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.util.FileUtils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import wjx.classmanager.R;
import wjx.classmanager.model.Notification;
import wjx.classmanager.utils.FileUtil;

public class ShowNotifyActivity extends BaseActivity {


    private String title;
    private String time;
    private String content;
    private String file;
    
    private TextView mTextViewTitle;
    private TextView mTextViewTime;
    private TextView mTextViewContent;
    private TextView mTextViewFile;
    
    private ImageView mImageViewBack;
    private ImageView mImageViewAdd;
    private TextView barTitle;
    private ProgressDialog downloadPd;
    
    
    @Override
    protected void init() {
        Notification notification= (Notification) getIntent().getSerializableExtra("notify");
        title=notification.getTitle();
        time=notification.getTime();
        content=notification.getContent();
        file=notification.getFilePath();
    }

    @Override
    public void initView() {
        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mImageViewAdd.setVisibility(View.GONE);
        barTitle= (TextView) findViewById(R.id.back_title);
        barTitle.setText("班级通知");
        mTextViewTitle= (TextView) findViewById(R.id.text_title);
        mTextViewTime= (TextView) findViewById(R.id.text_time);
        mTextViewContent= (TextView) findViewById(R.id.text_content);
        mTextViewFile= (TextView) findViewById(R.id.text_file);

        mTextViewTitle.setText(title);
        mTextViewTime.setText(time);
        mTextViewContent.setText(content);

        downloadPd=new ProgressDialog(this);
        downloadPd.setMessage("downloading...");
        downloadPd.setCanceledOnTouchOutside(false);

         
    }

    @Override
    public void initListener() {
        mTextViewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(file)){

                    BmobFile bmobFile=new BmobFile(file,"",file);
                    File saveFile = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
                    if (saveFile.exists()){
                        //如果本地文件存在，可以直接打开
                        openFile(saveFile);
                    }else {
                        //如果本地文件不存在，就先下载
                        downloadFile(bmobFile,saveFile);
                    }
                }else {
                    Toast.makeText(mContext, "没有附件", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_show_notify;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }


    /**
     * 下载附件
     * @param file
     */
    private void downloadFile(BmobFile file, File saveFile){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                downloadPd.show();
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    downloadPd.dismiss();
                    Toast.makeText(ShowNotifyActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                }else{
                   downloadPd.dismiss();
                    Toast.makeText(ShowNotifyActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }
}
