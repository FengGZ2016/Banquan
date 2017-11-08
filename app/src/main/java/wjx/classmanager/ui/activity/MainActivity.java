package wjx.classmanager.ui.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import wjx.classlibrary.zxing.CustomScanActivity;
import wjx.classmanager.R;
import wjx.classmanager.collector.ActivityCollector;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.presenter.impl.MainPresenterImpl;
import wjx.classmanager.ui.fragment.FragmentFactory;
import wjx.classmanager.ui.fragment.ManageFragment;
import wjx.classmanager.ui.fragment.MemberFragment;
import wjx.classmanager.ui.fragment.MessageFragment;
import wjx.classmanager.utils.SPUtil;
import wjx.classmanager.utils.ThreadUtil;
import wjx.classmanager.view.MainView;
import wjx.classmanager.widget.SlideMenu;
import wjx.classmanager.widget.TitleBar;

import static org.litepal.LitePalApplication.getContext;
import static wjx.classmanager.model.Constant.ErrorCode.ERROR_ACTIVITY;
import static wjx.classmanager.model.Constant.FragmentType.CREATE_MANAGE;
import static wjx.classmanager.model.Constant.FragmentType.CREATE_MESSAGE;
import static wjx.classmanager.model.Constant.FragmentType.CREATE_NOTIFY;
import static wjx.classmanager.model.Constant.FragmentType.FRAGMENT_COUNT;
import static wjx.classmanager.model.Constant.FragmentType.FRAGMENT_MANAGE;
import static wjx.classmanager.model.Constant.FragmentType.FRAGMENT_MSG;
import static wjx.classmanager.model.Constant.FragmentType.FRAGMENT_NOTIFY;
import static wjx.classmanager.model.Constant.MyClass.CLASS_GROUP_ID;

public class MainActivity extends BaseActivity implements View.OnClickListener, MainView, TitleBar.onScanClickListener {

    private SlideMenu mSlideMenu;
    private TitleBar mTitleBar;
    private RadioGroup mRadioGroup;

    private Fragment[] mFragments;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private FrameLayout mMessageFrame;
    private FrameLayout mNotifyFrame;
    private FrameLayout mManageFrame;
    private FrameLayout[] mFrameLayouts;

    //侧滑菜单
    private RelativeLayout mLeftClass;
    private RelativeLayout mLeftEvaluate;
    private RelativeLayout mLeftVote;
    private RelativeLayout mLeftData;
    private RelativeLayout mLeftInfo;
    private RelativeLayout mLeftUnsign;

    private MainPresenterImpl mMainPresenter;

    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    @Override
    protected void init() {


    }

    @Override
    public void initView() {

        //控件绑定
        mSlideMenu = (SlideMenu) findViewById(R.id.slide_menu);
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        //侧滑菜单绑定
        mLeftClass = (RelativeLayout) findViewById(R.id.left_class);
        mLeftEvaluate = (RelativeLayout) findViewById(R.id.left_evaluate);
        mLeftVote = (RelativeLayout) findViewById(R.id.left_vote);
        mLeftData = (RelativeLayout) findViewById(R.id.left_data);
        mLeftInfo = (RelativeLayout) findViewById(R.id.left_info);
        mLeftUnsign = (RelativeLayout) findViewById(R.id.left_unsign);

        //布局绑定
        mMessageFrame = (FrameLayout) findViewById(R.id.frame_message);
        mNotifyFrame = (FrameLayout) findViewById(R.id.frame_notify);
        mManageFrame = (FrameLayout) findViewById(R.id.frame_manage);
        mFrameLayouts = new FrameLayout[FRAGMENT_COUNT];

        //管理布局
        mFrameLayouts[0] = mMessageFrame;
        mFrameLayouts[1] = mNotifyFrame;
        mFrameLayouts[2] = mManageFrame;

        //获取Fragment事务
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        //创建Fragment
        MessageFragment messageFragment = (MessageFragment) FragmentFactory.getFragment(CREATE_MESSAGE);
        MemberFragment memberFragment = (MemberFragment) FragmentFactory.getFragment(CREATE_NOTIFY);
        ManageFragment manageFragment = (ManageFragment) FragmentFactory.getFragment(CREATE_MANAGE);

        //管理Fragment
        mFragments = new Fragment[FRAGMENT_COUNT];
        mFragments[0] = messageFragment;
        mFragments[1] = memberFragment;
        mFragments[2] = manageFragment;

        //默认显示消息界面
        showFragment(FRAGMENT_MSG);
        mMainPresenter = new MainPresenterImpl(this, this);

        EMClient.getInstance().addConnectionListener(mEMConnectionListener);
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }


    @Override
    public void initListener() {
        mLeftClass.setOnClickListener(this);
        mLeftEvaluate.setOnClickListener(this);
        mLeftVote.setOnClickListener(this);
        mLeftData.setOnClickListener(this);
        mLeftInfo.setOnClickListener(this);
        mLeftUnsign.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_msg:
                        showFragment(FRAGMENT_MSG);
                        break;
                    case R.id.radio_notify:
                        showFragment(FRAGMENT_NOTIFY);
                        break;
                    case R.id.radio_manage:
                        showFragment(FRAGMENT_MANAGE);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        mTitleBar.setSlideMenu(mSlideMenu);
        mTitleBar.setOnScanClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isImmersive() {
        return true;
    }

    /**
     * 显示某个Fragment
     *
     * @param index
     */
    private void showFragment(int index) {
        //每次更改Fragment都需要重新获取事务,犯错重大Bug
        mTransaction = mFragmentManager.beginTransaction();
        for (int i = 0; i < FRAGMENT_COUNT; i++) {
            mTransaction.hide(mFragments[i]);
            mFrameLayouts[i].setVisibility(View.GONE);
        }
        mFrameLayouts[index].setVisibility(View.VISIBLE);
        mTransaction.show(mFragments[index]);
        mTransaction.replace(mFrameLayouts[index].getId(), mFragments[index]);
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_class:
                if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
                    startActivity(MyClassActivity.class,CLASS_GROUP_ID,mClassUser.getGroupId());
                }else {
                    Toast.makeText(mContext, "你还没有班级!", Toast.LENGTH_SHORT).show();
                }
              //  mMainPresenter.toMyClass();
                break;
            case R.id.left_evaluate:
                startActivity(EvaluateActivity.class);
               // mMainPresenter.evaluate();
                break;
            case R.id.left_vote:
                //mMainPresenter.activityVote();
                startActivity(VoteActivity.class);
                break;
            case R.id.left_data:
                toPostData();
                break;
            case R.id.left_info:
                //mMainPresenter.personalInfo();
                startActivity(PerosonalActivity.class);
                break;
            case R.id.left_unsign:
                mMainPresenter.unSign();
                SPUtil.addFirstRunTag(mContext,false);
                break;
        }
    }

    /**
     * 上传资料
     */
    private void toPostData() {
        //首先拿到班级id
        String classId=mClassUser.getGroupId();
        if (classId!=null){
            startActivity(PostDataActivity.class,CLASS_GROUP_ID,classId);
        }else {
            Toast.makeText(mContext, "你还没有班级!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 我的班级
     */
    private void toMyClass() {
        String classId=mClassUser.getGroupId();
                    if (classId!=null){
                        startActivity(MyClassActivity.class,CLASS_GROUP_ID,classId);
                    }else {
                        startActivity(ErrorActivity.class,ERROR_ACTIVITY,"你还没有班级!");
                    }
    }

    @Override
    public void logoutSuccess() {
        hideProgress();
        startActivity(LogInActivity.class,true);
    }

    @Override
    public void logoutFailed() {
        hideProgress();
        showToast("退出登录失败");
    }

    @Override
    public void onStartLogout() {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress("正在退出登录...");
            }
        });
    }

    @Override
    public void toMyClass(String classId) {
        startActivity(MyClassActivity.class,CLASS_GROUP_ID,classId);
    }

    @Override
    public void noClass() {
        startActivity(ErrorActivity.class,ERROR_ACTIVITY,"你还没有班级!");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            //扫描出来的结果是一个班级id
            String ScanResult = intentResult.getContents();
            Intent intent=new Intent(this,ClassSeachActivity.class);
            intent.putExtra("classId",ScanResult);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onScanClick() {
        new IntentIntegrator(MainActivity.this)
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class)
                .initiateScan();
    }

    @Override
    public void onJoinCLick() {

        String groupId= mClassUser.getGroupId();
        if (groupId!=null){
            Toast.makeText(getContext(), "你已有班级了", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(JoinClassActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(mEMConnectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }

    /**
     * 环信连接状态监听
     */
    private EMConnectionListener mEMConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            ThreadUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (error) {
                        case EMError.USER_LOGIN_ANOTHER_DEVICE:
                            ActivityCollector.finishAll();
                            startActivity(LogInActivity.class);
                            showToast("账号异地登录");
                            break;
                        case EMError.USER_REMOVED:
                            showToast("用户被移除");
                            break;
                        default:
                            if (NetUtils.hasNetwork(MainActivity.this)) {
                                showToast("网络状况不佳，连接不到服务器");
                            } else {
                                showToast("当前网络不可用，请检查网络设置");
                            }
                    }
                }
            });
        }
    };

    private EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) { //收到消息

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) { //收到透传消息

        }

        @Override
        public void onMessageRead(List<EMMessage> list) { //收到已读回执

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) { //收到已送达回执

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) { //消息被撤回

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) { //消息状态变动

        }
    };
}
