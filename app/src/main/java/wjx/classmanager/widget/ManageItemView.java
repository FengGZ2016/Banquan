package wjx.classmanager.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import wjx.classmanager.R;
import wjx.classmanager.model.Manage;

import static wjx.classmanager.R.string.manage;

/**
 * Created by wjx on 2017/9/25.
 */

public class ManageItemView extends RelativeLayout {

    private View mView;

    private CircleImageView mIcon;
    private TextView mTitle;

    public ManageItemView(@NonNull Context context) {
        this(context, null);
    }

    public ManageItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.item_manage, this);
        mIcon = (CircleImageView) mView.findViewById(R.id.item_icon);
        mTitle = (TextView) mView.findViewById(R.id.item_title);
    }

    public void bindView(Context context,Manage manage) {
        Glide.with(context).load(manage.getIcon()).into(mIcon);
        mTitle.setText(manage.getTitle());
    }
}
