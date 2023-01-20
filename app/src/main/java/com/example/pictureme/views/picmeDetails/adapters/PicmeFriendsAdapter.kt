package com.example.pictureme.views.picmeDetails.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.User
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.google.android.material.imageview.ShapeableImageView

class PicmeFriendsAdapter(
    private var friends: List<User>,
) : RecyclerView.Adapter<PicmeFriendsAdapter.PicmeFriendsViewHolder>() {

    inner class PicmeFriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFriend: TextView
        val imageFriend: ShapeableImageView
        val imageLoadingBar: ContentLoadingProgressBar

        init {
            textFriend = itemView.findViewById(R.id.textFriend)
            imageFriend = itemView.findViewById(R.id.imageFriend)
            imageLoadingBar = itemView.findViewById(R.id.imageLoadingBar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicmeFriendsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_picme_friend, parent, false)
        return PicmeFriendsViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PicmeFriendsViewHolder, position: Int) {
        holder.textFriend.text = Details.getPrettyNameFormat(friends[position].fullName!!)
        Pictures.loadProfilePicture(
            friends[position].profilePicturePath,
            holder.imageFriend,
            holder.imageLoadingBar
        )

    }

    override fun getItemCount(): Int {
        return friends.size
    }
}