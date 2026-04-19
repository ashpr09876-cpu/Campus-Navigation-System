package com.medicapsnav.ui.indoor

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.medicapsnav.data.model.FloorPlan

class FloorPlanPagerAdapter(
    fragment: Fragment,
    private val floorPlans: List<FloorPlan>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = floorPlans.size

    override fun createFragment(position: Int): Fragment {
        val floorPlan = floorPlans[position]
        return FloorPlanFragment().apply {
            arguments = Bundle().apply {
                putInt("floorNumber", floorPlan.floorNumber)
                putString("floorLabel", floorPlan.floorLabel)
                putParcelableArrayList("rooms",
                    ArrayList(floorPlan.rooms.map { room ->
                        RoomParcel(room.id, room.name, room.type.name,
                                   room.type.emoji, room.relativeX, room.relativeY)
                    })
                )
            }
        }
    }
}
