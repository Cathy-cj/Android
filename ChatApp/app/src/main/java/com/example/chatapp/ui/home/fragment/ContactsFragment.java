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

import com.chad.library.adapter4.BaseQuickAdapter;
import com.example.chatapp.base.BaseFragment;
import com.example.chatapp.databinding.FragmentContactsBinding;
import com.example.chatapp.db.contacts.Contacts;
import com.example.chatapp.db.contacts.ContactsDao;
import com.example.chatapp.db.user.User;
import com.example.chatapp.ui.contacts.AddContactActivity;
import com.example.chatapp.ui.home.adapter.ContactsAdapter;
import com.example.chatapp.ui.message.P2PMessageActivity;
import com.example.chatapp.util.UserCache;

import java.util.List;
import java.util.Objects;

import kotlin.Lazy;

public class ContactsFragment extends BaseFragment<FragmentContactsBinding> {

    Lazy<ContactsDao> contactsDao = inject(ContactsDao.class);

    private ContactsAdapter contactsAdapter;

    @NonNull
    @Override
    public FragmentContactsBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentContactsBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvTitle.setText("通讯录");

        contactsAdapter = new ContactsAdapter();

        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().recyclerView.setAdapter(contactsAdapter);

        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        showLoadingDialog(null, true);
        launchIO(() -> {
            User user = UserCache.INSTANCE.getUser();
            List<Contacts> contacts = contactsDao.getValue().queryContacts(Objects.requireNonNull(user).getPhone());
            requireActivity().runOnUiThread(() -> {
                contactsAdapter.submitList(contacts);
                closeLoadingDialog();
            });
            return null;
        });

    }

    private void initListener() {
        getBinding().ivAdd.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), AddContactActivity.class);
            startActivity(intent);
        });

        // 单击联系人
        contactsAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Contacts contact = contactsAdapter.getItem(i);
            if (contact == null) return;
            Intent intent = new Intent(requireContext(), P2PMessageActivity.class);
            intent.putExtra("friendPhone", contactsAdapter.getFriendAccount(contact));
            intent.putExtra("friendName", contactsAdapter.getFriendName(contact));
            startActivity(intent);
        });

        // 长按联系人
        contactsAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener<Contacts>() {
            @Override
            public boolean onLongClick(@NonNull BaseQuickAdapter<Contacts, ?> baseQuickAdapter, @NonNull View view, int i) {
                //
                return false;
            }
        });
    }
}
