package wjx.classmanager.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import wjx.classmanager.R;
import wjx.classmanager.model.Notification;

/**
 * Created by wjx on 2017/10/3.
 */

public class MessageItemView extends RelativeLayout implements Badge.OnDragStateChangedListener{

    private ImageView mIcon;    //图标
    private TextView mTitle;    //消息标题
    private TextView mTime;     //发布时间
    private TextView mCount;    //消息数量
    private Badge mQBadgeView;

    public MessageItemView(Context context) {
        this(context,null);
    }

    public MessageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initListener();
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg,this);
        mIcon = (ImageView) view.findViewById(R.id.item_icon);
        mTitle = (TextView) view.findViewById(R.id.item_title);
        mTime = (TextView) view.findViewById(R.id.item_time);
        mCount = (TextView) view.findViewById(R.id.item_count);
        mQBadgeView =new QBadgeView(context).bindTarget(mCount).setBadgeNumber(-1);
    }

    private void initListener() {
        mQBadgeView.setOnDragStateChangedListener(this);
    }

    public void bindView(Context context, Notification notification){
        mTitle.setText(notification.getTitle());
        mTime.setText(notification.getTime());
        Glide.with(context).load(R.drawable.title_bar_icon).into(mIcon);
        mQBadgeView.setBadgeNumber(1);

    }

    @Override
    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
        switch (dragState){
            case STATE_START:
                break;
            case STATE_DRAGGING:
                break;
            case STATE_DRAGGING_OUT_OF_RANGE:
                break;
            case STATE_CANCELED:
                break;
            case STATE_SUCCEED:
                badge.setBadgeNumber(0);
                break;
        }
    }
}
