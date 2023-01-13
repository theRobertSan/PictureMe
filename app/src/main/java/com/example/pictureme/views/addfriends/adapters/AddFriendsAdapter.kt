package com.example.pictureme.views.addfriends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.viewmodels.PreviewPicmeViewModel

class AddFriendsAdapter(
    var friendships: List<Friendship>,
    val previewPicmeViewModel: PreviewPicmeViewModel
) : RecyclerView.Adapter<AddFriendsAdapter.AddFriendsViewHolder>() {

    inner class AddFriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textUsername: TextView
        val cbAddFriend: CheckBox

        init {
            textUsername = itemView.findViewById(R.id.textUsername)
            cbAddFriend = itemView.findViewById(R.id.cbAddFriend)
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
        holder.textUsername.text = friendships[position].friend!!.username

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