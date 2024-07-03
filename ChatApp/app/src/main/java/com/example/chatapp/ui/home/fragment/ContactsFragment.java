package com.example.chatapp.ui.home.fragment;

import static org.koin.java.KoinJavaComponent.inject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;

import java.util.List;
import java.util.Objects;

import kotlin.Lazy;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * 通讯录
 */
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
        queryAllContacts();
    }

    /**
     * 所有联系人
     */
    private void queryAllContacts() {
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
                Contacts contact = contactsAdapter.getItem(i);
                showContactsLongClickDialog(contact);
                return true;
            }
        });

        // 搜索框监听
        getBinding().searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    queryAllContacts();
                } else {
                    searchContacts(s.toString());
                }
            }
        });
    }


    /**
     * 长按联系人弹窗
     */
    private void showContactsLongClickDialog(Contacts contact) {
        BottomMenu.show(List.of("修改昵称", "删除联系人"))
                .setOnMenuItemClickListener((dialog, text, index) -> {
                    if (index == 0) {
                        showModifierContactNicknameDialog(contact);
                    } else if (index == 1) {
                        showDeletedContactDialog(contact);
                    }
                    return false;
                });
    }

    /**
     * 删除联系人提示
     */
    private void showDeletedContactDialog(Contacts contact) {
        new MessageDialog("提示", "确认删除该联系人吗", "确定", "取消")
                .setOkButtonClickListener((dialog, v) -> {
                    launchIO(() -> {
                        try {
                            contactsDao.getValue().deleteContacts(contact.getMyPhone(), contact.getFriendPhone());
                            requireActivity().runOnUiThread(() -> {
                                contactsAdapter.remove(contact);
                                showToast("删除成功");
                            });
                        } catch (Exception e) {
                            showToast("删除失败");
                        }
                        return null;
                    });
                    return false;
                })
                .setCancelButtonClickListener((dialog, v) -> {
                    dialog.dismiss();
                    return false;
                }).show();
    }

    /**
     * 修改联系人昵称
     */
    private void showModifierContactNicknameDialog(Contacts contact) {
        InputDialog.show("tips", "请输入联系人昵称", "确定")
                .setOkButtonClickListener((dialog, v, inputStr) -> {
                    launchIO(() -> {
                        try {
                            if (contactIsMe(contact)) {
                                contactsDao.getValue().updateNickName(
                                        contact.getMyPhone(),
                                        contact.getFriendPhone(),
                                        contact.getMyNickName(),
                                        inputStr
                                );
                            } else {
                                contactsDao.getValue().updateNickName(
                                        contact.getMyPhone(),
                                        contact.getFriendPhone(),
                                        inputStr,
                                        contact.getFriendNickName()
                                );
                            }
                            queryAllContacts();
                            showToast("修改成功");
                        } catch (Exception e) {
                            showToast("修改失败");
                        }
                        return null;
                    });
                    return false;
                });
    }

    /**
     * 搜索联系人
     */
    private void searchContacts(String keyword) {
        launchIO(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                try {
                    List<Contacts> contacts = contactsDao.getValue().searchContacts(keyword);
                    requireActivity().runOnUiThread(() -> {
                        contactsAdapter.submitList(contacts);
                    });
                } catch (Exception e) {
                    showToast("搜索失败");
                }
                return null;
            }
        });
    }

    private boolean contactIsMe(Contacts contact) {
        return contact.getMyPhone().equals(UserCache.INSTANCE.getUser().getPhone());
    }
}
