package com.example.pictureme.views.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pictureme.R;
import com.example.pictureme.models.Picme;
import com.example.pictureme.viewmodel.PicmeListViewModel;
import com.example.pictureme.views.home.adapters.HomeSectionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        PicmeListViewModel picmeListViewModel = new ViewModelProvider(getActivity()).get(PicmeListViewModel.class);

        recyclerView = view.findViewById(R.id.recycler_view_sections);

        List<Picme>[] sectionPicmes = new List[3];
        sectionPicmes[0] = new ArrayList<>();
        sectionPicmes[1] = new ArrayList<>();
        sectionPicmes[2] = new ArrayList<>();

        HomeSectionsAdapter mAdapter = new HomeSectionsAdapter(
                new String[]{"PicMe's with friends", "Food PicMe's", "PicMe's from long ago"},
                sectionPicmes
        );

        picmeListViewModel.getUserPicmesLiveData().observe(getViewLifecycleOwner(), picmes -> {
            System.out.println("HELLO1");
            sectionPicmes[0] = picmes;
            sectionPicmes[1] = picmes;
            sectionPicmes[2] = picmes;
            mAdapter.notifyDataSetChanged();
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }
}