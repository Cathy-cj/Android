package com.example.chatapp.ui.login;

import static org.koin.java.KoinJavaComponent.inject;

import android.text.TextUtils;

import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityRegisterBinding;
import com.example.chatapp.db.user.User;
import com.example.chatapp.db.user.UserDao;

import kotlin.Lazy;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    //依赖
    Lazy<UserDao> userDao = inject(UserDao.class);

    @Override
    public void viewBound() {
        // 点击返回
        getBinding().back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // 点击注册
        getBinding().registerButton.setOnClickListener(v -> {
            String nickName = getBinding().nickname.getText().toString();
            String phone = getBinding().phone.getText().toString();
            String password = getBinding().password.getText().toString();
            if (TextUtils.isEmpty(nickName)) {
                showToast("请输入用户名");
            } else if (TextUtils.isEmpty(phone)) {
                showToast("请输入手机号");
            } else if (TextUtils.isEmpty(password)) {
                showToast("请输入密码");
            } else {
                launchIO(() -> {
                    try {
                        User user = new User(phone, nickName, password, "null");
                        // 注册用户的数据库操作
                        userDao.getValue().register(user);
                        showToast("注册成功");
                        finish();
                    } catch (Exception e) {
                        showToast("注册失败, 手机号已存在");
                    }
                    return null;
                });
            }
        });
    }
}
