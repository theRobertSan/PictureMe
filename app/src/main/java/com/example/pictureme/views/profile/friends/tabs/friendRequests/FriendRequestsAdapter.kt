package com.example.pictureme.views.profile.friends.tabs.friendRequests

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.FriendRequest
import com.example.pictureme.utils.Details

class FriendRequestsAdapter (
        private var friendRequests: List<FriendRequest>
        ) : RecyclerView.Adapter<FriendRequestsAdapter.RequestsViewHolder>() {

        inner class RequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                // TODO users profile picture
                val sendingUserUsername: TextView
                val requestSentDate: TextView
                val acceptButton: Button
                val declineButton: Button

                init {
                        sendingUserUsername = itemView.findViewById(R.id.sending_user_username)
                        requestSentDate = itemView.findViewById(R.id.request_date)
                        acceptButton = itemView.findViewById(R.id.button_accept)
                        declineButton = itemView.findViewById(R.id.button_decline)
                }

                fun updateRequestStatus(request: FriendRequest) {
                        acceptButton.setOnClickListener{
                                deleteRequest(request)
                        }

                        declineButton.setOnClickListener{
                                deleteRequest(request)
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestsAdapter.RequestsViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_friend_request, parent, false)

                return RequestsViewHolder(view)
        }


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
                holder.updateRequestStatus(friendRequests[position])

                holder.sendingUserUsername.text = friendRequests[position].sendingUser!!.username

                val relativeDateRequest = Details.getRelativeDate(friendRequests[position].sentAt!!)
                holder.requestSentDate.text = relativeDateRequest
        }

        override fun getItemCount(): Int {
                return friendRequests.size
        }
}