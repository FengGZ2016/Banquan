package wjx.classmanager.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.model.JoinClass;
import wjx.classmanager.presenter.JoinClassPresenter;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.JoinClassView;

/**
 * Created by wjx on 2017/10/14.
 */

public class JoinClassPresenterImpl implements JoinClassPresenter {

    private List<JoinClass> mJoinClasses = new ArrayList<>();
    private JoinClassView mJoinClassView;

    public JoinClassPresenterImpl(JoinClassView joinClassView){
        mJoinClassView=joinClassView;

    }



    /**
     * 从服务器上拿到班级列表
     * @param pagesize
     * @param cursor
     */
    @Override
    public void getServerData(final int pagesize, final String cursor) {

            ThreadUtil.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    try {
                    final EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(pagesize, cursor);
                    final List<EMGroupInfo> returnGroups = result.getData();

                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mJoinClassView.getServerDataSuccess(result,returnGroups);
                        }
                    });

                }catch (HyphenateException e){
                    e.printStackTrace();
                        ThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mJoinClassView.getServerDataFail();
                            }
                        });
                }
                }
            });

    }

}
