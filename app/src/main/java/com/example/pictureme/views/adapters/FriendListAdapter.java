package com.example.pictureme.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictureme.R;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    Context context;
    String[] names, counts;
    int[] images;

    public FriendListAdapter(Context context, String[] names, String[] counts, int[] images) {
        this.context = context;
        this.names = names;
        this.counts = counts;
        this.images = images;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_element, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.friendNameText.setText(names[position]);
        holder.countText.setText("You took " + counts[position] + " PicMe's together");
        holder.friendProfilePicture.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friendNameText, countText;
        ImageView friendProfilePicture;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameText = itemView.findViewById(R.id.text_friend_name);
            countText = itemView.findViewById(R.id.text_friend_count);
            friendProfilePicture = itemView.findViewById(R.id.image_friend);
        }
    }

}
