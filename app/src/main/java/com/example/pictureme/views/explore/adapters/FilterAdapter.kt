package com.example.pictureme.views.explore.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Filter
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.User
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.imageview.ShapeableImageView

class FilterAdapter(
    var friendships: List<Friendship>,
    var filter: Filter
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {
    var selectedFriends = ArrayList<User>()
    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFriend: TextView
        val cbSelectFriend: MaterialCheckBox
        val imageFriend: ShapeableImageView
        val imageLoadingBar: ContentLoadingProgressBar

        init {
            textFriend = itemView.findViewById(R.id.textFriend)
            cbSelectFriend = itemView.findViewById(R.id.cbSelectFriend)
            imageFriend = itemView.findViewById(R.id.imageFriend)
            imageLoadingBar = itemView.findViewById(R.id.imageLoadingBar)
        }
    }

    fun setList(newList: List<Friendship>) {
        this.friendships = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter_friend, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.textFriend.text = Details.getPrettyNameFormat(friendships[position].friend!!.fullName!!)
        val userId = friendships[position].friend!!.id!!

        // Check if this user was selected before
        holder.cbSelectFriend.isChecked = filter.friendIds.contains(userId)

        // When checked, add on remove friend from list
        holder.cbSelectFriend.setOnClickListener { view ->

            if (holder.cbSelectFriend.isChecked) {
                filter.friendIds.add(userId)
            } else {
                filter.friendIds.remove(userId)
            }
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