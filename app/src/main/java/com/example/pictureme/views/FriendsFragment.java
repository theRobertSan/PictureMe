package com.example.pictureme.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pictureme.adapters.MyAdapter;
import com.example.pictureme.R;

public class FriendsFragment extends Fragment {

    int dummyFriendsImages[] = {R.drawable.ic_avatar, R.drawable.ic_avatar};

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_friends);

        String[] friendNames = getResources().getStringArray(R.array.dummy_friends);
        String[] friendPicMes = getResources().getStringArray(R.array.dummy_friends_picmes);

        MyAdapter myAdapter = new MyAdapter(view.getContext(), friendNames, friendPicMes, dummyFriendsImages);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}