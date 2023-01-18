package com.example.pictureme.views.explore.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.google.android.material.imageview.ShapeableImageView

class ClusterPicmeAdapter(
    private var picmes: List<Picme>
): RecyclerView.Adapter<ClusterPicmeAdapter.ClusterPicmeViewHolder>() {

    inner class ClusterPicmeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePicme: ShapeableImageView
        val textPicme: TextView
        val imageLoadingBar: ContentLoadingProgressBar

        init {
            imagePicme = itemView.findViewById(R.id.image_picme_map)
            textPicme = itemView.findViewById(R.id.text_relative_time_map)
            imageLoadingBar = itemView.findViewById(R.id.image_loading_bar_map)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterPicmeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_picme_map, parent, false)
        return ClusterPicmeViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClusterPicmeViewHolder, position: Int) {
        val picme = picmes[position]
        holder.textPicme.text = Details.getRelativeDate(picme.createdAt!!)
        Pictures.loadPicme(picme.imagePath, holder.imagePicme, holder.imageLoadingBar)
        //holder.imagePicme.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    override fun getItemCount(): Int {
        return picmes.size
    }
}
