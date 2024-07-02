package com.example.chatapp.ui.message.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.example.chatapp.R;
import com.example.chatapp.db.message.Message;
import com.example.chatapp.util.UserCache;

public class P2PMessageAdapter extends BaseQuickAdapter<Message, QuickViewHolder> {
    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int i, @Nullable Message item) {
        if (item == null) return;
        holder.setText(R.id.messageTextView, item.getContent());
        if (isSentByMe(item)) {
            setRightMessageStyle(holder);
        } else {
            setLeftMessageStyle(holder);
        }
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_p2p_message, viewGroup);
    }

    /**
     * 是否由我发送的消息
     */
    private boolean isSentByMe(Message message) {
        return (message.getFromAccount().equals(UserCache.INSTANCE.getUser().getPhone()));
    }

    /**
     * 设置联系人消息样式
     */
    private void setLeftMessageStyle(@NonNull QuickViewHolder holder) {
        holder.setBackgroundResource(R.id.messageTextView, R.drawable.bg_message_contacts);
        holder.setGone(R.id.spaceViewStart, true);
        holder.setGone(R.id.spaceViewEnd, false);
    }

    /**
     * 设置我发送的消息样式
     */
    private void setRightMessageStyle(@NonNull QuickViewHolder holder) {
        holder.setBackgroundResource(R.id.messageTextView, R.drawable.bg_message_mine);
        holder.setGone(R.id.spaceViewStart, false);
        holder.setGone(R.id.spaceViewEnd, true);
    }
}
