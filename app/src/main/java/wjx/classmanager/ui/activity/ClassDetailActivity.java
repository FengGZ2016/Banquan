package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;

import wjx.classmanager.R;
import wjx.classmanager.presenter.ClassDetailPresenter;
import wjx.classmanager.presenter.impl.ClassDetailPresenterImpl;
import wjx.classmanager.view.ClassDetailView;


public class ClassDetailActivity extends BaseActivity implements ClassDetailView{

    private LinearLayout mLinearLayout_back;
    private Button btn_add_group;
    private TextView tv_admin;
    private TextView tv_name;
    private TextView tv_introduction;
    private String groupid;
    private ProgressBar progressBar;
    private ClassDetailPresenter mClassDetailPresenter;
    private EMGroup mEMGroup;
    private ProgressDialog pd;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mClassDetailPresenter=new ClassDetailPresenterImpl(this);
        mLinearLayout_back= (LinearLayout) findViewById(R.id.linear_layout_back);
        tv_name = (TextView) findViewById(R.id.name);
        tv_admin = (TextView) findViewById(R.id.tv_admin);
        btn_add_group = (Button) findViewById(R.id.btn_add_to_group);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        progressBar = (ProgressBar) findViewById(R.id.loading);
    }

    @Override
    public void initListener() {

        mLinearLayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToClass();
            }
        });
    }

    /**
     * 申请加入班级
     */
    private void addToClass() {
        if (mEMGroup!=null){

            pd=new ProgressDialog(this);
            pd.setMessage("Sending the request...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            mClassDetailPresenter.addToClass(mEMGroup);

        }else {
            Toast.makeText(mContext, "join fail , get group error !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initData() {
        EMGroupInfo groupInfo= (EMGroupInfo) getIntent().getSerializableExtra("groupinfo");
        String groupName=null;
        if (groupInfo!=null){
            groupName=groupInfo.getGroupName();
            groupid=groupInfo.getGroupId();

            tv_name.setText(groupName);
            mClassDetailPresenter.getGroupData(groupid);

        }else {
            mEMGroup = ClassSeachActivity.searchedGroup;
            if(mEMGroup == null){
                return;
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                if (!mEMGroup.getMembers().contains(EMClient.getInstance().getCurrentUser())) {
                    btn_add_group.setEnabled(true);
                }

                groupName = mEMGroup.getGroupName();
                groupid = mEMGroup.getGroupId();

                tv_name.setText(groupName);
                tv_admin.setText(mEMGroup.getOwner());
                tv_introduction.setText(mEMGroup.getDescription());
            }

        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_class_detail;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }


    @Override
    public void getGroupDataSuccess(EMGroup group, String groupName, String groupOwner, String groupDescription) {
        mEMGroup=group;
        progressBar.setVisibility(View.INVISIBLE);
        if (!group.getMembers().contains(EMClient.getInstance().getCurrentUser())) {
            btn_add_group.setEnabled(true);
        }
        tv_name.setText(groupName);
        tv_admin.setText(groupOwner);
        tv_introduction.setText(groupDescription);

    }

    @Override
    public void getGroupDataFail() {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(mContext, "get group error !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void applyFail() {
        pd.dismiss();
        Toast.makeText(mContext, "Failed to join the group", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void applySuccess() {
        pd.dismiss();
        if (mEMGroup.isMemberOnly()){
            Toast.makeText(mContext, "The request was sent and waiting for owner approval", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "Join the group", Toast.LENGTH_SHORT).show();
            btn_add_group.setEnabled(false);
        }

    }

}
