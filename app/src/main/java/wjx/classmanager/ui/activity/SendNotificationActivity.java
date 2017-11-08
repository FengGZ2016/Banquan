package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import wjx.classmanager.R;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Notification;
import wjx.classmanager.utils.FileUtil;

/**
 * 发布通知
 */
public class SendNotificationActivity extends BaseActivity {


    private ImageView mImageViewBack;
    private TextView mTextViewTitle;
    private ImageView mImageViewAdd;
    private EditText mEditTextNotifyName;
    private EditText mEditTextNotifyDesc;
    private ImageView mImageViewAddFile;
    private TextView mTextViewFilecount;
    private Button mButtonSend;
    private String fileUrl;
    private ProgressDialog sendPd;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);


    @Override
    protected void init() {

    }

    @Override
    public void initView() {

        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mImageViewAdd.setVisibility(View.GONE);
        mTextViewTitle.setText("编辑通知");
        mEditTextNotifyName= (EditText) findViewById(R.id.notify_name);
        mEditTextNotifyDesc= (EditText) findViewById(R.id.notify_desc);
        mImageViewAddFile= (ImageView) findViewById(R.id.notify_add);
        mButtonSend= (Button) findViewById(R.id.notify_send);
        mTextViewFilecount= (TextView) findViewById(R.id.notify_count);

        sendPd = new ProgressDialog(this);
        sendPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        sendPd.setMessage("sending..." + "0%");
        sendPd.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        sendPd.setCancelable(true);// 设置ProgressDialog 是否可以按退回按键取消

    }

    @Override
    public void initListener() {
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /**
         * 添加附件
         */
        mImageViewAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });

        /**
         * 发布通知
         */
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
                Date curDate=new Date(System.currentTimeMillis());
                final String time=format.format(curDate);
                final String notifyName=mEditTextNotifyName.getText().toString();
                final String notifyDesc=mEditTextNotifyDesc.getText().toString();
                if (!TextUtils.isEmpty(notifyName)&&!TextUtils.isEmpty(notifyDesc)){
                    sendPd.show();
                    if (!TextUtils.isEmpty(fileUrl)){

                        //如果有附件
                        //先上传文件路径，上传成功后再把路径存入通知表表
                        final BmobFile bmobFile = new BmobFile(new File(fileUrl));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    //上传附件成功

                                    Notification notification=new Notification();
                                    notification.setClassId(mClassUser.getGroupId());
                                    notification.setTitle(notifyName);
                                    notification.setContent(notifyDesc);
                                    notification.setTime(time);
                                    notification.setFilePath(bmobFile.getUrl());
                                    notification.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e==null){
                                                sendPd.hide();
                                                Toast.makeText(SendNotificationActivity.this, "发布成功..", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else {
                                                sendPd.hide();
                                                Toast.makeText(SendNotificationActivity.this, "发布失败!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            }   else {
                                //上传失败
                                    sendPd.hide();
                                Toast.makeText(mContext, "上传附件失败！", Toast.LENGTH_SHORT).show();
                            }
                            }

                            @Override
                            public void onProgress(Integer value) {
                                super.onProgress(value);
                                sendPd.setMessage("sending..." + value + "%");
                            }
                        });
                    }else {
                        //没有附件,直接把通知标题和内容保存到比目
                        Notification notification=new Notification();
                        notification.setClassId(mClassUser.getGroupId());
                        notification.setTitle(notifyName);
                        notification.setContent(notifyDesc);
                        notification.setTime(time);
                        notification.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    sendPd.hide();
                                    Toast.makeText(SendNotificationActivity.this, "发布成功..", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    sendPd.hide();
                                    Toast.makeText(SendNotificationActivity.this, "发布失败!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                }else {
                    Toast.makeText(mContext, "通知标题和通知内容不能为空！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择上传文件返回的结果
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                if (data != null) {
                    mTextViewFilecount.setText("已添加附件");
                    Uri uri = data.getData();
                    fileUrl= FileUtil.getFilePath(uri,this);
                }
            }
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_send_notification;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
