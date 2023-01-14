package com.example.pictureme.views.profile.friends.tabs.friendsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship

class FriendsListAdapter (
    private var friendships: List<Friendship>
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO friends profile picture
        val friendName: TextView
        val numPicmesTogether: TextView

        init {
            friendName = itemView.findViewById(R.id.friend_name)
            numPicmesTogether = itemView.findViewById(R.id.num_picmes_together)
        }
    }

    fun setFriendList(newList: List<Friendship>) {
        this.friendships = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListAdapter.FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsListAdapter.FriendsViewHolder, position: Int) {
        holder.friendName.text = friendships[position].friend!!.username
    }

    override fun getItemCount(): Int {
        return friendships.size
    }
}