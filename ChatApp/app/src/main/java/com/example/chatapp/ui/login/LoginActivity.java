package com.example.chatapp.ui.login;

import static org.koin.java.KoinJavaComponent.inject;

import android.content.Intent;
import android.text.TextUtils;

import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityLoginBinding;
import com.example.chatapp.db.user.User;
import com.example.chatapp.db.user.UserDao;
import com.example.chatapp.ui.home.HomeActivity;
import com.example.chatapp.util.UserCache;

import java.util.List;
import java.util.Objects;

import kotlin.Lazy;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    Lazy<UserDao> userDao = inject(UserDao.class);

    @Override
    public void viewBound() {

        // 点击登录
        getBinding().Loginbutton.setOnClickListener(view -> {
            String phone = getBinding().editTextPhone.getText().toString();
            String password = getBinding().editTextPassword.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                showToast("请输入手机号");
            } else if (TextUtils.isEmpty(password)) {
                showToast("请输入密码");
            } else {
                launchIO(() -> {
                    try {
                        //数据库查询
                        List<User> users = userDao.getValue().login(phone, password);
                        if (users.isEmpty()) {
                            throw new IllegalStateException("账号或密码错误");
                        }
                        UserCache.INSTANCE.setUser(users.get(0));
                        showToast("登录成功");
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        showToast(Objects.requireNonNull(e.getMessage()));
                    }
                    return null;
                });
            }
        });

        // 点击还未注册
        getBinding().register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
