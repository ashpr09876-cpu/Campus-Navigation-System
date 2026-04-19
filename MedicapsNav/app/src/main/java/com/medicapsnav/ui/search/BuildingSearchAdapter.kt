package com.medicapsnav.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medicapsnav.data.model.Building
import com.medicapsnav.databinding.ItemBuildingBinding

class BuildingSearchAdapter(
    private val onItemClick: (Building) -> Unit
) : ListAdapter<Building, BuildingSearchAdapter.VH>(DiffCB) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemBuildingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemBuildingBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(building: Building) {
            b.tvName.text = building.name
            b.tvCategory.text = "${building.category.emoji} ${building.category.displayName}"
            b.tvDescription.text = building.description
            b.tvIndoorBadge.visibility =
                if (building.hasIndoorMap) android.view.View.VISIBLE else android.view.View.GONE
            b.root.setOnClickListener { onItemClick(building) }
        }
    }

    companion object DiffCB : DiffUtil.ItemCallback<Building>() {
        override fun areItemsTheSame(a: Building, b: Building) = a.id == b.id
        override fun areContentsTheSame(a: Building, b: Building) = a == b
    }
}
