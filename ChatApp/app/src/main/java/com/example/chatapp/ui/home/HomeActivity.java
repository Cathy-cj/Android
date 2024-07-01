package com.example.chatapp.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.example.chatapp.R;
import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityHomeBinding;
import com.example.chatapp.databinding.TabViewMenuBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private final String[] tabs = new String[]{"消息", "通讯录", "我的"};
    private final int[] selectedIcons = new int[]{
            R.drawable.baseline_message_24,
            R.drawable.baseline_contacts_24,
            R.drawable.baseline_person_24_grey,
    };
    private final int[] unSelectedIcons = new int[]{
            R.drawable.baseline_message_24_grey,
            R.drawable.baseline_contacts_24_grey,
            R.drawable.baseline_person_24_grey,
    };

    private final List<TabViewMenuBinding> tabViewMenuBindings = List.of(null, null, null);

    @SuppressLint("InflateParams")
    @Override
    public void viewBound() {
        // Set up ViewPager with adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        getBinding().viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(getBinding().tabLayout, getBinding().viewPager, (tab, position) -> {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_view_menu, null);
            TabViewMenuBinding viewMenuBinding = TabViewMenuBinding.bind(view);
            tabViewMenuBindings.set(position, viewMenuBinding);
            viewMenuBinding.ivIcon.setImageDrawable(g);
        }).attach();
    }
}
