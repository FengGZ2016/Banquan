package wjx.classmanager.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.adapter.JoinClassAdapter;
import wjx.classmanager.presenter.impl.JoinClassPresenterImpl;
import wjx.classmanager.view.JoinClassView;

public class JoinClassActivity extends BaseActivity implements View.OnClickListener,JoinClassView{

    private ImageView mImageBack;
    private ImageView mImageSearch;
    private TextView mTextTitle;
    private EditText mEditClass;
    private RecyclerView mRecyclerView;
    private JoinClassAdapter mJoinClassAdapter;
    private JoinClassPresenterImpl mJoinClassPresenter;
    private List<EMGroupInfo> groupsList;

    private String cursor;
    //分页数量
    private final int pagesize = 20;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {

        groupsList = new ArrayList<>();
        mImageBack = (ImageView) findViewById(R.id.back_image);
        mImageSearch = (ImageView) findViewById(R.id.join_search);
        mTextTitle = (TextView) findViewById(R.id.back_title);
        mEditClass = (EditText) findViewById(R.id.join_input);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_join);

        mJoinClassPresenter = new JoinClassPresenterImpl(this);
        mJoinClassAdapter = new JoinClassAdapter(groupsList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mJoinClassAdapter);

        loadData();
    }

    /**
     * 拿到服务器的数据
     */
    private void loadData() {

        mJoinClassPresenter.getServerData(pagesize,cursor);
    }

    @Override
    public void initListener() {
        mImageBack.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTextTitle.setText("班级查找");
    }

    @Override
    public int getLayout() {
        return R.layout.activity_join_class;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.join_search:
               // mJoinClassPresenter.searchClassFromServer(mEditClass.getText().toString());
                break;
            case R.id.back_image:
                finish();
                break;
        }
    }

    @Override
    public void onStartSearch() {
        showProgress("努力查找中...");
    }

    @Override
    public void getServerDataSuccess(EMCursorResult<EMGroupInfo> result, List<EMGroupInfo> returnGroups) {

        groupsList.addAll(returnGroups);
        updateAdapter();

    }

    private void updateAdapter() {
        mJoinClassAdapter = new JoinClassAdapter(groupsList);
        mRecyclerView.setAdapter(mJoinClassAdapter);
        mJoinClassAdapter.notifyDataSetChanged();
    }

    @Override
    public void getServerDataFail() {

    }
}
