package wjx.classmanager.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.R;
import wjx.classmanager.adapter.MsgAdapter;
import wjx.classmanager.adapter.TextWatcherAdapter;
import wjx.classmanager.model.ChatBean;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.model.Msg;

public class ChatActivity extends BaseActivity implements EMMessageListener{

    private ImageView mImageViewBack;
    private ImageView mImageViewAdd;
    private TextView mTextViewTitle;
    private RecyclerView mRecyclerView;
    private EditText mEditTextInput;
    private Button mButtonSend;

    private String friendName;
    private Bitmap friendImg;
    private String mineName;
    private Bitmap mineImg;

    private List<Msg> msgList=new ArrayList<>();
    private MsgAdapter msgAdapter;

    private ClassUser mClassUser= BmobUser.getCurrentUser(ClassUser.class);


    @Override
    protected void init() {
        ChatBean chatBean= (ChatBean) getIntent().getSerializableExtra("chatBean");
        friendName=chatBean.getName();
        mineName=mClassUser.getUsername();
        Log.e("friend:",""+friendName);
        byte[] friendByte=chatBean.getImgByte();
        Log.e("friendIMG:",""+friendByte.length);
       // byte[] mineByte=mClassUser.getImg_msg();
        friendImg= getBitmapFromByte(friendByte);
       // mineImg= getBitmapFromByte(mineByte);
    }

    public Bitmap getBitmapFromByte(byte[] temp){//将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

    @Override
    public void initView() {
        mImageViewBack= (ImageView) findViewById(R.id.back_image);
                mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mImageViewAdd.setVisibility(View.GONE);
        mTextViewTitle= (TextView) findViewById(R.id.back_title);
                mRecyclerView= (RecyclerView) findViewById(R.id.msg_recyclerView);
        mEditTextInput= (EditText) findViewById(R.id.edit_input);
                mButtonSend= (Button) findViewById(R.id.btn_send);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        msgAdapter=new MsgAdapter(msgList);
        mRecyclerView.setAdapter(msgAdapter);

    }

    private void refreshData() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friendName);
        List<EMMessage> messages = conversation.getAllMessages();
        for(int i=0;i<messages.size();i++){
            Msg msg=new Msg( messages.get(i).getBody().toString(),Msg.TYPE_RECEIVED);
            msg.setMineName(friendName);
            msg.setMineImg(friendImg);
            msgList.add(msg);
        }
        msgAdapter.updateData(msgList);
    }

    @Override
    public void initListener() {

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mEditTextInput.setOnEditorActionListener(mOnEditorActionListener);
        mEditTextInput.addTextChangedListener(mTextWatcher);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        }
    };

    private void sendMessage() {
        String content=mEditTextInput.getText().toString();
        if(content.length()==0){
            Toast.makeText(mContext, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.length()!=0){
            final Msg msg=new Msg(content,Msg.TYPE_SENT);
            msg.setMineName(mineName);
            msg.setMineImg(mineImg);
            EMMessage message = EMMessage.createTxtSendMessage(content, friendName);
            EMClient.getInstance().chatManager().sendMessage(message);
            msgList.add(msg);
            msgAdapter.notifyItemInserted(msgList.size()-1);
            mRecyclerView.scrollToPosition(msgList.size()-1);//将消息定位到最后一行
            mEditTextInput.setText("");
            message.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e("EMM","消息发送成功");

                }

                @Override
                public void onError(int i, String s) {
                    Log.e("EMM","消息发送失败:"+s+"   i="+i);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });

        }
    }

    private TextWatcherAdapter mTextWatcher = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            mButtonSend.setEnabled(s.length() != 0);
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }



    @Override
    public void onMessageReceived(List<EMMessage> list) {
        EMMessage emMessage=list.get(list.size()-1);
        String content=  getContentMessage(emMessage.getBody().toString());//截取双引号中的内容
        Msg msg=new Msg(content,Msg.TYPE_RECEIVED);
        msgList.add(msg);
        msg.setFriednImg(friendImg);
        msgAdapter.notifyItemInserted(msgList.size()-1);
        mRecyclerView.scrollToPosition(msgList.size()-1);
    }

    private String getContentMessage(String s) {
        String content=s.substring(5,s.length()-1);
        return content;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //通过注册消息监听来接收消息
        EMClient.getInstance().chatManager().addMessageListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息监听
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }
}
