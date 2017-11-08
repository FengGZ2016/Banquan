package wjx.classmanager.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import wjx.classmanager.R;
import wjx.classmanager.model.ClassUser;

public class EdtPersonActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mImageBack;
    private ImageView mImageFinish;
    private TextView mTextTitle;
    private EditText mEditSchool;
    private EditText mEditDepartment;
    private EditText mEditProfess;
    private EditText mEditName;
    private EditText mEditPhone;
    private EditText mEditEmail;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);



    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mImageBack = (ImageView) findViewById(R.id.back_image);
        mImageFinish = (ImageView) findViewById(R.id.back_add);
        mTextTitle = (TextView) findViewById(R.id.back_title);
        mEditSchool = (EditText) findViewById(R.id.edt_school);
        mEditDepartment = (EditText) findViewById(R.id.edt_department);
        mEditProfess = (EditText) findViewById(R.id.edt_profess);
        mEditName = (EditText) findViewById(R.id.edt_name);
        mEditPhone = (EditText) findViewById(R.id.edt_phone);
        mEditEmail = (EditText) findViewById(R.id.edt_email);
    }

    @Override
    public void initListener() {
        mImageBack.setOnClickListener(this);
        mImageFinish.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mTextTitle.setText("个人信息编辑");

        Intent intent = getIntent();
        if (intent != null) {
            mEditSchool.setText(intent.getStringExtra("school"));
            mEditDepartment.setText(intent.getStringExtra("department"));
            mEditProfess.setText(intent.getStringExtra("profess"));
            mEditName.setText(intent.getStringExtra("name"));
            mEditPhone.setText(intent.getStringExtra("phone"));
            mEditEmail.setText(intent.getStringExtra("email"));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_edt_person;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.back_add:
                infoFinishEdit();
                break;

        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 保存个人信息
     */
    private void infoFinishEdit() {

        String school=mEditSchool.getText().toString().trim();
        String department=mEditDepartment.getText().toString().trim();
        String profess=mEditProfess.getText().toString().trim();
        String name=mEditName.getText().toString().trim();
        String phone=mEditPhone.getText().toString().trim();
        String email=mEditEmail.getText().toString().trim();
        ClassUser classUser=new ClassUser();
        BmobUser bmobUser=BmobUser.getCurrentUser(ClassUser.class);
        if (school.length()!=0){
            classUser.setSchool(school);
        }
        if (department.length()!=0){
            classUser.setDepartment(department);
        }
        if (profess.length()!=0){
          classUser.setMajor(profess);
        }
        if (name.length()!=0){
            classUser.setRealName(name);
        }
        if (phone.length()!=0){
            classUser.setMobile(phone);
        }
        if (email.length()!=0){
            classUser.setEmail(email);
        }

        classUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(mContext, "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /**
     * 将图片转化为2进制数据
     * @param bm
     * @return
     */
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }


}
