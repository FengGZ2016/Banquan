package wjx.classmanager.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import wjx.classmanager.presenter.ClassDetailPresenter;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.ClassDetailView;

/**
 * 作者：国富小哥
 * 日期：2017/10/18
 * Created by Administrator
 */

public class ClassDetailPresenterImpl implements ClassDetailPresenter{

    private EMGroup group;
    private String groupName;
    private String groupOwner;
    private String groupDescription;

    private ClassDetailView mClassDetailView;

    public ClassDetailPresenterImpl(ClassDetailView mClassDetailView){
        this.mClassDetailView=mClassDetailView;
    }
    /**
     * 获取班级的创建人和班级简介
     * @param groupid
     */
    @Override
    public void getGroupData(final String groupid) {

        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupid);
                    groupName=group.getGroupName();
                    groupOwner=group.getOwner();
                    groupDescription=group.getDescription();

                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClassDetailView.getGroupDataSuccess(group,groupName,groupOwner,groupDescription);
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClassDetailView.getGroupDataFail();
                        }
                    });
                }

            }
        });
    }

    /**
     * 加入班级
     * @param emGroup
     */
    @Override
    public void addToClass(final EMGroup emGroup) {
        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(emGroup.isMembersOnly()){
                        EMClient.getInstance().groupManager().applyJoinToGroup(emGroup.getGroupId(), "Apply to join");
                    }else{
                        EMClient.getInstance().groupManager().joinGroup(emGroup.getGroupId());
                    }

                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClassDetailView.applySuccess();
                        }
                    });

                }catch (HyphenateException e){
                    e.printStackTrace();
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClassDetailView.applyFail();
                        }
                    });
                }

            }
        });
    }
}
