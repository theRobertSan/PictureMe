package com.example.pictureme.views.home.adapters

import android.graphics.BitmapFactory
import android.icu.text.RelativeDateTimeFormatter
import android.os.Build
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.google.android.material.imageview.ShapeableImageView

class PicmeAdapter(
    private var picmes: List<Picme>
) : RecyclerView.Adapter<PicmeAdapter.PicMeViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.N)
    val dateFormater = RelativeDateTimeFormatter.getInstance()

    inner class PicMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relativeTime: AppCompatCheckedTextView
        val picmeImage: ShapeableImageView
        val loadingBar: ContentLoadingProgressBar

        init {
            relativeTime = itemView.findViewById(R.id.text_relative_time)
            picmeImage = itemView.findViewById(R.id.image_picme)
            loadingBar = itemView.findViewById(R.id.image_loading_bar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicMeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picme, parent, false)
        return PicMeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PicMeViewHolder, position: Int) {
        val relativeDate = DateUtils.getRelativeTimeSpanString(
            picmes[position].createdAt!!.seconds * 1000,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS
        );
        holder.relativeTime.text = relativeDate

        holder.picmeImage.load(picmes[position].imagePath) {
            crossfade(true)
            crossfade(1000)
            listener { request, result ->
                holder.loadingBar.isGone = true
            }
        }
        //Picasso.get().load(picmes[position].imagePath).into(holder.picmeImage)
//        val takenPicture = BitmapFactory.decodeFile(picmes[position].imageFile?.absolutePath)
//        holder.picmeImage.setImageBitmap(takenPicture)
    }

    override fun getItemCount(): Int {
        return picmes.size
    }
}