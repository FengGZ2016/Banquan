package wjx.classmanager.application;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;

/**
 * Created by wjx on 2017/9/20.
 */

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance=this;

        //init demo helper
        DemoHelper.getInstance().init(context);

        LitePal.initialize(context);
        Bmob.initialize(this, "7643aa32d4bd2af39bf4620ac4680b1b");

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

    }

    public static Context getMyContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
