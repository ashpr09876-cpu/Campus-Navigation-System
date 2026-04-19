package com.medicapsnav.ui.indoor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.medicapsnav.databinding.FragmentFloorPlanBinding

class FloorPlanFragment : Fragment() {

    private var _binding: FragmentFloorPlanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFloorPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rooms: List<RoomParcel> = arguments
            ?.getParcelableArrayList("rooms") ?: emptyList()
        val floorLabel = arguments?.getString("floorLabel") ?: "Floor"

        binding.tvFloorLabel.text = floorLabel

        // Set rooms on interactive canvas
        binding.floorPlanCanvas.setRooms(rooms)

        // Room list below the canvas
        val adapter = RoomListAdapter(rooms) { room ->
            binding.floorPlanCanvas.highlightRoom(room.id)
        }
        binding.rvRooms.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRooms.adapter = adapter

        // Search filter
        binding.etRoomSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.lowercase() ?: ""
            val filtered = if (query.isBlank()) rooms
                           else rooms.filter { it.name.lowercase().contains(query) || it.emoji.contains(query) }
            adapter.submitList(filtered)
            if (filtered.size == 1) binding.floorPlanCanvas.highlightRoom(filtered[0].id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
