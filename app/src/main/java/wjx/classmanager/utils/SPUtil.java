package wjx.classmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static wjx.classmanager.model.Constant.SharePreference.BMOB;
import static wjx.classmanager.model.Constant.SharePreference.HUAN_XIN;

/**
 * Created by wjx on 2017/8/1.
 */

public class SPUtil {
    private static final String KEY="first";

    /**
     * @param context 获取导航页上下文
     * @return 是否第一次使用应用
     */
    public static boolean getFirstRunTag(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean hasTag = sp.getBoolean(KEY, true);
        return hasTag;
    }

    /**
     * 第一次使用后添加使用标记
     *
     * @param context
     * @param isFirst
     */
    public static void addFirstRunTag(Context context,boolean isFirst) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY, isFirst);
        editor.commit();
    }

    /**
     * 存放环信字段
     * @param context
     * @param groupId
     */
    public static void huanXinGroupId(Context context,String groupId){
        SharedPreferences sp = context.getSharedPreferences(HUAN_XIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(HUAN_XIN, groupId);
        editor.commit();
    }

    /**
     * 获取群组Id
     * @param context
     * @return
     */
    public static String getGroupId(Context context){
        SharedPreferences sp = context.getSharedPreferences(HUAN_XIN, Context.MODE_PRIVATE);
        return sp.getString(HUAN_XIN,"");
    }

    /**
     * 存放比目字段
     * @param context
     * @param objectId
     */
    public static void bmobObjectId(Context context,String objectId){
        SharedPreferences sp = context.getSharedPreferences(BMOB, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(BMOB, objectId);
        editor.commit();
    }

    /**
     * 获取班级表的Id
     * @param context
     * @return
     */
    public static String getObjectId(Context context){
        SharedPreferences sp = context.getSharedPreferences(BMOB, Context.MODE_PRIVATE);
        return sp.getString(BMOB,"");
    }

    /**
     * 清理环信缓存字段
     * @param context
     */
    public static void clearHuanXin(Context context){
        SharedPreferences sp = context.getSharedPreferences(HUAN_XIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清理Bmob缓存字段
     * @param context
     */
    public static void clearBmob(Context context){
        SharedPreferences sp = context.getSharedPreferences(BMOB, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 缓存当前用户名
     * @param context
     * @param user
     */
    public static void setCurrUser(Context context,String key,String user){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, user);
        editor.commit();
    }

    /**
     * 获取当前用户名
     * @param context
     * @param key
     * @return
     */
    public static String getCurrUser(Context context,String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key,"");
    }


    /**
     * @param context
     * @param key
     * @return
     */
    public static String getCache(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }




}
