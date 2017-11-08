package wjx.classmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wjx.classmanager.R;
import wjx.classmanager.model.ImageLoader;

/**
 * Created by wjx on 2017/8/12.
 */

public class ImageAdapter extends BaseAdapter {

    private String mDirPath;
    private List<String> mImagePath;
    private LayoutInflater mInflater;
    private static Set<String> mSelectedImage = new HashSet<>();

    public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mDirPath = dirPath;
        this.mImagePath = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImagePath.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagePath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.item_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //重置状态
        holder.imageView.setImageResource(R.drawable.prepare_load);
        holder.imageButton.setImageResource(R.drawable.check_not);
        holder.imageView.setColorFilter(null);

        //加载图片
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mImagePath.get(position), holder.imageView);

        final String filePath = mDirPath + "/" + mImagePath.get(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedImage.contains(filePath)) {
                    mSelectedImage.remove(filePath);
                    holder.imageView.setColorFilter(null);
                    holder.imageButton.setImageResource(R.drawable.check_not);
                } else {      // 图片未被选中
                    mSelectedImage.add(filePath);
                    holder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    holder.imageButton.setImageResource(R.drawable.check_is);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        ImageButton imageButton;
    }

    public static Set<String> getSelectedImage(){
        return mSelectedImage;
    }
}

