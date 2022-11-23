package com.example.pictureme.views.friends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pictureme.R;
import com.example.pictureme.views.friends.FriendListFragment;
import com.example.pictureme.views.friends.FriendRequestsFragment;
import com.google.android.material.tabs.TabLayout;

public class FriendsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.friends_fragment_container, FriendListFragment.class, null).commit();
        TabLayout friendsTab = view.findViewById(R.id.tabs);

        friendsTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.friends_fragment_container, FriendListFragment.class, null).commit();
                        break;
                    default:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.friends_fragment_container, FriendRequestsFragment.class, null).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}