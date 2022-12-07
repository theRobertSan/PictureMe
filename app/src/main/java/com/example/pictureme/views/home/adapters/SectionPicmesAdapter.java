package com.example.pictureme.views.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureme.R;
import com.example.pictureme.models.Picme;

import java.util.List;

public class SectionPicmesAdapter extends RecyclerView.Adapter<SectionPicmesAdapter.SectionPicMeViewHolder> {

    private List<Picme> testList;

    public SectionPicmesAdapter(List<Picme> testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public SectionPicMeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_picme_element, parent, false);
        return new SectionPicmesAdapter.SectionPicMeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionPicMeViewHolder holder, int position) {
        String test = testList.get(position).getFeeling();
        holder.testText.setText(test);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class SectionPicMeViewHolder extends RecyclerView.ViewHolder {
        TextView testText;

        public SectionPicMeViewHolder(View itemView) {
            super(itemView);
            testText = itemView.findViewById(R.id.text_section_picme_test);
        }
    }
}
