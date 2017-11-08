package wjx.classmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wjx.classmanager.model.Manage;
import wjx.classmanager.widget.ManageItemView;

/**
 * Created by wjx on 2017/9/18.
 */

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ManageViewHolder> {

    private List<Manage> mManages;
    private Context mContext;

    public ManageAdapter(List<Manage> manages) {
        mManages = manages;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        return new ManageViewHolder(new ManageItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ManageViewHolder holder, final int position) {
        holder.mManageItemView.bindView(mContext,mManages.get(position));
        holder.mManageItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile();
            }
        });
    }

    /**
     * 发送班级成绩文件
     */
    private void sendFile() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            // intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        }


    }


    @Override
    public int getItemCount() {
        if(mManages==null){
            return 0;
        }
        return mManages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ManageViewHolder extends RecyclerView.ViewHolder {

        ManageItemView mManageItemView;

        public ManageViewHolder(ManageItemView itemView) {
            super(itemView);
            mManageItemView=itemView;
        }
    }

}
