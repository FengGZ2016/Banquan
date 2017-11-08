package wjx.classmanager.ui.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import wjx.classmanager.R;
import wjx.classmanager.adapter.NavPageAdapter;
import wjx.classmanager.widget.PageTransformer;

public class NavigationActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private static final int PAGE_SIZE=4;
    private ViewPager mViewPager;
    private TextView mTextView;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mTextView = (TextView) findViewById(R.id.nav_used);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new NavPageAdapter());
        mViewPager.setPageTransformer(true,new PageTransformer());
    }

    @Override
    public void initListener() {
        mTextView.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_navigation;
    }

    @Override
    public boolean isImmersive() {
        return true;
    }

    @Override
    public void onClick(View v) {
        startActivity(MainActivity.class,true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position==PAGE_SIZE-1){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setAlpha(0f);
            mTextView.animate().alpha(1f).setDuration(1000).start();
        }else{
            mTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
