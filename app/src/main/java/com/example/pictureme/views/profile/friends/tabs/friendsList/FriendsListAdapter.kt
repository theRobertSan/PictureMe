package com.example.pictureme.views.profile.friends.tabs.friendsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.utils.Pictures
import com.google.android.material.imageview.ShapeableImageView

class FriendsListAdapter(
    private var friendships: List<Friendship>,
    private var numPicmesWithEachFriend: HashMap<String, Int>
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {
    val PICMES_TOGETHER = " PicMe's together"
    val ONE_PICME_TOGETHER = "1 PicMe together"
    val NO_PICMES_TOGETHER = "No PicMe's together"

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendUsername: TextView
        val numPicmesTogether: TextView
        val imageFriend: ShapeableImageView
        val imageLoadingBar: ContentLoadingProgressBar

        init {
            friendUsername = itemView.findViewById(R.id.friend_username)
            numPicmesTogether = itemView.findViewById(R.id.num_picmes_together)
            imageFriend = itemView.findViewById(R.id.friend_profile_pic)
            imageLoadingBar = itemView.findViewById(R.id.imageLoadingBar)
        }
    }

    fun setFriendListAndPicmeNum(
        newList: List<Friendship>,
        numPicmesWithEachFriend: HashMap<String, Int>
    ) {
        this.friendships = newList
        this.numPicmesWithEachFriend = numPicmesWithEachFriend
        notifyDataSetChanged()
    }

    fun setFriendList(newList: List<Friendship>) {
        this.friendships = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsListAdapter.FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsListAdapter.FriendsViewHolder, position: Int) {
        val currentFriend = friendships[position].friend!!

        holder.friendUsername.text = currentFriend.username
        if (numPicmesWithEachFriend[currentFriend.id]!! > 1) {
            holder.numPicmesTogether.text =
                numPicmesWithEachFriend[currentFriend.id].toString() + PICMES_TOGETHER
        } else if (numPicmesWithEachFriend[currentFriend.id] == 1) {
            holder.numPicmesTogether.text = ONE_PICME_TOGETHER
        } else {
            holder.numPicmesTogether.text = NO_PICMES_TOGETHER
        }

        // Load friend's picture
        Pictures.loadProfilePicture(
            friendships[position].friend!!.profilePicturePath,
            holder.imageFriend,
            holder.imageLoadingBar
        )
    }

    override fun getItemCount(): Int {
        return friendships.size
    }
}