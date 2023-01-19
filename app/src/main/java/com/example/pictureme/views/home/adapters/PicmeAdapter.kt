package com.example.pictureme.views.home.adapters

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Permissions
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.views.home.ParentModelClass
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class PicmeAdapter(
    private var picmes: List<Picme>,
    private val picmeDetailsViewModelViewModel: PicmeDetailsViewModel,
    private val context: Context
) : RecyclerView.Adapter<PicmeAdapter.PicMeViewHolder>() {

    fun setList(newList: List<Picme>) {
        this.picmes = newList
        notifyDataSetChanged()
    }

    inner class PicMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relativeTime: TextView
        val picmeImage: ShapeableImageView
        val loadingBar: ContentLoadingProgressBar
        val cl: ConstraintLayout
        val friendsNum: TextView

        init {
            relativeTime = itemView.findViewById(R.id.text_relative_time)
            picmeImage = itemView.findViewById(R.id.image_picme)
            loadingBar = itemView.findViewById(R.id.image_loading_bar)
            cl = itemView.findViewById(R.id.clItem)
            friendsNum = itemView.findViewById(R.id.textFriendsNum)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicMeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picme, parent, false)
        return PicMeViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PicMeViewHolder, position: Int) {
        holder.relativeTime.text = Details.getRelativeDate(picmes[position].createdAt!!)

        holder.picmeImage.load(picmes[position].imagePath) {
            crossfade(true)
            crossfade(1000)
            listener { _, _ ->
                if (picmes[position].friends.isNotEmpty()) {
                    holder.friendsNum.text = picmes[position].friends.size.toString()
                    holder.friendsNum.visibility = View.VISIBLE
                }
                holder.loadingBar.isGone = true
            }
        }

        holder.cl.setOnClickListener {

            // If required permissions enabled, do something
            Permissions.checkPermissions(
                context,
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                "To view a PicMe's details, you have to enable this permission."
            ) {
                val navController =
                    Navigation.findNavController(holder.itemView.parent.parent.parent.parent.parent.parent.parent.parent.parent as View)
                picmeDetailsViewModelViewModel.selectPicme(picmes[position])
                navController.navigate(R.id.action_navFragment_to_picmeDetailsFragment)
            }


        }
    }

    override fun getItemCount(): Int {
        return picmes.size
    }
}