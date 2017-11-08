package wjx.classmanager.widget;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager切页动画类,当前为层叠式
 * Created by wjx on 2017/7/23.
 */

public class PageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;//最小比例

    @Override
    public void transformPage(View page, float position) {
        /**
         * position 解析
         * 当前页面永远为0
         * 相对于当前页面左边为负，相邻差1
         * 相对于当前页面右边为正，相邻差1
         */
        int pageWidth = page.getWidth();
        if (position < -1) {//（即-无穷到-1）：让引导页消失，透明度为0
            page.setAlpha(0);//设置透明度
        } else if (position <= 0) {//（即-1到0）：让引导页出现
            page.setAlpha(1);
            page.setTranslationX(0);//位置变化动画
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) {//（即0到1）：让引导页根据position做动画
            page.setAlpha(1 - position);
            page.setTranslationX(pageWidth * -position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {//（即1到无穷）：让引导页消失，透明度为0
            page.setAlpha(0);
        }
    }
}
