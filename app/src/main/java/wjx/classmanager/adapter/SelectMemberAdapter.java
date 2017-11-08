package wjx.classmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.model.Member;

/**
 * 作者：国富小哥
 * 日期：2017/10/22
 * Created by Administrator
 */

public class SelectMemberAdapter extends RecyclerView.Adapter<SelectMemberAdapter.ViewHolder>{

    private Context mContext;
    private List<Member> mMembers;

    public SelectMemberAdapter(Context mContext, List<Member> results){
        this.mContext=mContext;
        mMembers=results;
    }


    @Override
    public SelectMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectMemberAdapter.ViewHolder holder, int position) {

        final Member member=mMembers.get(position);
        holder.userName.setText(member.getUsername());
        holder.memerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectMember!=null){
                    mSelectMember.username(member.getUsername());
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mMembers==null ? 0:mMembers.size();
    }

    /**
     * 刷新
     * @param mList
     */
    public void refresh(List<Member> mList) {
        mMembers=mList;
        notifyDataSetChanged();
    }

    /**
     * 内部类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View memerView;
       TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);
            memerView=itemView;
           userName= (TextView) itemView.findViewById(R.id.item_name);
        }
    }

    public interface SelectMember{
        void username(String username);
    }

    public SelectMember mSelectMember;

    public void setSelectMember(SelectMember selectMember){
        mSelectMember=selectMember;
    }
}
