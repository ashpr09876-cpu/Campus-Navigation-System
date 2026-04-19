package com.medicapsnav.ui.indoor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.medicapsnav.databinding.ItemRoomBinding

class RoomListAdapter(
    private val rooms: List<RoomParcel>,
    private val onRoomClick: (RoomParcel) -> Unit
) : ListAdapter<RoomParcel, RoomListAdapter.VH>(DiffCB) {

    init { submitList(rooms) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemRoomBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(room: RoomParcel) {
            b.tvEmoji.text = room.emoji
            b.tvRoomName.text = room.name
            b.tvRoomType.text = room.typeName.replace("_", " ")
                .lowercase().replaceFirstChar { it.uppercase() }
            b.root.setOnClickListener { onRoomClick(room) }
        }
    }

    companion object DiffCB : DiffUtil.ItemCallback<RoomParcel>() {
        override fun areItemsTheSame(a: RoomParcel, b: RoomParcel) = a.id == b.id
        override fun areContentsTheSame(a: RoomParcel, b: RoomParcel) = a == b
    }
}
