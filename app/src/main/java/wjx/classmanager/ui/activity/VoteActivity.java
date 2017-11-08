package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wjx.classmanager.R;
import wjx.classmanager.adapter.VoteAdapter;
import wjx.classmanager.model.VoteResult;

public class VoteActivity extends BaseActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewBack;
    private TextView mTextViewTitle;
    private ImageView mImageViewAdd;
    private RecyclerView mRecyclerView;
    private Handler mHandler;
    private ProgressDialog delPd;
    private ProgressDialog downPd;
    private VoteAdapter mVoteAdapter;
    private List<VoteResult> mVoteResults;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        initHandler();
        mVoteResults = new ArrayList<>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getDataForBmob();
            }
        });
        mImageViewBack = (ImageView) findViewById(R.id.back_image);
        mTextViewTitle = (TextView) findViewById(R.id.back_title);
        mImageViewAdd = (ImageView) findViewById(R.id.back_add);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //为recyclerView注册ContextMenu
        registerForContextMenu(mRecyclerView);
        mTextViewTitle.setText("活动投票");
        mVoteAdapter = new VoteAdapter(this, mVoteResults);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mVoteAdapter);


    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1001) {
                    //数据发生变化
                     mVoteAdapter.refresh(mVoteResults);
                     mSwipeRefreshLayout.setRefreshing(false);

                }
                return false;
            }
        });
    }

    @Override
    public void initListener() {
        //返回
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //创建活动投票
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加活动投票
                 startActivity(CreateVoteActivity.class);
            }
        });
    }

    @Override
    public void initData() {

        getDataForBmob();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        delPd = new ProgressDialog(this);
        delPd.setMessage("Deleting...");
        delPd.setCanceledOnTouchOutside(false);
        delPd.show();
        final int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;

        return super.onContextItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_vote;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }


    /**
     * 从比目中获取数据
     */
    private void getDataForBmob() {
        downPd = new ProgressDialog(this);
        downPd.setMessage("loading...");
        downPd.setCanceledOnTouchOutside(false);
        downPd.show();
        BmobQuery<VoteResult> bmobQuery = new BmobQuery<>();

        bmobQuery.findObjects(new FindListener<VoteResult>() {
            @Override
            public void done(List<VoteResult> list, BmobException e) {
                if (e==null){
                    //成功
                    downPd.dismiss();
                    mVoteResults=list;
                    //刷新
                    mHandler.sendEmptyMessage(1001);
                }else {
                    //失败
                    downPd.dismiss();
                    Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
