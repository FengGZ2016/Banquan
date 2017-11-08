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

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import wjx.classmanager.R;
import wjx.classmanager.model.Vote;
import wjx.classmanager.model.VoteResult;
import wjx.classmanager.utils.FileUtil;

public class VoteDetailActivity extends BaseActivity {

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
    private  String title;
    private String author;
    private  String imageUrl;
    private  String introduce;


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
                savingPd=new ProgressDialog(VoteDetailActivity.this);
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
                            if (e==null){
                                //上传头像成功
                                Vote vote=new Vote();
                                vote.setName(name);
                                vote.setTitle(title);
                                vote.setMotto(motto);
                                vote.setAvatar(bmobFile.getFileUrl());
                                vote.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e==null){
                                            //保存成功
                                            savingPd.dismiss();
                                            showToast("保存成功，添加下一个");
                                            Intent intent=new Intent(VoteDetailActivity.this,VoteDetailActivity.class);
                                            intent.putExtra("title",title);
                                            intent.putExtra("author",author);
                                            intent.putExtra("introduce",introduce);
                                            intent.putExtra("image",imageUrl);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            //保存失败
                                            savingPd.dismiss();
                                            showToast("保存失败");
                                        }

                                    }
                                });
                            }else {
                                savingPd.dismiss();
                                //上传头像失败
                                showToast("");
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
                        Vote vote2=new Vote();
                        vote2.setName(name);
                        vote2.setTitle(title);
                        vote2.setMotto(motto);
                        vote2.setAvatar(bmobFile2.getFileUrl());

                        vote2.save(new SaveListener<String>() {
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

       // BmobUser bmobUser = BmobUser.getCurrentUser();
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
                    VoteResult voteResult=new VoteResult();
                    voteResult.setNameForCreate(author);
                    voteResult.setTimeForCreate(time);
                    voteResult.setIntroduction(introduce);
                    voteResult.setAppraiseTitle(title);
                    voteResult.setBackgroundImage(bmobFile.getFileUrl());

                    voteResult.save(new SaveListener<String>() {
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
                iconUri = FileUtil.getPath(VoteDetailActivity.this, uri);

            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_vote_detail;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
