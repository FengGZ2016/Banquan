package wjx.classmanager.model;

/**
 * Created by wjx on 2017/9/30.
 */

public class Constant { //常量类

    //登录用户已存在
    public static class ErrorCode {
        public static final int USER_ALREADY_EXIST = 202;
        public static final String ERROR_ACTIVITY = "error_activity";

    }

    public static class FragmentType {
        //创建Fragment
        public static final String CREATE_MANAGE = "manage_fragment";
        public static final String CREATE_MESSAGE = "message_fragment";
        public static final String CREATE_NOTIFY = "notify_fragment";

        //显示Fragment
        public static final int FRAGMENT_MSG = 0;
        public static final int FRAGMENT_NOTIFY = 1;
        public static final int FRAGMENT_MANAGE = 2;
        public static final int FRAGMENT_COUNT = 3;
    }

    public static class MessageType {
        public static final int JOIN_CLASS = 4;       //加入班级
        public static final int QUIT_CLASS = 5;       //退出班级
        public static final int CREATE_CLASS = 6;     //创建班级
        public static final int DIABAND_CLASS = 7;    //解散班级
        public static final int EVALUATE = 8;         //推优评优
        public static final int ACTIVITY_VOTE = 9;    //活动投票
        public static final int POST_DATA = 10;        //上传资料
        public static final int PERSON_INFO = 11;      //个人信息
        public static final int PHOTO_CLASS = 12;     //班级相册
    }

    public static class Receiver {
        public static final String ACTION = "action";
        public static final String RECEIVE = "receive";

    }

    public static class MyClass {
        public static final String CLASS_GROUP_ID = "class_group_id";
        public static final String CLASS_EDIT = "class_edit";
        public static final String PHOTO_SELECT = "photo_select";
        public static final String SCAN_RESULT = "scan_result";
        public static final int CLASS_NAME_CODE = 1001;
        public static final int CLASS_DESC_CODE = 1002;
        public static final int CLASS_OPEN_CAMERA = 1003;
        public static final int CLASS_ALBUM = 1004;
        public static final int OPEN_ALBUM = 1005;
        public static final int OPEN_CAMERA = 1006;
        public static final int OPEN_STORAGE = 1007;
        public static final int PHOTO_SELECTER = 1008;
    }


    public static class SharePreference {
        public static final String BMOB = "bmob";
        public static final String HUAN_XIN = "huan_xin";
        public static final String CURR_USER = "curr_user";
        public static final String H_GROUP_ID = "groupid";
        public static final String B_OBJECT_ID = "objectd";
        public static final String CACHE_LIST = "cache_list";

    }
}
