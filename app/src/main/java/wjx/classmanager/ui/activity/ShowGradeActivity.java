package wjx.classmanager.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wjx.classmanager.R;
import wjx.classmanager.adapter.GradeAdapter;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Grade;

public class ShowGradeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<Grade> mGrades=new ArrayList<>();
    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);
    private GradeAdapter mGradeAdapter;


    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mRecyclerView= (RecyclerView) findViewById(R.id.grade_recycler_view);
        mGradeAdapter = new GradeAdapter(this,mGrades);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setAdapter(mGradeAdapter);
        mRecyclerView.setLayoutManager(manager);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
            getBmobData();
        }else {
            Toast.makeText(mContext, "当前还没有班级！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取比目数据
     */
    private void getBmobData() {
        BmobQuery<Grade> bmobQuery=new BmobQuery<>();
        //根据班级id查询
        bmobQuery.addWhereEqualTo("classId",mClassUser.getGroupId() );
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Grade>() {
            @Override
            public void done(List<Grade> list, BmobException e) {
            if (e==null){
                mGrades=list;
                //刷新
                //刷新适配器
                mGradeAdapter.refresh(mGrades);

            }   else {
                Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_show_grade;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }
}
