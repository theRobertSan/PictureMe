package com.example.pictureme.views.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureme.R;
import com.example.pictureme.models.Picme;

import java.util.List;

public class HomeSectionsAdapter extends RecyclerView.Adapter<HomeSectionsAdapter.PicmeSectionViewHolder> {

    private String[] headers;
    private List<Picme>[] picmeLists;
//    private List<SmallPicMeFragment>[] picmes;

    public HomeSectionsAdapter(String[] headers, List<Picme>[] picmeLists) {
        this.headers = headers;
        this.picmeLists = picmeLists;
    }

    @NonNull
    @Override
    public PicmeSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_element, parent, false);
        return new HomeSectionsAdapter.PicmeSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicmeSectionViewHolder holder, int position) {
        holder.headerText.setText(headers[position]);
        SectionPicmesAdapter mAdapter = new SectionPicmesAdapter(picmeLists[position]);

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.picmesRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(picmeLists[position].size());

        holder.picmesRecyclerView.setAdapter(mAdapter);
        holder.picmesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return headers.length;
    }

    public class PicmeSectionViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;
        RecyclerView picmesRecyclerView;

        public PicmeSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.text_section_header);
            picmesRecyclerView = itemView.findViewById(R.id.recycler_view_section_picmes);
        }

    }
}
