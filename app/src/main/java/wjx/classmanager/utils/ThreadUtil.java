package wjx.classmanager.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wjx on 2017/9/27.
 */

public class ThreadUtil {

    //非UI线程
    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    //UI线程
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * UI操作
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable){
        sHandler.post(runnable);
    }

    /**
     * 耗时操作
     * @param runnable
     */
    public static void runOnBackgroundThread(Runnable runnable){
        sExecutor.execute(runnable);
    }
}
