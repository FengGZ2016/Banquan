package wjx.classmanager.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wjx.classmanager.R;
import wjx.classmanager.application.MyApplication;

/**
 * Created by wjx on 2017/10/14.
 */

public class NavPageAdapter extends PagerAdapter {

    private int mImgs[] ={R.drawable.nav1,R.drawable.nav2, R.drawable.nav3,R.drawable.nav4};
    private ImageView[] mImageViews = new ImageView[mImgs.length];

    @Override
    public int getCount() {
        return mImageViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(MyApplication.getMyContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mImgs[position]);
        container.addView(imageView);
        mImageViews[position]=imageView;
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews[position]);
    }
}
