package com.example.pictureme.views.explore.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.navigation.Navigation
import coil.load
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.viewmodels.PicmeDetailsViewModel

class ImageAdapter(
    private var picmes: List<Picme>,
    var context: Context,
) : BaseAdapter() {

    var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return picmes.size;
    }

    override fun getItem(position: Int): Any {
        return picmes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val imageView = ImageView(context)
        imageView.load(picmes[position].imagePath) {
            crossfade(true)
            crossfade(1000)
        }

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = AbsListView.LayoutParams(400, 350)

        return imageView
    }
}