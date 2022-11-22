package com.example.pictureme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureme.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    String[] names, counts;
    int[] images;

    public MyAdapter(Context context, String[] names, String[] counts, int[] images) {
        this.context = context;
        this.names = names;
        this.counts = counts;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_element, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.friendNameText.setText(names[position]);
        holder.countText.setText("You took " + counts[position] + " PicMe's together");
        holder.friendProfilePicture.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView friendNameText, countText;
        ImageView friendProfilePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameText = itemView.findViewById(R.id.text_friend_name);
            countText = itemView.findViewById(R.id.text_friend_count);
            friendProfilePicture = itemView.findViewById(R.id.image_friend);
        }
    }

}
