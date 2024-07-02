package com.example.chatapp.ui.contacts;

import static org.koin.java.KoinJavaComponent.inject;

import android.text.TextUtils;
import android.view.View;

import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityAddContactBinding;
import com.example.chatapp.db.contacts.Contacts;
import com.example.chatapp.db.contacts.ContactsDao;
import com.example.chatapp.db.user.User;
import com.example.chatapp.db.user.UserDao;
import com.example.chatapp.util.UserCache;

import java.util.List;

import kotlin.Lazy;

public class AddContactActivity extends BaseActivity<ActivityAddContactBinding> {

    Lazy<UserDao> userDao = inject(UserDao.class);
    Lazy<ContactsDao> contactsDao = inject(ContactsDao.class);

    @Override
    public void viewBound() {
        // 不需要姓名查询 phone 做为唯一主键，姓名是可重复的
        getBinding().contactName.setVisibility(View.GONE);

        // 点击返回
        getBinding().backAddcontact.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        // 点击添加联系人
        getBinding().addContactButton.setOnClickListener(view -> {
            String phone = getBinding().contactPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                showToast("请先输入要添加的手机号");
            } else if (phone.equals(UserCache.INSTANCE.getUser().getPhone())) {
                showToast("你不能添加自己为好友");
            } else {
                launchIO(() -> {
                    try {
                        List<User> users = userDao.getValue().queryUser(phone);
                        if (users.isEmpty()) {
                            showToast("该账号不存在");
                        } else if (contactsDao.getValue().isFriend(UserCache.INSTANCE.getUser().getPhone(), users.get(0).getPhone())) {
                            showToast("你们已是好友，无法重复添加");
                        } else {
                            User addUser = users.get(0);
                            Contacts contact = new Contacts(
                                    UserCache.INSTANCE.getUser().getPhone(),
                                    UserCache.INSTANCE.getUser().getNikeName(),
                                    UserCache.INSTANCE.getUser().getNikeName(),
                                    addUser.getPhone(),
                                    addUser.getNikeName(),
                                    addUser.getNikeName(),
                                    false
                            );
                            contactsDao.getValue().addContact(contact);
                            showToast("添加成功");
                            finish();
                        }
                    } catch (Exception e) {
                        showToast("添加联系人失败");
                    }
                    return null;
                });
            }
        });
    }
}
