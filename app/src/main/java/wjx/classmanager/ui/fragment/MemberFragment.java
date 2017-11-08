package wjx.classmanager.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.adapter.MemberAdapter;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.presenter.impl.MemberPresenterImpl;
import wjx.classmanager.view.MemberView;
import wjx.classmanager.widget.SlideBar;

/**
 * Created by wjx on 2017/9/16.
 */

public class MemberFragment extends BaseFragment implements MemberView,SlideBar.OnSlideBarChangeListener {

    private SlideBar mSlideBar;
    private TextView mMemberSelectText;
    private RecyclerView mRecyclerView;
    private MemberAdapter mMemberAdapter;
    private MemberPresenterImpl mMemberPresenter;
    private List<ClassUser> mClassUsers=new ArrayList<>();

    @Override
    protected void initListener() {
        mSlideBar.setOnSlidingBarChangeListener(this);
    }

    @Override
    protected void initView() {
        mMemberPresenter = new MemberPresenterImpl(this,getContext());
        mSlideBar = (SlideBar) mView.findViewById(R.id.slide_bar);
        mMemberSelectText= (TextView) mView.findViewById(R.id.member_select);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_member);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mMemberAdapter =new MemberAdapter(mClassUsers);
        mRecyclerView.setAdapter(mMemberAdapter);

        initData();
    }

    private void initData() {
        mMemberPresenter.getServerData();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_member;
    }

    @Override
    public void onSectionChange(int index, String section) {
        mMemberSelectText.setVisibility(View.VISIBLE);
        mMemberSelectText.setText(section);
    }

    @Override
    public void onSlidingFinish() {
        mMemberSelectText.setVisibility(View.GONE);
    }

    /**
     * 成功获取比目数据
     * @param memberList
     */
    @Override
    public void getServerDataSuccess(List<ClassUser> memberList) {
        mClassUsers=memberList;
        updateAdapter();
    }

    @Override
    public void getServerDataFail() {
        Toast.makeText(mContext, "获取成员列表失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noClass() {
        Toast.makeText(mContext, "当前没有班级！", Toast.LENGTH_SHORT).show();
    }

    private void updateAdapter() {
        mMemberAdapter =new MemberAdapter(mClassUsers);
        mRecyclerView.setAdapter(mMemberAdapter);
        mMemberAdapter.notifyDataSetChanged();
    }
}
