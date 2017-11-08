package wjx.classmanager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import wjx.classmanager.R;

import static com.hyphenate.chat.a.a.a.i;

/**
 * Created by wjx on 2017/7/2.
 */

public class SlideMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private int mScreenWidth;
    //dp值
    private int mMenuRightPadding = 50;
    private int mMenuWidth;

    private boolean once = false;
    private boolean isOpen = false;

    private VelocityTracker mVelocityTracker;

    private final int VELOCITY_OPEN = 300;
    private final int VELOCITY_CLOSE = -300;

    public SlideMenu(Context context) {
        //在代码中直接New一个对象的时候调用
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        //使用和获取自定义属性的时候调用
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlideMenu, defStyleAttr, 0);
        //获取自定义数量
        int count = array.getIndexCount();
        //遍历自定义属性
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.SlideMenu_rightPadding:
                    mMenuRightPadding = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        //用完后记得释放
        array.recycle();

        //获取屏幕宽度
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;

        //初始化滑动速度跟踪类
        mVelocityTracker = VelocityTracker.obtain();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        //不使用自定义属性的时候调用
        this(context, attrs, 0);
    }

    /**
     * 设置子View的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override        //第一步
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //因为onMeasure会被多次调用，只有第一次的时候才设置宽和高
        if (!once) {
            //第一个子控件，就是LinearLayout
            mWapper = (LinearLayout) getChildAt(0);
            //mWapper(就是LinearLayout)里面第一个子控件就是菜单
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            //mWapper(就是LinearLayout)里面第二个子控件就是内容
            mContent = (ViewGroup) mWapper.getChildAt(1);

            //设置菜单宽度为屏幕的宽度-右边距
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            //内容的宽度为屏幕的宽度
            mContent.getLayoutParams().width = mScreenWidth;

            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置子View的位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override   //第二步
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        //因为onLayout会被多次调用，只有状态发生改变的时候才调用
        if (changed) {
            //通过设置偏移量，将Menu隐藏
            this.scrollTo(mMenuWidth, 0);//X为正，向右移动
        }

    }

    @Override   //第三步
    public boolean onTouchEvent(MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        int x = (int) ev.getX();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);

                //隐藏在左边的宽度(即可滑动的X)
                int scrollx = getScrollX();

                // 菜单处于关闭状态
                if (!isOpen) {
                    if (mVelocityTracker.getXVelocity() >= VELOCITY_OPEN
                            || scrollx <= mMenuWidth / 2) {  //显示
                        this.smoothScrollTo(0, 0);
                        isOpen = true;
                    } else {  //隐藏
                        this.smoothScrollTo(mMenuWidth, 0);
                        isOpen = false;
                    }
                } else {
                    if (mVelocityTracker.getXVelocity() < VELOCITY_CLOSE
                            || scrollx >= mMenuWidth / 2 || x - mMenuWidth > 0) {
                        this.smoothScrollTo(mMenuWidth, 0);
                        isOpen = false;
                    } else {
                        this.smoothScrollTo(0, 0);
                        isOpen = true;
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen) {
            return;
        } else {
            this.smoothScrollTo(0, 0);
            isOpen = true;
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isOpen) {
            return;
        } else {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单
     */
    public void toggleMenu() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override       //滚动时触发
    protected void onScrollChanged(int left, int top, int oldl, int oldt) {
        //从SlideMenu转到抽屉式，仅仅几行代码
        super.onScrollChanged(left, top, oldl, oldt);

        //  left=getScrollX(),都是代表可滑动的距离,最大是mMenuWidth(完全隐藏),最小是0(完全显示)
        float scale = left * 1.0f / mMenuWidth; //左移scale增大，右移相反

        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

        //把中心点设置到内容的左边界的中心
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
    }

    private int startX;
    private int startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isOpen && ev.getX()-mMenuWidth>0){
            return true;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dX= (int) (ev.getX()-startX);
                int dY= (int) (ev.getY()-startY);
                if(Math.abs(dX)/2<Math.abs(dY)){
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
