package com.example.pictureme.views.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView

class ImageAdapter(
    private var thumbIds: Array<Int>,
    var context: Context
) : BaseAdapter() {

    var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return thumbIds.size;
    }

    override fun getItem(position: Int): Any {
        return thumbIds[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val imageView = ImageView(context)
        imageView.setImageResource(thumbIds[position])
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = AbsListView.LayoutParams(400, 350)

        return imageView
    }
}