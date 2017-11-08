package wjx.classmanager.utils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import wjx.classmanager.model.ClassUser;

/**
 * 作者：国富小哥
 * 日期：2017/10/14
 * Created by Administrator
 */

public class GroupUtil {
    //群组列表
    public static List<EMGroup> grouplist;
    private static String groupId;

    /**
     * 获取groupId
     * @return
     */
    public static String getGroupId(){

        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        for (EMGroup group:grouplist){
                        groupId=group.getGroupId();
                    }
        if (groupId!=null){
            return groupId;
        }else {
            return "";
        }
    }


    public static String getClassId(){
        BmobQuery<ClassUser> bmobQuery=new BmobQuery<>();
        BmobUser bmobUser = BmobUser.getCurrentUser(ClassUser.class);
        bmobQuery.getObject(bmobUser.getObjectId(), new QueryListener<ClassUser>() {
            @Override
            public void done(ClassUser classUser, BmobException e) {
                if (e==null){
                    //查询成功
                    groupId=classUser.getGroupId();
                }else {
                    groupId=null;
                }
            }
        });
        return groupId;
    }


}
