package wjx.classmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.model.Grade;
import wjx.classmanager.ui.activity.ShowNotifyActivity;

/**
 * 作者：国富小哥
 * 日期：2017/10/22
 * Created by Administrator
 */

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder>{

    private Context mContext;
    private List<Grade> mGrades=new ArrayList<>();

    public GradeAdapter(Context mContext, List<Grade> grades){
        this.mContext=mContext;
        mGrades=grades;
    }


    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GradeAdapter.ViewHolder holder, final int position) {

      final Grade grade=mGrades.get(position);
        holder.mTextViewTitle.setText(grade.getTitle());
        holder.mTextViewTime.setText(grade.getTime());

        holder.appraiseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到阅读通知的页面
                Intent intent=new Intent(mContext, ShowNotifyActivity.class);
                intent.putExtra("grade",mGrades.get(position));
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mGrades==null ? 0:mGrades.size();
    }

    /**
     * 刷新
     * @param mList
     */
    public void refresh(List<Grade> mList) {
        mGrades=mList;
        notifyDataSetChanged();
    }

    /**
     * 内部类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View appraiseView;
        TextView mTextViewTitle;
        TextView mTextViewTime;


        public ViewHolder(View itemView) {
            super(itemView);
            appraiseView=itemView;
            mTextViewTitle= (TextView) itemView.findViewById(R.id.item_title);
            mTextViewTime= (TextView) itemView.findViewById(R.id.item_time);

        }
    }
}
