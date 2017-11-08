package wjx.classmanager.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import wjx.classmanager.model.BmobClass;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.presenter.CreateClassPresenter;
import wjx.classmanager.utils.SPUtil;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.CreateClassView;

/**
 * Created by wjx on 2017/9/30.
 */

public class CreateClassPresenterImpl implements CreateClassPresenter {

    private CreateClassView mCreateClassView;
    protected List<EMGroup> grouplist = new ArrayList<>();
    private Context mContext;

    public CreateClassPresenterImpl(CreateClassView createClassView,Context context){
        mCreateClassView =createClassView;
        mContext=context;
    }

    @Override
    public void createClass(String className, String classDesc, boolean isPublic, boolean isMemberInviter) {
        if (TextUtils.isEmpty(className)) {
            mCreateClassView.classNameErroe();
        } else {
            //通知View层正在创建班级
            mCreateClassView.onCreateClass();
            startCreate(className,classDesc,isPublic,isMemberInviter);
        }
    }
    private void startCreate(final String className, final String classDesc, final boolean isPublic, final boolean isMemberInviter) {

        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {

                try {
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    option.inviteNeedConfirm = true;
                    String[] members=new String[]{};
                    String reason = "Invite to join the class";
                    reason  = EMClient.getInstance().getCurrentUser() + reason + className;

                    if(isPublic){
                        if (isMemberInviter){
                            option.style= EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                        }else {
                            option.style= EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                        }
                    }else{
                        if (isMemberInviter){
                            option.style= EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                        }else {
                            option.style= EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                        }
                    }

                    //在环信创建群组
                    EMClient.getInstance().groupManager().createGroup(className, classDesc, members, reason, option);
                    //在Bmob创建班级表
                    createTableInBmob(className,classDesc, BmobUser.getCurrentUser().getUsername());
                } catch (HyphenateException e) {
                    //通知view创建班级失败
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCreateClassView.onCreateFailed("创建群组失败");
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取环信群组的ID
     * @param classname
     * @return
     */
    private String huanXinGroupId(String classname){
        try {
            grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        grouplist = EMClient.getInstance().groupManager().getAllGroups();

        for (EMGroup group:grouplist){
            if (group.getGroupName().equals(classname)){
                return group.getGroupId();
            }
        }
        return "";
    }

    /**
     * 保存班级数据到比目
     * @param classname
     * @param classdesc
     * @param currusername
     */
    private void createTableInBmob(final String classname, String classdesc, String currusername) {
        final String groupId=huanXinGroupId(classname);
        //缓存在本地
        SPUtil.huanXinGroupId(mContext,groupId);

        BmobClass bmobClass = new BmobClass();
        //班级名称
        bmobClass.setClassName(classname);
        //班级id
        bmobClass.setGroupId(groupId);
        //班级简介
        bmobClass.setClassDescription(classdesc);
        //班级创建人
        bmobClass.setGroupOwner(currusername);
        bmobClass.save(new SaveListener<String>() {
            @Override
            public void done(final String objectId, BmobException e) {
                if(e==null){
                    final  ClassUser user=new ClassUser();;//时间过长，需要旋转框提示等待
                    final BmobUser bmobUser = BmobUser.getCurrentUser(ClassUser.class);

                    user.setGroupId(groupId);
                    user.setGroupName(classname);
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                //更新成功
                                ThreadUtil.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e( "run: ", "Bomb创建班级表成功");
                                        mCreateClassView.sendBroadcast();
                                        mCreateClassView.onCreateSuccess(groupId,objectId);
                                    }
                                });
                            }else {
                                //更新失败
                            }
                        }
                    });

                }else{
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCreateClassView.onCreateFailed("Bomb创建班级表失败");
                        }
                    });
                }
            }
        });
    }
}
