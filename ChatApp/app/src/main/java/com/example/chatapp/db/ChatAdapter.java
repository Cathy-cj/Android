//package com.example.chatapp.db;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatapp.R;
//import com.example.chatapp.ui.PersonChat;
//
//import java.util.List;
//
//public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
//    private List<PersonChat> chatList;
//    private Context context;
//    private OnItemClickListener listener;
//
//    public ChatAdapter(Context context, List<PersonChat> chatList, OnItemClickListener listener) {
//        this.context = context;
//        this.chatList = chatList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_message, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        PersonChat chat = chatList.get(position);
//        holder.bind(chat, listener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return chatList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView senderNameTextView, messageTextView, timestampTextView;
//        ImageView senderAvatarImageView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            senderNameTextView = itemView.findViewById(R.id.sender_name);
//            messageTextView = itemView.findViewById(R.id.message_content);
//            timestampTextView = itemView.findViewById(R.id.message_timestamp);
//            senderAvatarImageView = itemView.findViewById(R.id.sender_avatar);
//        }
//
//        public void bind(final PersonChat chat, final OnItemClickListener listener) {
//            senderNameTextView.setText(chat.getSenderName());
//            messageTextView.setText(chat.getContent());
//            timestampTextView.setText(chat.getTimestamp());
//
//            // 使用原生方法加载头像
//            // 这里假设 chat.getSenderAvatar() 返回一个图片路径，您可以根据实际情况修改加载方式
//            String imagePath = chat.getSenderAvatar();
//            if (imagePath != null && !imagePath.isEmpty()) {
//                // 使用 BitmapFactory 加载图片文件
//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                if (bitmap != null) {
//                    senderAvatarImageView.setImageBitmap(bitmap);
//                } else {
//                    // 加载失败时显示默认头像
//                    senderAvatarImageView.setImageResource(R.drawable.ic_default_avatar);
//                }
//            } else {
//                // 如果图片路径为空，显示默认头像
//                senderAvatarImageView.setImageResource(R.drawable.ic_default_avatar);
//            }
//
//            // 点击事件监听器
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(chat);
//                }
//            });
//
//            // 长按事件监听器
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    listener.onItemLongClick(chat);
//                    return true;
//                }
//            });
//        }
//    }
//
//    // 点击事件接口
//    public interface OnItemClickListener {
//        void onItemClick(PersonChat chat);
//
//        void onItemLongClick(PersonChat chat);
//    }
//}
