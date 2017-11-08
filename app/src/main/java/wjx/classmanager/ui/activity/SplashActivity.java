package wjx.classmanager.ui.activity;

import android.content.Intent;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;

import wjx.classmanager.R;
import wjx.classmanager.presenter.impl.SplashPresentImpl;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.SplashView;

public class SplashActivity extends BaseActivity implements SplashView{

    private static final int SLEEP_TIME=2000;
    private SplashPresentImpl mSplashPresent;
    private ImageView mImageView;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mSplashPresent =new SplashPresentImpl(this);
        mImageView = (ImageView) findViewById(R.id.splash_image);
        mImageView.setAlpha(0.4f);
        mImageView.animate().alpha(1.0f).setDuration(1500).start();

        ThreadUtil.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try{
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    long costTime = System.currentTimeMillis() - start;
                    if(SLEEP_TIME-costTime>0){
                        Thread.sleep(SLEEP_TIME-costTime);
                    }
                    mSplashPresent.checkLogInStatus();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean isImmersive() {
        return true;
    }

    @Override
    public void onLogIn() {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public void onNotLogIn() {
        startActivity(new Intent(SplashActivity.this,LogInActivity.class));
        finish();
    }
}
