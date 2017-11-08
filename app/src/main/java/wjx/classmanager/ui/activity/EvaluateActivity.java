package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wjx.classmanager.R;
import wjx.classmanager.adapter.AppraiseAdapter;
import wjx.classmanager.model.AppraiseResult;
import wjx.classmanager.model.ClassUser;

/**
 * 推优评优
 */
public class EvaluateActivity extends BaseActivity{
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewBack;
    private TextView mTextViewTitle;
    private ImageView mImageViewAdd;
    private RecyclerView mRecyclerView;
    private AppraiseAdapter mAppraiseAdapter;
    private List<AppraiseResult> mAppraiseResults;
    private ProgressDialog downPd;
    private ProgressDialog delPd;
    private Handler mHandler;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        initHandler();
        mAppraiseResults=new ArrayList<>();
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiperefresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getDataForBmob();
            }
        });
        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        //为recyclerView注册ContextMenu
        registerForContextMenu(mRecyclerView);
        mTextViewTitle.setText("推优评优");
        mAppraiseAdapter=new AppraiseAdapter(this,mAppraiseResults);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAppraiseAdapter);
    }

    private void initHandler() {
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what==1001){
                    //数据发生变化
                    mAppraiseAdapter.refresh(mAppraiseResults);
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

        //创建评优
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加评优项目
                startActivity(CreateAppraseActivity.class);
            }
        });
    }

    @Override
    public void initData() {
       getDataForBmob();
    }

    /**
     * 从比目中获取数据
     */
    private void getDataForBmob() {
        downPd=new ProgressDialog(this);
        downPd.setMessage("loading...");
        downPd.setCanceledOnTouchOutside(false);
        downPd.show();
        BmobQuery<AppraiseResult> bmobQuery=new BmobQuery<>();
        //根据班级id查询
        bmobQuery.addWhereEqualTo("classId",mClassUser.getGroupId() );
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<AppraiseResult>() {
            @Override
            public void done(List<AppraiseResult> list, BmobException e) {
                if (e==null){
                    //成功
                    downPd.dismiss();
                    mAppraiseResults=list;
                    Log.e("done: ","success"+ mAppraiseResults.size() );
                    //刷新
                    mHandler.sendEmptyMessage(1001);
                }else {
                    //失败
                    downPd.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public int getLayout() {
        return R.layout.activity_evaluate;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        delPd=new ProgressDialog(this);
        delPd.setMessage("Deleting...");
        delPd.setCanceledOnTouchOutside(false);
        delPd.show();
        final int position=((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;

        return super.onContextItemSelected(item);
    }


}
