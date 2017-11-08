package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import wjx.classmanager.R;
import wjx.classmanager.model.AppraiseResult;
import wjx.classmanager.model.Appraising;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.utils.FileUtil;

public class AppraseDetailActivity extends BaseActivity {

    private ImageView mImageViewBack;
    private TextView mTextViewTitle;
    private ImageView mImageViewAdd;

    private EditText mEditTextName;//竞选人
    private EditText mEditTextMotto;//竞选人座右铭
    private TextView mTextViewHeaderCount;//头像数
    private ImageView mImageViewAddHeader;//添加头像
    private Button mButtonAganAdd;//继续添加
    private String iconUri;
    private ProgressDialog savingPd;
    private ProgressDialog finishingPd;

    //评优集
    private String classId;
    private  String title;
    private String author;
    private  String imageUrl;
    private  String introduce;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        getIntentData();
        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mTextViewTitle.setText("添加竞选人");

        mEditTextName= (EditText) findViewById(R.id.next_name);
        mEditTextMotto= (EditText) findViewById(R.id.next_desc);
        mTextViewHeaderCount= (TextView) findViewById(R.id.header_count);
        mImageViewAddHeader= (ImageView) findViewById(R.id.btn_add_header);
        mButtonAganAdd= (Button) findViewById(R.id.next_next);

    }

    private void getIntentData() {
        Intent intent=getIntent();
        classId=intent.getStringExtra("classId");
         title=intent.getStringExtra("title");
         author=intent.getStringExtra("author");
        imageUrl=intent.getStringExtra("image");
         introduce=intent.getStringExtra("introduce");

    }


    @Override
    public void initListener() {
        //返回
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //完成
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSave();
            }
        });

        //添加头像
        mImageViewAddHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });


        //继续添加
        mButtonAganAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingPd=new ProgressDialog(AppraseDetailActivity.this);
                savingPd.setMessage("saving...");
                savingPd.setCanceledOnTouchOutside(false);
                savingPd.show();

                final String name=mEditTextName.getText().toString();
                final String motto=mEditTextMotto.getText().toString();
                if (!TextUtils.isEmpty(iconUri) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(motto)) {

                    final BmobFile bmobFile = new BmobFile(new File(iconUri));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Appraising appraise = new Appraising();
                                appraise.setClassId(mClassUser.getGroupId());
                                appraise.setName(name);
                                appraise.setTitle(title);
                                appraise.setMotto(motto);
                                appraise.setAvatar(bmobFile.getFileUrl());

                                appraise.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            savingPd.dismiss();
                                            showToast("保存成功,请添加下一个");

                                            Intent intent = new Intent(AppraseDetailActivity.this, AppraseDetailActivity.class);
                                            intent.putExtra("classId",classId);
                                            intent.putExtra("title",title);
                                            intent.putExtra("author",author);
                                            intent.putExtra("introduce",introduce);
                                            intent.putExtra("image",imageUrl);
                                            startActivity(intent);
                                            finish();
                                        } else {

                                            savingPd.dismiss();
                                            showToast("保存失败");
                                        }
                                    }
                                });

                            } else {
                                Log.e("AAA", e.getMessage());
                            }
                        }
                    });
                }else {
                    savingPd.dismiss();
                    showToast("所有选项必须都填写！");
                }
            }
        });


    }

    /**
     * 添加评选人完成
     */
    private void finishSave() {

        finishingPd=new ProgressDialog(this);
        finishingPd.setMessage("waiting...");
        finishingPd.setCanceledOnTouchOutside(false);
        finishingPd.show();

        final String name=mEditTextName.getText().toString();
        final String motto=mEditTextMotto.getText().toString();
        if (!TextUtils.isEmpty(iconUri) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(motto)) {

            final BmobFile bmobFile2 = new BmobFile(new File(iconUri));
            bmobFile2.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //上传图片成功
                        Appraising appraise2 = new Appraising();
                        appraise2.setClassId(mClassUser.getGroupId());
                        appraise2.setName(name);
                        appraise2.setTitle(title);
                        appraise2.setMotto(motto);
                        appraise2.setAvatar(bmobFile2.getFileUrl());

                        appraise2.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    execureCreateOperate();
                                } else {
                                    showToast("保存失败");
                                    finishingPd.dismiss();

                                }
                            }
                        });
                    } else {
                        //上传图片失败
                        Log.e("AAA", e.getMessage());
                    }
                }
            });
        }else {
            savingPd.dismiss();
            showToast("所有选项必须都填写！");
        }
    }


    /**
     * 执行创建操作
     * */
    private void execureCreateOperate() {

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate=new Date(System.currentTimeMillis());
        final String time=format.format(curDate);
        final BmobFile bmobFile = new BmobFile(new File(imageUrl));

        //先上传评优活动的背景图
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    //背景图上传成功
                    AppraiseResult result=new AppraiseResult();
                    result.setClassId(mClassUser.getGroupId());
                    result.setNameForCreate(author);
                    result.setTimeForCreate(time);
                    result.setIntroduction(introduce);
                    result.setAppraiseTitle(title);
                    result.setBackgroundImage(bmobFile.getFileUrl());

                    result.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                //保存成功
                                savingPd.dismiss();
                                showToast("创建成功");
                                finish();
                            }else {
                                //保存失败
                                savingPd.dismiss();
                                showToast("创建失败");
                            }
                        }
                    });
                }else {
                    //背景图上传失败
                    Log.e("AAA", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                Uri uri = data.getData();
                //返回头像时，头像数量显示1
               mTextViewHeaderCount.setText("已添加头像");
                iconUri = FileUtil.getPath(AppraseDetailActivity.this, uri);

            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_apprase_detail;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
