package wjx.classmanager.presenter.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.R;
import wjx.classmanager.application.MyApplication;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Manage;
import wjx.classmanager.presenter.ManagePresenter;
import wjx.classmanager.ui.activity.EvaluateActivity;
import wjx.classmanager.ui.activity.PostDataActivity;
import wjx.classmanager.ui.activity.SendGradeActivity;
import wjx.classmanager.ui.activity.SendNotificationActivity;
import wjx.classmanager.ui.activity.VoteActivity;
import wjx.classmanager.utils.SPUtil;
import wjx.classmanager.view.ManageView;

import static wjx.classmanager.model.Constant.SharePreference.CACHE_LIST;

/**
 * Created by wjx on 2017/10/9.
 */

public class ManagePresenterImpl implements ManagePresenter {

    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    private String[] mFunctionTitle = {"发布通知", "公布成绩", "资料收集", "活动投票",
            "推优评优", "班级管理", "班级考勤", "更多管理"};

    private int[] mFunctionIcon = {R.drawable.manage_publish, R.drawable.manage_score,
            R.drawable.manage_collect, R.drawable.manage_vote, R.drawable.manage_evaluate,
            R.drawable.manage_class, R.drawable.manage_work, R.drawable.manage_more
    };

    private ManageView mManageView;
    private List<Manage> mManages = new ArrayList<>();

    public ManagePresenterImpl(ManageView manageView) {
        mManageView = manageView;
        initData();
    }

    private void initData() {
        if (!SPUtil.getCache(MyApplication.getMyContext(), CACHE_LIST).equals("")) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Manage>>() {
            }.getType();
            mManages = gson.fromJson(SPUtil.getCache(MyApplication.getMyContext(), CACHE_LIST), type);
        } else {
            for (int i = 0; i < mFunctionTitle.length; i++) {
                mManages.add(new Manage(mFunctionIcon[i], mFunctionTitle[i]));
            }
        }
    }

    public List<Manage> getManageList() {
        return mManages;
    }

    @Override
    public void checkItemType(String title) {
        if(title.equals("发布通知")){
            if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
                mManageView.start(SendNotificationActivity.class);
            }else {
                mManageView.noClass();
            }

        }else if(title.equals("活动投票")){
            if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
                mManageView.start(VoteActivity.class);
            }else {
                mManageView.noClass();
            }

        }else if(title.equals("推优评优")){
            if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
                mManageView.start(EvaluateActivity.class);
            }else {
                mManageView.noClass();
            }


        }else if (title.equals("公布成绩")){

            if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
                mManageView.start(SendGradeActivity.class);
            }else {
                mManageView.noClass();
            }

        }else if (title.equals("资料收集")){
            mManageView.start(PostDataActivity.class);
//            if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
//                mManageView.start(PostDataActivity.class);
//            }else {
//                mManageView.noClass();
//            }

        }else if (title.equals("班级管理")){
//            if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
//                mManageView.start(MyClassActivity.class);
//            }else {
//                mManageView.noClass();
//            }

        }
    }

}
