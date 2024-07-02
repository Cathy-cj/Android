package com.example.chatapp.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.example.chatapp.R;
import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityHomeBinding;
import com.example.chatapp.databinding.TabViewMenuBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private final String[] tabs = new String[]{"通讯录", "我的"};
    private final int[] selectedIcons = new int[]{
            // R.drawable.baseline_message_24,
            R.drawable.baseline_contacts_24,
            R.drawable.baseline_person_24,
    };
    private final int[] unSelectedIcons = new int[]{
            // R.drawable.baseline_message_24_grey,
            R.drawable.baseline_contacts_24_grey,
            R.drawable.baseline_person_24_grey,
    };

    private final TabViewMenuBinding[] tabViewMenuBindings = new TabViewMenuBinding[3];

    @SuppressLint({"InflateParams"})
    @Override
    public void viewBound() {
        // Set up ViewPager with adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(tabs.length, this);

        getBinding().viewPager.setAdapter(adapter);
        getBinding().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                tabViewMenuBindings[index].ivIcon.setBackgroundResource(selectedIcons[index]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                tabViewMenuBindings[index].ivIcon.setBackgroundResource(unSelectedIcons[index]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Connect TabLayout with ViewPager
        new TabLayoutMediator(getBinding().tabLayout, getBinding().viewPager, (tab, position) -> {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_view_menu, null);
            TabViewMenuBinding viewMenuBinding = TabViewMenuBinding.bind(view);
            tabViewMenuBindings[position] = viewMenuBinding;
            viewMenuBinding.tvItem.setText(tabs[position]);
            if (position == 0) {
                viewMenuBinding.ivIcon.setBackgroundResource(selectedIcons[position]);
            } else {
                viewMenuBinding.ivIcon.setBackgroundResource(unSelectedIcons[position]);
            }
            tab.setCustomView(view);
        }).attach();
    }
}
