package wjx.classmanager.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.R;
import wjx.classmanager.adapter.SelectMemberAdapter;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Member;
import wjx.classmanager.utils.ThreadUtil;

public class AddAdminActivity extends BaseActivity {


    private EMGroup group;
    private String groupId;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);
    private EMPushConfigs pushConfigs;
    private RecyclerView mRecyclerView;
    private SelectMemberAdapter mMemberAdapter;
    //成员列表
    List<String> memberList = new ArrayList<>();
    private List<Member> mMembers = new ArrayList<>();

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mRecyclerView= (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mMemberAdapter =new SelectMemberAdapter(this,mMembers);
        mRecyclerView.setAdapter(mMemberAdapter);
    }

    @Override
    public void initListener() {
        mMemberAdapter.setSelectMember(new SelectMemberAdapter.SelectMember() {
            @Override
            public void username(String username) {
                Intent intent=new Intent();
                intent.putExtra("userName",username);
                setResult(1001,intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {

        getBmobData();
    }

    private void getBmobData() {

        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(pushConfigs == null){
                        EMClient.getInstance().pushManager().getPushConfigsFromServer();
                    }

                    //先获取自己所在的群组
                    groupId= mClassUser.getGroupId();
                    if (!groupId.equals("")){
                        group=EMClient.getInstance().groupManager().getGroup(groupId);
                        String groupName = group.getGroupName();
                        String description = group.getDescription();
                        EMCursorResult<String> result = null;
                        do {
                            result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
                                    result != null ? result.getCursor() : "",
                                    20);
                            memberList.addAll(result.getData());
                        } while (result.getCursor() != null && !result.getCursor().isEmpty());

                        for (String member : memberList) {
                            Member mb = new Member(member);
                            mb.setSection(true);
                            mMembers.add(mb);
                        }

                        ThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             refreshData();
                            }
                        });


                    }else {

                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();

                }
            }
        });

    }

    private void refreshData() {
        mMemberAdapter.refresh(mMembers);
    }


    public void back(View view){
        finish();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_add_admin;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }




}
