package wjx.classmanager.ui.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.adapter.ManageAdapter;
import wjx.classmanager.listener.RecyclerItemClickListener;
import wjx.classmanager.model.Manage;
import wjx.classmanager.presenter.ManagePresenter;
import wjx.classmanager.presenter.impl.ManagePresenterImpl;
import wjx.classmanager.view.ManageView;

/**
 * Created by wjx on 2017/9/16.
 */

public class ManageFragment extends BaseFragment implements ManageView{

    private RecyclerView mRecyclerView;
    private ManageAdapter mManageAdapter;
    private ManagePresenter mManagePresenter;
    private RecyclerItemClickListener mRecyclerItemClickListener;
    private List<Manage> mManages=new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void initListener() {
        mRecyclerItemClickListener =new RecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getLayoutPosition();

                mManagePresenter.checkItemType(mManages.get(position).getTitle() );
                Log.e( "onItemClick: ","click" + mManages.get(position).getTitle());
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getLayoutPosition() != 0 && viewHolder.getLayoutPosition() != mManages.size()-1)
                    mItemTouchHelper.startDrag(viewHolder);
            }
        };
        mRecyclerView.addOnItemTouchListener(mRecyclerItemClickListener);
    }

    @Override
    protected void initView() {
        mManagePresenter = new ManagePresenterImpl(this);
        mManages =mManagePresenter.getManageList();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_manage);
        mManageAdapter =new ManageAdapter(mManages);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mManageAdapter);

        initHelper();
    }

    private void initHelper() {
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //可拖拽的方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //侧滑删除的方向
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到拖拽到的item的Position
                int toPosition = target.getAdapterPosition();

                //第1和第2个位置不能被取代
                if (toPosition == 0 || toPosition == mManages.size()-1) {
                    toPosition = 1;
                }

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mManages, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mManages, i, i - 1);
                    }
                }
                mManageAdapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件
                mManages.remove(viewHolder.getAdapterPosition());
                mManageAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //长按可否拖拽
                return false;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                //当长按选中item的时候（拖拽开始的时候）调用
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#10000000"));
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //当手指松开的时候（拖拽完成的时候）调用
                viewHolder.itemView.setBackgroundColor(0);
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_manage;
    }

    @Override
    public void start(Class activity) {
        startActivity(activity);
    }

    @Override
    public void noClass() {
        Toast.makeText(mContext, "你还没有班级", Toast.LENGTH_SHORT).show();
    }
}
