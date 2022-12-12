package com.example.pictureme.views.addfriends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship

class AddFriendsAdapter(
    var friendships: List<Friendship>
): RecyclerView.Adapter<AddFriendsAdapter.AddFriendsViewHolder>() {

    inner class AddFriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textUsername: TextView
        init {
            textUsername = itemView.findViewById(R.id.textUsername)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_friend, parent, false)
        return AddFriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddFriendsViewHolder, position: Int) {
        holder.textUsername.text = friendships[position].friend!!.username
    }

    override fun getItemCount(): Int {
        return friendships.size
    }

}