package wjx.classmanager.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import wjx.classmanager.R;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.ui.activity.CreateClassActivity;

/**
 * Created by wjx on 2017/9/16.
 */

public class TitleBar extends RelativeLayout implements View.OnClickListener,PopupMenu.OnMenuItemClickListener{

    private String mTitle="班圈";

    //头像
    private CircleImageView mCircleImageView;

    //添加
    private ImageView mImageView;

    //标题
    private TextView mTextView;

    //侧滑菜单
    private SlideMenu mSlideMenu;

    //弹窗式菜单
    private PopupMenu mMenu;

    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);

    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //布局作为子View加入到RelativeLayout
        LayoutInflater.from(context).inflate(R.layout.title_bar_layout,this);

        initView();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        mCircleImageView = (CircleImageView) findViewById(R.id.title_image);
        mImageView = (ImageView) findViewById(R.id.title_add);
        mTextView = (TextView) findViewById(R.id.title_text);
        mTextView.setText(mTitle);
        mMenu = new PopupMenu(getContext(),mImageView);
        mMenu.getMenuInflater().inflate(R.menu.title_bar_menu,mMenu.getMenu());
        mMenu.setOnMenuItemClickListener(this);

        try {   //通过反射显示菜单图标
            Field field=mMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) field.get(mMenu);
            mPopup.setForceShowIcon(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 监听控件
     */
    private void initListener() {
        mCircleImageView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_image:
                mSlideMenu.toggleMenu();
                break;
            case R.id.title_add:
                mMenu.show();
                break;
        }
    }

    /**
     * 添加侧滑菜单
     * @param slideMenu
     */
    public void setSlideMenu(SlideMenu slideMenu){
        mSlideMenu=slideMenu;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_create:

                if (!"".equals(mClassUser.getGroupId())&&mClassUser.getGroupId()!=null){
                    Toast.makeText(getContext(), "你已有班级了", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(getContext(), CreateClassActivity.class);
                    getContext().startActivity(intent);
                }

                break;
            case R.id.menu_join:
                if(mOnScanClickListener!=null) {
                    mOnScanClickListener.onJoinCLick();
                }
                break;
            case R.id.menu_scan:
                if(mOnScanClickListener!=null) {
                    mOnScanClickListener.onScanClick();
                }
                break;
        }
        return true;
    }

    private onScanClickListener mOnScanClickListener;

    public interface onScanClickListener{
        void onScanClick();
        void onJoinCLick();
    }

    public void setOnScanClickListener(onScanClickListener onScanClickListener){
        mOnScanClickListener=onScanClickListener;
    }
}
