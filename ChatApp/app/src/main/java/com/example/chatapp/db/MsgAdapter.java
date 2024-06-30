//package com.example.chatapp.db;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatapp.R;
//import com.example.chatapp.ui.PersonChat;
//
//import java.util.List;
//
//public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
//    private List<PersonChat> mChatList;
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView messageText;
//
//        public ViewHolder(View view) {
//            super(view);
//            messageText = view.findViewById(R.id.message_text);
//        }
//    }
//
//    public MsgAdapter(List<PersonChat> chatList) {
//        mChatList = chatList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_item_message, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        PersonChat chat = mChatList.get(position);
//        holder.messageText.setText(chat.getContent());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mChatList.size();
//    }
//}
