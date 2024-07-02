package com.example.chatapp.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.base.BaseFragment;
import com.example.chatapp.databinding.FragmentMessageBinding;

/**
 * 会话列表
 */
public class MessagesFragment extends BaseFragment<FragmentMessageBinding> {

    @NonNull
    @Override
    public FragmentMessageBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentMessageBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvTitle.setText("消息");
    }
}