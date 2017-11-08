package wjx.classmanager.presenter.impl;

import android.content.Context;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMPushConfigs;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Member;
import wjx.classmanager.presenter.MemberPresenter;
import wjx.classmanager.view.MemberView;

/**
 * Created by wjx on 2017/10/9.
 */

public class MemberPresenterImpl implements MemberPresenter {

    private MemberView mMemberView;
    //群组
    private EMGroup group;
    private String groupId;
    //成员列表
    List<String> memberList = new ArrayList<>();
    private EMPushConfigs pushConfigs;
    private List<Member> mMembers = new ArrayList<>();
    private Context mContext;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    public MemberPresenterImpl(MemberView memberView,Context context){
        mMemberView = memberView;
        mContext=context;
    }

    /**
     * 获取服务器数据
     */
    @Override
    public void getServerData() {

        //先获取自己所在的群组
        groupId= mClassUser.getGroupId();
        if (!"".equals(groupId)){
            //查询
            BmobQuery<ClassUser> query = new BmobQuery<ClassUser>();
            //查询playerName叫“比目”的数据
            query.addWhereEqualTo("groupId", groupId);
            //返回50条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(50);
            //执行查询方法
            query.findObjects(new FindListener<ClassUser>() {
                @Override
                public void done(List<ClassUser> list, BmobException e) {
                    if (e==null){
                        mMemberView.getServerDataSuccess(list);
                    }else {
                        mMemberView.getServerDataFail();
                    }
                }
            });


        }else {
            mMemberView.noClass();
        }

    }
}
