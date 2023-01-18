package com.example.pictureme.views.addfriends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.PreviewPicmeViewModel
import com.google.android.material.imageview.ShapeableImageView

class AddFriendsAdapter(
    var friendships: List<Friendship>,
    val previewPicmeViewModel: PreviewPicmeViewModel
) : RecyclerView.Adapter<AddFriendsAdapter.AddFriendsViewHolder>() {

    inner class AddFriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textUserFullName: TextView
        val textUserUsername: TextView
        val cbAddFriend: CheckBox
        val imageFriend: ShapeableImageView
        val imageLoadingBar: ContentLoadingProgressBar

        init {
            textUserFullName = itemView.findViewById(R.id.text_user_full_name)
            textUserUsername = itemView.findViewById(R.id.text_user_username)
            cbAddFriend = itemView.findViewById(R.id.cbAddFriend)
            imageFriend = itemView.findViewById(R.id.imageUserPicture)
            imageLoadingBar = itemView.findViewById(R.id.imageLoadingBar)
        }
    }

    fun setList(newList: List<Friendship>) {
        this.friendships = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_friend, parent, false)
        return AddFriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddFriendsViewHolder, position: Int) {
        holder.textUserFullName.text = friendships[position].friend!!.fullName
        holder.textUserUsername.text = friendships[position].friend!!.username

        // Load friend's picture
        Pictures.loadProfilePicture(
            friendships[position].friend!!.profilePicturePath,
            holder.imageFriend,
            holder.imageLoadingBar
        )

        val userId = friendships[position].friend!!.id!!

        // Check if this user was selected before
        holder.cbAddFriend.isChecked = previewPicmeViewModel.containsFriend(userId)

        // When checked, add on remove friend from list
        holder.cbAddFriend.setOnClickListener { view ->
            if (holder.cbAddFriend.isChecked) {
                previewPicmeViewModel.selectFriend(userId)
            } else {
                previewPicmeViewModel.unselectFriend(userId)
            }

        }

    }

    override fun getItemCount(): Int {
        return friendships.size
    }

}