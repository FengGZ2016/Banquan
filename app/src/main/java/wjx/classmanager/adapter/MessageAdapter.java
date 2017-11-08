package wjx.classmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.model.Notification;
import wjx.classmanager.ui.activity.ShowNotifyActivity;
import wjx.classmanager.widget.MessageItemView;

/**
 * Created by wjx on 2017/10/3.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    //private List<Message> mMessages;
    private List<Notification> mNotifications;
    private int mMessagePosition;
    private int mMessageLastCount;
    private Context mContext;

    public MessageAdapter(List<Notification> mList) {
        mNotifications=mList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MessageViewHolder(new MessageItemView(mContext));
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, final int position) {
        holder.mMessageItemView.bindView(mContext,mNotifications.get(position));

        /**
         * 通知的item点击事件
         */
        holder.mMessageItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到阅读通知的页面
                Intent intent=new Intent(mContext, ShowNotifyActivity.class);

                intent.putExtra("notify",mNotifications.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.mMessageItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(holder.mMessageItemView,position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mNotifications == null) {
            return 0;
        }
        return mNotifications.size();
    }


    /**
     * 刷新
     * @param mList
     */
    public void refresh(List<Notification> mList) {
        mNotifications=mList;
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        MessageItemView mMessageItemView;

        public MessageViewHolder(MessageItemView itemView) {
            super(itemView);
            mMessageItemView = itemView;
        }
    }

    /**
     * 新增一条消息
     *
     * @param message
     */
//    public void addMessageItem(Message message) {
//        if (checkMessageType(message.getType())) {
//            mMessages.remove(mMessagePosition);
//        }
//        message.setCount(++mMessageLastCount);
//        mMessages.add(0, message);
//        notifyDataSetChanged();
//    }

    /**
     * 消息列表中是否已存在这种消息类型
     *
     * @param type
     * @return
     */
//    private boolean checkMessageType(int type) {
//        for (int i = 0; i < mMessages.size(); i++) {
//            if (mMessages.get(i).getType() == type) {
//                mMessagePosition = i;
//                mMessageLastCount =mMessages.get(i).getCount();
//                return true;
//            }
//        }
//        mMessagePosition = -1;
//        return false;
//    }

    /**
     * 显示功能菜单
     *
     * @param messageItemView
     */
    private void showPopupMenu(MessageItemView messageItemView, final int position) {
        PopupMenu menu = new PopupMenu(mContext, messageItemView);
        menu.getMenuInflater().inflate(R.menu.msg_item_menu, menu.getMenu());
        menu.setGravity(Gravity.END);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.msg_unread:
                        break;
                    case R.id.msg_top:
                        updateMessageItem(position);
                        break;
                    case R.id.msg_delete:
                        deleteMessageItem(position);
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    /**
     * 刷新消息的排序
     *
     * @param position
     */
    private void updateMessageItem(int position) {
        //Message message = mMessages.get(position);
        Notification notification=mNotifications.get(position);
        mNotifications.remove(position);
        mNotifications.add(0, notification);
        notifyDataSetChanged();
    }

    /**
     * 删除一条消息
     * @param position
     */
    private void deleteMessageItem(int position){
        mNotifications.remove(position);
        notifyDataSetChanged();
    }
}
