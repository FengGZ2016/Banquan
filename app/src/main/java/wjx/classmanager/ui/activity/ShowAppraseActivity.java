package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import wjx.classmanager.R;
import wjx.classmanager.adapter.ShowAppraiseAdapter;
import wjx.classmanager.model.AppraiseResult;
import wjx.classmanager.model.Appraising;
import wjx.classmanager.model.ClassUser;

public class ShowAppraseActivity extends BaseActivity implements ShowAppraiseAdapter.OnAgreeClickListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewBack;
    private TextView mTextViewTitle;
    private ImageView mImageViewAdd;
    private RecyclerView mRecyclerView;

    private ShowAppraiseAdapter mShowAppraiseAdapter;
    private List<Appraising> mAppraisings = new ArrayList<>();
    //private BmobRealTimeData bmobRealTimeData;     //Bmob实时同步
    private AppraiseResult mAppraiseResult;
    private ProgressDialog loadPd;
    private int total;
    private Handler mHandler;
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);


    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        initHandler();
        mAppraiseResult= (AppraiseResult) getIntent().getSerializableExtra("appraiseResult");
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppraiseData(mAppraiseResult);
            }
        });
        mImageViewBack= (ImageView) findViewById(R.id.back_image);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mImageViewAdd.setVisibility(View.GONE);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mShowAppraiseAdapter=new ShowAppraiseAdapter(this,mAppraisings,this,mAppraiseResult);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mShowAppraiseAdapter);

    }

    private void initHandler() {
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what==1001){
                    //数据发生变化
                    //刷新adapter
                    mShowAppraiseAdapter.refresh(mAppraisings);
                    mSwipeRefreshLayout.setRefreshing(false);

                }
                return false;
            }
        });

    }

    @Override
    public void initListener() {
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        getAppraiseData(mAppraiseResult);
       // onTimeSyncData();
    }



    /**
     * 获取单个人的评优数据
     * @param appraiseResult
     */
    private void getAppraiseData(AppraiseResult appraiseResult) {
        loadPd=new ProgressDialog(this);
        loadPd.setMessage("正在加载...");
        loadPd.setCanceledOnTouchOutside(false);
        loadPd.show();

        BmobQuery<Appraising> bmobQuery = new BmobQuery<>();
        //根据评优的标题查询单个评优项目的数据
        bmobQuery.addWhereEqualTo("title", appraiseResult.getAppraiseTitle());
        //最多返回50条数据
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Appraising>() {
            @Override
            public void done(List<Appraising> list, BmobException e) {
                if (e==null){
                    loadPd.dismiss();
//                    List<Appraising> mList=new ArrayList<>();
//                    for(int i=0;i< list.size();i++){
//                        //如果数据库中存在同标题的评优，就只要自己班级的
//                        if(mClassUser.getGroupId().equals(list.get(i).getClassId())){
//                            mList.add(list.get(i));
//                        }
//                    }
                    mAppraisings=list;
                    //刷新
                    mHandler.sendEmptyMessage(1001);
                }else {
                    //查询失败
                    loadPd.dismiss();
                    showToast("加载失败！");
                }
            }
        });

    }


    /**
     * 实时同步数据
     */
    private void onTimeSyncData() {

//            bmobRealTimeData = new BmobRealTimeData();
//            bmobRealTimeData.start(new ValueEventListener() {
//                @Override
//                public void onConnectCompleted(Exception e) {
//                    if (e == null && bmobRealTimeData.isConnected()) {
//                        bmobRealTimeData.subTableUpdate("AppraiseResult");
//                        Log.e("AAA", "连接成功");
//                    } else {
//                        Log.e("AAA", "连接失败");
//                    }
//                }
//
//                @Override
//                public void onDataChange(JSONObject jsonObject) {
//                    if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))) {
//                        JSONObject data = jsonObject.optJSONObject("data");
//                     //   total = data.optInt("voteNum");
//                        total=data.optInt("likeCount");
//                        Log.e("AAA", "voteNum:" + jsonObject.toString());
//                        Log.e("AAA", "voteNum:" + total);
//                    }
//                }
//            });

    }



    @Override
    public int getLayout() {
        return R.layout.activity_show_apprase;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    /**
     * 点击投票按钮
     * @param position
     */
    @Override
    public void OnItemAgree(int position) {

      //  bmobRealTimeData.subRowUpdate("Appraising", mAppraisings.get(position).getObjectId());
        int num = (mAppraisings.get(position).getLikeNum()) + 1;
        List<String> mAgreeList=mAppraisings.get(position).getLikeNameList();
        mAgreeList.add(mClassUser.getUsername());
        Appraising appraising=new Appraising();
        appraising.setLikeNameList(mAgreeList);
        appraising.setLikeNum(num);
        appraising.update(mAppraisings.get(position).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    //更新
                    updateAppraseResults();
                }else {

                }
            }
        });

    }


    private void updateAppraseResults() {
        int num=mAppraiseResult.getLikeCount();
        AppraiseResult result=new AppraiseResult();
        result.setLikeCount(num+1);
        result.update(mAppraiseResult.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
           if (e==null){
               //更新成功，处理ui
              updateAdapter(mAppraiseResult.getAppraiseTitle());
           }   else {

           }
            }
        });

    }

    private void updateAdapter(String appraiseTitle) {
        BmobQuery<Appraising> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("title",appraiseTitle);
        bmobQuery.setLimit(20);
        bmobQuery.findObjects(new FindListener<Appraising>() {
            @Override
            public void done(List<Appraising> list, BmobException e) {
           if (e==null){
               mShowAppraiseAdapter.refresh(list);
           }   else {

           }
            }
        });
    }

    /**
     * 取消投票
     * @param position
     */
    @Override
    public void OnItemUnAgree(int position) {
      //  bmobRealTimeData.subRowUpdate("Appraising", mAppraisings.get(position).getObjectId());
        int num = (mAppraisings.get(position).getLikeNum()) - 1;
    }
}
