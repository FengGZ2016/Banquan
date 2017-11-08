package wjx.classmanager.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Notification;
import wjx.classmanager.presenter.MessagePrestener;
import wjx.classmanager.view.MessageView;

/**
 * Created by wjx on 2017/10/9.
 */

public class MessagePresenterImpl implements MessagePrestener {

    private MessageView mMessageView;
    private List<Notification> mNotifications=new ArrayList<>();
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    public MessagePresenterImpl(MessageView messageView){
        mMessageView =messageView;
    }


    @Override
    public void getBmobData() {
        mMessageView.onGetBmobData();
        BmobQuery<Notification> bmobQuery=new BmobQuery<>();
        //根据班级id查询
        bmobQuery.addWhereEqualTo("classId",mClassUser.getGroupId() );
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Notification>() {
            @Override
            public void done(List<Notification> list, BmobException e) {
                if (e==null){
                    //获取比目数据成功
                    mMessageView.getBmobDataSuccess(list);

                }else {
                    //获取比目数据失败
                    mMessageView.getBmobDataFail();

                }
            }
        });
    }
}
