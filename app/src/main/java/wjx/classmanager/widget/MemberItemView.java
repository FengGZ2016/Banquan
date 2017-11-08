package wjx.classmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wjx.classmanager.R;
import wjx.classmanager.model.ClassUser;

/**
 * Created by wjx on 2017/10/12.
 */

public class MemberItemView extends RelativeLayout {

    private TextView mSectionText;
    private TextView mNameText;

    public MemberItemView(Context context) {
        this(context,null);
    }

    public MemberItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member,this);
        mNameText = (TextView) view.findViewById(R.id.item_name);
    }

    public void bindView(ClassUser member){
        if (member.getRealName()!=null){
            mNameText.setText(member.getRealName());
        }else {
            mNameText.setText(member.getUsername());
        }
    }
}
