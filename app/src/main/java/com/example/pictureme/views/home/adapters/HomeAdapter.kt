package com.example.pictureme.views.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.views.home.ParentModelClass
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.viewmodels.PicmeDetailsViewModel

class HomeAdapter(
    private var rvs: List<ParentModelClass>,
    private val picmeDetailsViewModelViewModel: PicmeDetailsViewModel
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    fun setList(newList: List<ParentModelClass>) {
        println("ADAPTER CHANGED")
        this.rvs = newList
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val rvCategory: RecyclerView

        init {
            tvTitle = itemView.findViewById(R.id.tvParentTitle)
            rvCategory = itemView.findViewById(R.id.rvCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_card, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.tvTitle.text = rvs[position].title

        val picMeAdapter = PicmeAdapter(rvs[position].picmes, picmeDetailsViewModelViewModel)
        //picMeAdapter.setList(rvs[position].picmes)
        holder.rvCategory.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvCategory.adapter = picMeAdapter
        //picMeAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return rvs.size
    }


}