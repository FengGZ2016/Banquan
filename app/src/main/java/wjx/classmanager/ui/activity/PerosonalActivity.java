package wjx.classmanager.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import wjx.classmanager.R;
import wjx.classmanager.application.MyApplication;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.utils.FileUtil;

public class PerosonalActivity extends BaseActivity {

    private ImageView mImageViewBack;
    private ImageView mImageViewAdd;
    private TextView mTextViewTitle;
    private CircleImageView mCircleImageViewIcon;
    private TextView mTextViewSchool;
    private TextView mTextViewDepartment;
    private TextView mTextViewProfess;
    private TextView mTextViewName;
    private TextView mTextViewClass;
    private TextView mTextViewPhone;
    private TextView mTextViewEmail;
    private FloatingActionButton mFloatingActionButton;

    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);
    private String iconUri;
    private String objectId;



    @Override
    protected void init() {
         objectId=getIntent().getStringExtra("objectId");
    }

    @Override
    public void initView() {
        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
        mCircleImageViewIcon= (CircleImageView) findViewById(R.id.icon_image);
        mTextViewSchool= (TextView) findViewById(R.id.text_school);
        mTextViewDepartment= (TextView) findViewById(R.id.text_department);
        mTextViewClass= (TextView) findViewById(R.id.text_class);
        mTextViewProfess= (TextView) findViewById(R.id.text_profess);
        mTextViewName= (TextView) findViewById(R.id.text_name);
        mTextViewPhone= (TextView) findViewById(R.id.text_phone);
        mTextViewEmail= (TextView) findViewById(R.id.text_email);
        mFloatingActionButton= (FloatingActionButton) findViewById(R.id.person_fab);
        if (objectId!=null){
            mFloatingActionButton.setVisibility(View.GONE);
        }

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

            }
        });

        mCircleImageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objectId==null){
                    selectIcon();
                }
            }
        });


        /**
         * 编辑
         */
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerosonalActivity.this,EdtPersonActivity.class);
                intent.putExtra("school",mTextViewSchool.getText().toString());
                intent.putExtra("department",mTextViewDepartment.getText().toString());
                intent.putExtra("profess",mTextViewProfess.getText().toString());
                intent.putExtra("name",mTextViewName.getText().toString());
                intent.putExtra("phone",mTextViewPhone.getText().toString());
                intent.putExtra("email",mTextViewEmail.getText().toString());
                startActivity(intent);
            }
        });

    }

    /**
     * 选择头像
     */
    private void selectIcon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                //返回头像时，头像数量显示1
                Uri uri=data.getData();
                iconUri = FileUtil.getPath(PerosonalActivity.this, uri);
                final BmobFile bmobFile = new BmobFile(new File(iconUri));
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            ClassUser classUser=new ClassUser();
                            classUser.setHeadImgPath(bmobFile.getFileUrl());
                            classUser.update(mClassUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Glide.with(MyApplication.getMyContext())
                                                .load(bmobFile.getFileUrl())
                                                .into(mCircleImageViewIcon);
                                    }else {
                                        Toast.makeText(mContext, "更换头像失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(mContext, "更换头像失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void initData() {
        if (objectId!=null){
            //显示其他成员的个人信息
            getOtherUserData();

        }else {
            mTextViewTitle.setText(mClassUser.getUsername());
            //显示自己的个人信息
            if (!"".equals(mClassUser.getHeadImgPath())){
                Glide.with(MyApplication.getMyContext())
                        .load(mClassUser.getHeadImgPath())
                        .into(mCircleImageViewIcon);
            }

            if (!"".equals(mClassUser.getSchool())){
                mTextViewSchool.setText(mClassUser.getSchool());
            }
            if (!"".equals(mClassUser.getDepartment())){
                mTextViewDepartment.setText(mClassUser.getDepartment());
            }
            if (!"".equals(mClassUser.getGroupName())){
                mTextViewClass.setText(mClassUser.getGroupName());
            }
            if (!"".equals(mClassUser.getMajor())){
                mTextViewProfess.setText(mClassUser.getMajor());
            }
            if (!"".equals(mClassUser.getRealName())){
                mTextViewName.setText(mClassUser.getRealName());
            }
            if (!"".equals(mClassUser.getEmail())){
                mTextViewEmail.setText(mClassUser.getEmail());
            }
            if (!"".equals(mClassUser.getMobile())){
                mTextViewPhone.setText(mClassUser.getMobile());
            }
        }


    }

    private void getOtherUserData() {
        BmobQuery<ClassUser> query = new BmobQuery<ClassUser>();
        query.getObject(objectId, new QueryListener<ClassUser>() {
            @Override
            public void done(ClassUser classUser, BmobException e) {
                if (e==null){
                    updateUI(classUser);
                }else {
                    Toast.makeText(mContext, "获取成员信息失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 刷新ui
     * @param classUser
     */
    private void updateUI(ClassUser classUser) {

        mTextViewTitle.setText(classUser.getUsername());
        if (!"".equals(classUser.getHeadImgPath())){
            Glide.with(MyApplication.getMyContext())
                    .load(classUser.getHeadImgPath())
                    .into(mCircleImageViewIcon);
        }

        if (!"".equals(classUser.getSchool())){
            mTextViewSchool.setText(classUser.getSchool());
        }
        if (!"".equals(classUser.getDepartment())){
            mTextViewDepartment.setText(classUser.getDepartment());
        }
        if (!"".equals(classUser.getGroupName())){
            mTextViewClass.setText(classUser.getGroupName());
        }
        if (!"".equals(classUser.getMajor())){
            mTextViewProfess.setText(classUser.getMajor());
        }
        if (!"".equals(classUser.getRealName())){
            mTextViewName.setText(classUser.getRealName());
        }
        if (!"".equals(classUser.getEmail())){
            mTextViewEmail.setText(classUser.getEmail());
        }
        if (!"".equals(classUser.getMobile())){
            mTextViewPhone.setText(classUser.getMobile());
        }

    }



    @Override
    public int getLayout() {
        return R.layout.activity_perosonal;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
