package wjx.classmanager.ui.fragment;

import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.R;
import wjx.classmanager.adapter.MessageAdapter;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Constant;
import wjx.classmanager.model.Notification;
import wjx.classmanager.presenter.impl.MessagePresenterImpl;
import wjx.classmanager.ui.activity.ApplyActivity;
import wjx.classmanager.view.MessageView;

/**
 * Created by wjx on 2017/9/16.
 */

public class MessageFragment extends BaseFragment implements MessageView{

    private RecyclerView mRecyclerView;
    private MessageAdapter mMessageAdapter;
    private MessagePresenterImpl mMessagePresenter;
    private IntentFilter mIntentFilter;
    private Button mButton_apply;
    private List<Notification> mNotifications=new ArrayList<>();
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);


    @Override
    protected void initListener() {
        mButton_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ApplyActivity.class);
            }
        });
    }

    @Override
    protected void initView() {
        mMessagePresenter = new MessagePresenterImpl(this);
        mButton_apply= (Button) mView.findViewById(R.id.btn_apply);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_msg);
        mMessageAdapter = new MessageAdapter(mNotifications);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setAdapter(mMessageAdapter);
        mRecyclerView.setLayoutManager(manager);
        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(Constant.Receiver.ACTION);

        initData();
    }

    private void initData() {
        if (mClassUser.getGroupId()!=null){
            mMessagePresenter.getBmobData();
        }else {
            Toast.makeText(mContext, "当前还没有班级！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_msg;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 正在从比目中获取数据
     */
    @Override
    public void onGetBmobData() {
    }

    /**
     * 获取比目数据失败
     */
    @Override
    public void getBmobDataFail() {
        Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取比目数据成功
     * @param list
     */
    @Override
    public void getBmobDataSuccess(List<Notification> list) {
        Toast.makeText(mContext, "获取数据成功", Toast.LENGTH_SHORT).show();
        mNotifications=list;
        //刷新适配器
        mMessageAdapter.refresh(mNotifications);
    }
}