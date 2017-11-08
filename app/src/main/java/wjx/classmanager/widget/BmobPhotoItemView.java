package wjx.classmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maning.imagebrowserlibrary.MNImageBrowser;

import java.util.ArrayList;
import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.model.ClassPhoto;

/**
 * Created by wjx on 2017/10/22.
 */

public class BmobPhotoItemView extends RelativeLayout {

    private TextView mTextView;
    private ImageView mImageView;

    public BmobPhotoItemView(Context context) {
        this(context,null);
    }

    public BmobPhotoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    private void initData(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class_pic,this);
        mTextView = (TextView) view.findViewById(R.id.item_url);
        mImageView = (ImageView) view.findViewById(R.id.item_pic);

        //点击图片
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "你点击了图片", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void bindView(final Context context, List<ClassPhoto> classPhotos, final int position, final ArrayList<String> photoStrs) {

        mTextView.setText(classPhotos.get(position).getSourse());
        Glide.with(context).load(classPhotos.get(position).getPhotoPath()).into(mImageView);

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MNImageBrowser.showImageBrowser(context, mImageView, position, photoStrs);
            }
        });


    }
}
