package com.example.pictureme.views.profile.friends.tabs.friendRequests

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.FriendRequest
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.UserViewModel
import com.google.android.material.imageview.ShapeableImageView

class FriendRequestsAdapter(
    private var friendRequests: List<FriendRequest>,
    private val userViewModel: UserViewModel
) : RecyclerView.Adapter<FriendRequestsAdapter.RequestsViewHolder>() {

    inner class RequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO users profile picture
        val sendingUserFullName: TextView
        val sendingUserUsername: TextView
        val requestSentDate: TextView
        val acceptButton: Button
        val declineButton: Button
        val imageUser: ShapeableImageView
        val imageLoadingBar: ContentLoadingProgressBar

        init {
            sendingUserFullName = itemView.findViewById(R.id.sending_user_full_name)
            sendingUserUsername = itemView.findViewById(R.id.sending_user_username)
            requestSentDate = itemView.findViewById(R.id.request_date)
            acceptButton = itemView.findViewById(R.id.button_accept)
            declineButton = itemView.findViewById(R.id.button_decline)
            imageUser = itemView.findViewById(R.id.friend_profile_pic)
            imageLoadingBar = itemView.findViewById(R.id.imageLoadingBar)
        }

        fun updateRequestStatus(request: FriendRequest) {
            acceptButton.setOnClickListener {
                userViewModel.handleFriendRequestAnswer(request.id!!, true)
                // Delete request artificially to speed up the deletion
                deleteRequest(request)

                Toast.makeText(it.context, "You are now friends with " + request.sendingUser!!.fullName, Toast.LENGTH_SHORT).show()
            }

            declineButton.setOnClickListener {
                userViewModel.handleFriendRequestAnswer(request.id!!, false)
                // Delete request artificially to speed up the deletion
                deleteRequest(request)

                Toast.makeText(it.context, "You deleted " + request.sendingUser!!.fullName + " friend request", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setFriendRequests(newList: List<FriendRequest>) {
        this.friendRequests = newList
        notifyDataSetChanged()
    }

    fun deleteRequest(request: FriendRequest) {
        val mutableList = this.friendRequests.toMutableList()
        for ((i, r) in friendRequests.withIndex()) {
            if (r.id == request.id) {
                mutableList.removeAt(i)
            }
        }
        this.friendRequests = mutableList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendRequestsAdapter.RequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend_request, parent, false)

        return RequestsViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
        holder.updateRequestStatus(friendRequests[position])

        holder.sendingUserFullName.text = friendRequests[position].sendingUser!!.fullName
        holder.sendingUserUsername.text = friendRequests[position].sendingUser!!.username

        val relativeDateRequest = Details.getRelativeDate(friendRequests[position].sentAt!!)
        holder.requestSentDate.text = relativeDateRequest

        // Load friend's picture
        Pictures.loadProfilePicture(
            friendRequests[position].sendingUser!!.profilePicturePath,
            holder.imageUser,
            holder.imageLoadingBar
        )
    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }
}