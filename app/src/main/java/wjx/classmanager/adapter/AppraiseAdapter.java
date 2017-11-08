package wjx.classmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import wjx.classmanager.R;
import wjx.classmanager.application.MyApplication;
import wjx.classmanager.model.AppraiseResult;
import wjx.classmanager.ui.activity.ShowAppraseActivity;
import wjx.classmanager.utils.DownUtil;

/**
 * 作者：国富小哥
 * 日期：2017/10/22
 * Created by Administrator
 */

public class AppraiseAdapter extends RecyclerView.Adapter<AppraiseAdapter.ViewHolder>{

    private Context mContext;
    private List<AppraiseResult> mAppraiseResults;

    public AppraiseAdapter(Context mContext,List<AppraiseResult> results){
        this.mContext=mContext;
        mAppraiseResults=results;
    }


    @Override
    public AppraiseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appraise_item_view,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AppraiseAdapter.ViewHolder holder, int position) {

        final AppraiseResult appraiseResult=mAppraiseResults.get(position);
        holder.mTextViewTitle.setText(appraiseResult.getAppraiseTitle());
        holder.mTextViewTime.setText(appraiseResult.getTimeForCreate());
        holder.mTextViewAuthor.setText(appraiseResult.getNameForCreate());

        holder.appraiseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ShowAppraseActivity.class);
               intent.putExtra("appraiseResult",appraiseResult);
               // mContext.startActivity(intent);
                mContext.startActivity(intent);
            }
        });
        //暂时用这图片
        getBombImage(appraiseResult,holder.mImageViewBg);


    }

    private void getBombImage(AppraiseResult appraiseResult, final ImageView imageViewBg) {
        //首先获取图片的名字
        final String imageName= DownUtil.getImageNameToDate(appraiseResult.getBackgroundImage());

        //根据图片名去bomb查询
        BmobFile bmobFile=new BmobFile(imageName,"",appraiseResult.getBackgroundImage());
        File file = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());

        bmobFile.download(file, new DownloadFileListener() {
            @Override
            public void done(String savePath, BmobException e) {
                if (e==null){
                    //成功
                    Glide.with(MyApplication.getMyContext())
                            .load(savePath)
                            .into(imageViewBg);
                }else {
                    //失败,显示默认的图片
                    imageViewBg.setImageResource(R.mipmap.haha);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppraiseResults==null ? 0:mAppraiseResults.size();
    }

    /**
     * 刷新
     * @param mList
     */
    public void refresh(List<AppraiseResult> mList) {
        mAppraiseResults=mList;
        notifyDataSetChanged();
    }

    /**
     * 内部类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        View appraiseView;
        TextView mTextViewTitle;
        TextView mTextViewTime;
        ImageView mImageViewBg;
        TextView mTextViewAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            appraiseView=itemView;
            mTextViewTitle= (TextView) itemView.findViewById(R.id.text_view_title);
            mTextViewTime= (TextView) itemView.findViewById(R.id.text_view_time);
            mImageViewBg= (ImageView) itemView.findViewById(R.id.image_view_bg);
            mTextViewAuthor= (TextView) itemView.findViewById(R.id.text_view_author);
        }
    }
}
