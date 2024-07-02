package com.example.chatapp.ui.home.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.example.chatapp.R;
import com.example.chatapp.db.contacts.Contacts;
import com.example.chatapp.util.UserCache;

import org.jetbrains.annotations.NotNull;

public class ContactsAdapter extends BaseQuickAdapter<Contacts, QuickViewHolder> {

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int i, @Nullable Contacts item) {
        if (item == null) return;
        if (item.getMyPhone().equals(UserCache.INSTANCE.getUser().getPhone())) {
            holder.setText(R.id.tvNickName, item.getFriendName());
            holder.setText(R.id.tvPhone, item.getFriendPhone());
            // head pic
        } else {
            holder.setText(R.id.tvNickName, item.getMyName());
            holder.setText(R.id.tvPhone, item.getMyPhone());
        }
    }

    @NotNull
    public String getFriendAccount(Contacts contact) {
        if (contact.getMyPhone().equals(UserCache.INSTANCE.getUser().getPhone())) {
            return contact.getFriendPhone();
        } else {
            return contact.getMyPhone();
        }
    }

    @NotNull
    public String getFriendName(Contacts contact) {
        if (contact.getMyPhone().equals(UserCache.INSTANCE.getUser().getPhone())) {
            return contact.getFriendName();
        } else {
            return contact.getFriendPhone();
        }
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_contacts, viewGroup);
    }
}
