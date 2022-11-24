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

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestViewHolder> {

    Context context;
    String[] names;
    int[] images;

    public FriendRequestsAdapter(Context context, String[] names, int[] images) {
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        holder.friendNameText.setText(names[position]);
        holder.friendProfilePicture.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        TextView friendNameText;
        ImageView friendProfilePicture;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameText = itemView.findViewById(R.id.text_request_name);
            friendProfilePicture = itemView.findViewById(R.id.image_request);
        }
    }

}