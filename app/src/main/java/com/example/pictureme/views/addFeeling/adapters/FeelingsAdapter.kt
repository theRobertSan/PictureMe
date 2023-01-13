package com.example.pictureme.views.addFeeling.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.viewmodels.PreviewPicmeViewModel

class FeelingsAdapter(
    var feelings: List<Feeling>,
    val previewPicmeViewModel: PreviewPicmeViewModel
) : RecyclerView.Adapter<FeelingsAdapter.FeelingsViewHolder>() {

    inner class FeelingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFeeling: TextView
        val cbSelectFeeling: CheckBox

        init {
            textFeeling = itemView.findViewById(R.id.textFeeling)
            cbSelectFeeling = itemView.findViewById(R.id.cbSelectFeeling)
        }
    }

    fun setList(newList: List<Feeling>) {
        this.feelings = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeelingsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_feeling, parent, false)
        return FeelingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeelingsViewHolder, position: Int) {
        holder.textFeeling.text = feelings[position].feeling

        val feelingId = feelings[position].id

//        // Check if this user was selected before
//        holder.cbSelectFeeling.isChecked = previewPicmeViewModel.containsFriend(userId)
//
//        // When checked, add on remove friend from list
//        holder.cbAddFriend.setOnClickListener { view ->
//            if (holder.cbAddFriend.isChecked) {
//                previewPicmeViewModel.selectFriend(userId)
//            } else {
//                previewPicmeViewModel.unselectFriend(userId)
//            }
//
//        }

    }

    override fun getItemCount(): Int {
        return feelings.size
    }
}