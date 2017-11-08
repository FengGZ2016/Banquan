package wjx.classmanager.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.adapter.ApplyAdapter;
import wjx.classmanager.db.InviteMessgeDao;
import wjx.classmanager.model.InviteMessage;

public class ApplyActivity extends BaseActivity {

    private LinearLayout mLinearLayout;
    private ListView mListView;
    private InviteMessgeDao mInviteMessgeDao;
    private List<InviteMessage> msgs;
    private ApplyAdapter mApplyAdapter;


    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mLinearLayout= (LinearLayout) findViewById(R.id.linear_layout_back);
        mListView= (ListView) findViewById(R.id.list);
        mInviteMessgeDao=new InviteMessgeDao(this);
        msgs=mInviteMessgeDao.getMessagesList();

        //反转
        Collections.reverse(msgs);

        mApplyAdapter=new ApplyAdapter(this,1,msgs);
        mListView.setAdapter(mApplyAdapter);

        mInviteMessgeDao.saveUnreadMessageCount(0);
    }

    @Override
    public void initListener() {
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_apply;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
