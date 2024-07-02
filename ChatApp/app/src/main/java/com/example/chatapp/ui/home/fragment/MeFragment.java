package com.example.chatapp.ui.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.ui.login.LoginActivity;
import com.example.chatapp.base.BaseFragment;
import com.example.chatapp.databinding.FragmentMeBinding;

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
                // 清空用户数据（例如SharedPreferences）
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // 跳转到登录界面
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish(); // 关闭当前活动
            }
        });
    }
}