package com.example.chatapp.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.base.BaseFragment;
import com.example.chatapp.databinding.FragmentMeBinding;
import com.example.chatapp.db.user.User;
import com.example.chatapp.ui.login.LoginActivity;
import com.example.chatapp.util.UserCache;

/**
 * 我的
 */
public class MeFragment extends BaseFragment<FragmentMeBinding> {

    @NonNull
    @Override
    public FragmentMeBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentMeBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvTitle.setText("我的");

        // 获取退出登录按钮
        Button btnLogout = getBinding().btnLogout;

        // 设置按钮点击事件监听器
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    // 在某个Activity中执行退出登录操作
    private void logout() {
        UserCache.INSTANCE.setUser(new User("", "", "", ""));
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

}