package com.example.pictureme.views.profile.friends.tabs.friendsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship

class FriendsListAdapter (
    private var friendships: List<Friendship>,
    private val numPicmesWithEachFriend: HashMap<String, Int>
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {
    val PICMES_TOGETHER = " PicMe's together"
    val NO_PICMES_TOGETHER = "No PicMe's together"

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO friends profile picture
        val friendUsername: TextView
        val numPicmesTogether: TextView

        init {
            friendUsername = itemView.findViewById(R.id.friend_username)
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
        val currentFriend = friendships[position].friend!!

        holder.friendUsername.text = currentFriend.username
        if (numPicmesWithEachFriend[currentFriend.id] != 0) {
            holder.numPicmesTogether.text = numPicmesWithEachFriend[currentFriend.id].toString() + PICMES_TOGETHER
        } else {
            holder.numPicmesTogether.text = NO_PICMES_TOGETHER
        }
    }

    override fun getItemCount(): Int {
        return friendships.size
    }
}