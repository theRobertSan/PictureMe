package com.example.pictureme.views.addFeeling.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.PreviewPicme
import com.example.pictureme.utils.Details
import com.example.pictureme.viewmodels.PreviewPicmeViewModel
import com.google.android.material.imageview.ShapeableImageView

class FeelingsAdapter(
    private var feelings: List<Feeling>,
    private val previewPicmeViewModel: PreviewPicmeViewModel,
    val viewLifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<FeelingsAdapter.FeelingsViewHolder>() {

    private var preview: PreviewPicme? = null

    inner class FeelingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFeeling: TextView
        val cbSelectFeeling: CheckBox
        val imageFeeling: ShapeableImageView

        init {
            textFeeling = itemView.findViewById(R.id.textFeeling)
            cbSelectFeeling = itemView.findViewById(R.id.cbSelectFeeling)
            imageFeeling = itemView.findViewById(R.id.imageFeeling)
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

        holder.cbSelectFeeling.isChecked = SelectedPosition.currentPosition == position;

        holder.cbSelectFeeling.setOnClickListener {
            if (!holder.cbSelectFeeling.isChecked && !SelectedPosition.anyChecked()) {
                holder.cbSelectFeeling.isChecked = true
            } else {
                if (holder.cbSelectFeeling.isChecked) {
                    previewPicmeViewModel.updateFeeling(feelingId)
                    SelectedPosition.currentPosition = position
                    notifyDataSetChanged();
                } else {
                    previewPicmeViewModel.removeFeeling()
                }
            }

        }

        // Load feeling
        holder.imageFeeling.setImageResource(Details.getFeelingImage(feelings[position].feeling)!!)
    }

    override fun getItemCount(): Int {
        return feelings.size
    }

    fun setPreview(preview: PreviewPicme) {
        this.preview = preview
    }
}