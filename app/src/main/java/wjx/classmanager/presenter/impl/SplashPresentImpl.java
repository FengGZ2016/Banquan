package wjx.classmanager.presenter.impl;

import com.hyphenate.chat.EMClient;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.presenter.SplashPresenter;
import wjx.classmanager.view.SplashView;

/**
 * Created by wjx on 2017/9/26.
 */

public class SplashPresentImpl implements SplashPresenter {

    private SplashView mSplashView;

    public SplashPresentImpl(SplashView splashView){
        mSplashView =splashView;
    }

    @Override
    public void checkLogInStatus() {
        if(checkBmobLogInStatus() && checkEMClientLogInStatus()){
            mSplashView.onLogIn();
        }else{
            mSplashView.onNotLogIn();
        }
    }

    /**
     * Bomb的登录状态
     * @return
     */
    public boolean checkBmobLogInStatus(){
        BmobUser bmobUser = BmobUser.getCurrentUser();
        return bmobUser != null;
    }

    /**
     * 环信的登录状态
     * @return
     */
    public boolean checkEMClientLogInStatus(){
        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
    }
}
