package wjx.classmanager.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMGroupInfo;

import wjx.classmanager.R;
import wjx.classmanager.ui.activity.ClassDetailActivity;

/**
 * Created by wjx on 2017/10/14.
 */

public class JoinClassItemView extends RelativeLayout implements View.OnClickListener{

    private TextView mClassName;
    private Button mClassJoin;
    private EMGroupInfo groupInfo;

    public JoinClassItemView(Context context) {
        this(context,null);
    }

    public JoinClassItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initListener();
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_join_class,this);
        mClassName = (TextView) view.findViewById(R.id.item_name);
        mClassJoin = (Button) view.findViewById(R.id.item_join_class);
    }

    private void initListener(){
        mClassJoin.setOnClickListener(this);
    }

    public void bindView(EMGroupInfo joinclass){
        groupInfo=joinclass;
        mClassName.setText(groupInfo.getGroupName());
    }

    @Override
    public void onClick(View v) {

        //跳转到当前选中的班级详情页面
        if (groupInfo!=null){
            Intent intent=new Intent(getContext(), ClassDetailActivity.class);
            intent.putExtra("groupinfo",groupInfo);
            getContext().startActivity(intent);
        }else {
            Log.d("--------------","groupInfo==null!!!!");
        }


    }
}
