package com.campusnav.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.campusnav.data.model.Building
import com.campusnav.databinding.ItemBuildingBinding

class BuildingSearchAdapter(
    private val onItemClick: (Building) -> Unit
) : ListAdapter<Building, BuildingSearchAdapter.BuildingViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val binding = ItemBuildingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BuildingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BuildingViewHolder(
        private val binding: ItemBuildingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(building: Building) {
            binding.tvName.text = building.name
            binding.tvCategory.text = "${building.category.emoji} ${building.category.displayName}"
            binding.tvDescription.text = building.description
            binding.root.setOnClickListener { onItemClick(building) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Building>() {
        override fun areItemsTheSame(a: Building, b: Building) = a.id == b.id
        override fun areContentsTheSame(a: Building, b: Building) = a == b
    }
}
