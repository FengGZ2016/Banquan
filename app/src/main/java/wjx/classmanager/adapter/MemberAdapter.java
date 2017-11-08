package wjx.classmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.model.ClassUser;
import wjx.classmanager.ui.activity.PerosonalActivity;
import wjx.classmanager.widget.MemberItemView;

/**
 * Created by wjx on 2017/10/12.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<ClassUser> mClassUsers=new ArrayList<>();
    private Context mContext;

    public MemberAdapter(List<ClassUser> members){
        mClassUsers=members;
    }



    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext =parent.getContext();
        return new MemberViewHolder(new MemberItemView(mContext));
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, final int position) {
        holder.mMemberItemView.bindView(mClassUsers.get(position));

        holder.mMemberItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PerosonalActivity.class);
                //传一个objectid
                intent.putExtra("objectId",mClassUsers.get(position).getObjectId());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mClassUsers==null){
            return 0;
        }
        return mClassUsers.size();
    }

    public void refresh(List<ClassUser> members) {
        mClassUsers=members;
        notifyDataSetChanged();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder{

        MemberItemView mMemberItemView;

        public MemberViewHolder(MemberItemView itemView) {
            super(itemView);
            mMemberItemView=itemView;
        }
    }
}
