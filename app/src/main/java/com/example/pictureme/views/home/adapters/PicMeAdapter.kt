package com.example.pictureme.views.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.views.home.PicMe
import com.example.pictureme.R

class PicMeAdapter(
    private var picmes: List<PicMe>
) : RecyclerView.Adapter<PicMeAdapter.PicMeViewHolder>() {

    inner class PicMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        init {
            textView = itemView.findViewById(R.id.tvTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicMeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picme, parent, false)
        return PicMeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PicMeViewHolder, position: Int) {
        holder.textView.text = picmes[position].title;
    }

    override fun getItemCount(): Int {
        return picmes.size
    }
}