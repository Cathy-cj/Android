package com.example.chatapp.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatapp.ui.home.fragment.ContactsFragment;
import com.example.chatapp.ui.home.fragment.MeFragment;
import com.example.chatapp.ui.home.fragment.MessagesFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int size;

    public ViewPagerAdapter(int num, @NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.size = num;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MessagesFragment();
            case 1:
                return new ContactsFragment();
            case 2:
                return new MeFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setItemAnimator(null);
    }
}
