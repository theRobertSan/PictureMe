package com.example.pictureme.views.explore.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.google.android.material.imageview.ShapeableImageView

class ClusterPicmeAdapter(
    private var picmes: List<Picme>,
    private val picmeDetailsViewModel: PicmeDetailsViewModel
) : RecyclerView.Adapter<ClusterPicmeAdapter.ClusterPicmeViewHolder>() {

    inner class ClusterPicmeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePicme: ShapeableImageView
        val textPicme: TextView
        val imageLoadingBar: ContentLoadingProgressBar
        val mapPicme: ConstraintLayout
        val textFriendsNum: TextView

        init {
            imagePicme = itemView.findViewById(R.id.image_picme_map)
            textPicme = itemView.findViewById(R.id.text_relative_time_map)
            imageLoadingBar = itemView.findViewById(R.id.image_loading_bar_map)
            mapPicme = itemView.findViewById(R.id.map_picme_layout)
            textFriendsNum = itemView.findViewById(R.id.textFriendsNum)
        }

        fun navigateToClickedPicme(picme: Picme) {
            mapPicme.setOnClickListener {
                // This is a sin
                val navigation =
                    Navigation.findNavController(itemView.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent as View)
                picmeDetailsViewModel.selectPicme(picme)
                navigation.navigate(R.id.action_navFragment_to_picmeDetailsFragment)
            }

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
        holder.navigateToClickedPicme(picme)

        holder.textPicme.text = Details.getRelativeDate(picme.createdAt!!)
        Pictures.loadPicme(picme.imagePath, holder.imagePicme, holder.imageLoadingBar)
        if (picme.friends.isNotEmpty()) {
            holder.textFriendsNum.text = picme.friends.size.toString()
            holder.textFriendsNum.visibility = View.VISIBLE
        }
        Pictures.loadPicme(
            picme.imagePath,
            holder.imagePicme,
            holder.imageLoadingBar
        )
    }

    override fun getItemCount(): Int {
        return picmes.size
    }
}
