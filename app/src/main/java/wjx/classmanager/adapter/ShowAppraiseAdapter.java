package wjx.classmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ldoublem.thumbUplib.ThumbUpView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import wjx.classmanager.R;
import wjx.classmanager.model.AppraiseResult;
import wjx.classmanager.model.Appraising;
import wjx.classmanager.utils.DownUtil;

/**
 * 作者：国富小哥
 * 日期：2017/10/23
 * Created by Administrator
 */

public class ShowAppraiseAdapter extends RecyclerView.Adapter<ShowAppraiseAdapter.ViewHolder>{

    private Context mContext;
    private List<Appraising> mAppraisings;
    private OnAgreeClickListener mAgreeClickListener;
    private AppraiseResult mAppraiseResult;
    private int num;//单个人的得票数量
    private int total;//全部人的投票总数

    public ShowAppraiseAdapter(Context mContext,List<Appraising> mAppraisings,OnAgreeClickListener mAgreeClickListener,AppraiseResult mAppraiseResult){
        this.mContext=mContext;
        this.mAppraisings=mAppraisings;
        this.mAgreeClickListener=mAgreeClickListener;
        this.mAppraiseResult=mAppraiseResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.show_apprase_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Appraising appraising=mAppraisings.get(position);

        holder.mTextViewName.setText(appraising.getName());
        holder.mTextViewIntroduce.setText(appraising.getMotto());

        num = appraising.getLikeNum();
        holder.mTextViewNum.setText(num + " 票");


        getImageFromBmob(appraising, holder.mImageViewIcon);
        holder.mThumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like){
                    //通知
                    mAgreeClickListener.OnItemAgree(position);
                }else {
                    mAgreeClickListener.OnItemUnAgree(position);
                }


            }
        });

    }


    /**
     * 从比目上获取图片数据
     * @param appraising
     * @param imageViewIcon
     */
    private void getImageFromBmob(Appraising appraising, final ImageView imageViewIcon) {
        //获取图片的名字
        final String imageName = DownUtil.getImageNameToDate(appraising.getAvatar());

        BmobFile bmobFile = new BmobFile(imageName, "", appraising.getAvatar());
        File file = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());

        bmobFile.download(file, new DownloadFileListener() {
            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    Glide.with(mContext)
                            .load(savePath)
                            .into(imageViewIcon);

                } else {
                    Log.e("AAA", "下载失败" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppraisings==null ? 0:mAppraisings.size();
    }


    /**
     * 刷新
     * @param list
     */
    public void refresh(List<Appraising> list) {
        mAppraisings = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewIcon;
        TextView mTextViewName;
        TextView mTextViewIntroduce;
        ThumbUpView mThumbUpView;
        TextView mTextViewNum;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageViewIcon= (ImageView) itemView.findViewById(R.id.iv_header);
            mTextViewName= (TextView) itemView.findViewById(R.id.tv_name);
            mTextViewIntroduce= (TextView) itemView.findViewById(R.id.tv_introduce);
            mThumbUpView= (ThumbUpView) itemView.findViewById(R.id.tpv);
            mTextViewNum= (TextView) itemView.findViewById(R.id.tv_agreeNum);
            mThumbUpView.setUnLikeType(ThumbUpView.LikeType.broken);

            mThumbUpView.setCracksColor(Color.rgb(22, 33, 44));
            mThumbUpView.setFillColor(Color.rgb(11, 200, 77));
            mThumbUpView.setEdgeColor(Color.rgb(33, 3, 219));
            mThumbUpView.Like();
            mThumbUpView.UnLike();

        }
    }

    public interface OnAgreeClickListener {
        void OnItemAgree(int position);
        void OnItemUnAgree(int position);
    }
}
