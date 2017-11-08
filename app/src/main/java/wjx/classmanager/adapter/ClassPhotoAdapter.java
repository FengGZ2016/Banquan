package wjx.classmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.model.ClassPhoto;
import wjx.classmanager.widget.BmobPhotoItemView;
/**
 * Created by wjx on 2017/10/18.
 */

public class ClassPhotoAdapter extends RecyclerView.Adapter<ClassPhotoAdapter.ClassPhotoViewHolder> {

    private List<ClassPhoto> mClassPhotos = new ArrayList<>();
    private Context mContext;
    private ArrayList<String> photoStrs=new ArrayList<>();


    public ClassPhotoAdapter(List<ClassPhoto> classPhotos,ArrayList<String> photos){
        mClassPhotos=classPhotos;
        photoStrs=photos;
    }

    @Override
    public ClassPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext =parent.getContext();
        return new ClassPhotoViewHolder(new BmobPhotoItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ClassPhotoViewHolder holder, int position) {
        holder.mBmobPhotoItemView.bindView(mContext,mClassPhotos,position,photoStrs);

    }

    @Override
    public int getItemCount() {
        return mClassPhotos.size();
    }


    /**
     * 刷新
     * @param mList
     */
    public void refresh(List<ClassPhoto> mList,ArrayList<String> photos) {
        mClassPhotos=mList;
        photoStrs=photos;
        notifyDataSetChanged();
    }

    class ClassPhotoViewHolder extends RecyclerView.ViewHolder{

        private BmobPhotoItemView mBmobPhotoItemView;

        public ClassPhotoViewHolder(BmobPhotoItemView itemView) {
            super(itemView);
            mBmobPhotoItemView =itemView;
        }
    }

    public void addPhoto(ClassPhoto classPhoto){
        mClassPhotos.add(0,classPhoto);
        notifyItemInserted(0);
    }
}
