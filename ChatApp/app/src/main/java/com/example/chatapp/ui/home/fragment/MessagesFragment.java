package com.example.chatapp.ui.home.fragment;

import static org.koin.java.KoinJavaComponent.inject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapp.base.BaseFragment;
import com.example.chatapp.databinding.FragmentMessageBinding;
import com.example.chatapp.db.contacts.Contacts;
import com.example.chatapp.db.contacts.ContactsDao;
import com.example.chatapp.db.message.MessageDao;
import com.example.chatapp.db.user.User;
import com.example.chatapp.ui.home.adapter.ContactsAdapter;
import com.example.chatapp.ui.message.P2PMessageActivity;
import com.example.chatapp.util.UserCache;

import java.util.List;

import kotlin.Lazy;

/**
 * 会话列表
 */
public class MessagesFragment extends BaseFragment<FragmentMessageBinding> {

    Lazy<ContactsDao> contactsDao = inject(ContactsDao.class);
    Lazy<MessageDao> messageDao = inject(MessageDao.class);
    private ContactsAdapter contactsAdapter;

    @NonNull
    @Override
    public FragmentMessageBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentMessageBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvTitle.setText("消息");

        contactsAdapter = new ContactsAdapter();

        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().recyclerView.setAdapter(contactsAdapter);

        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        queryAllSessions();
    }

    private void initListener() {
        // 单击会话
        contactsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Contacts contact = contactsAdapter.getItem(i);
            if (contact == null) return;
            Intent intent = new Intent(requireContext(), P2PMessageActivity.class);
            intent.putExtra("friendPhone", contactsAdapter.getFriendAccount(contact));
            intent.putExtra("friendName", contactsAdapter.getFriendName(contact));
            startActivity(intent);
        });
    }

    /**
     * 所有会话
     */
    private void queryAllSessions() {
        showLoadingDialog(null, true);
        launchIO(() -> {
            User user = UserCache.INSTANCE.getUser();
            List<Contacts> contacts = contactsDao.getValue().queryContacts(user.getPhone());
            contacts.removeIf(contact -> !messageDao.getValue().hasSessions(contact.getMyPhone(), contact.getFriendPhone()));
            requireActivity().runOnUiThread(() -> {
                contactsAdapter.submitList(contacts);
                closeLoadingDialog();
            });
            return null;
        });
    }
}