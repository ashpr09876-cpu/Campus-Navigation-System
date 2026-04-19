package com.medicapsnav.ui.indoor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.medicapsnav.data.repository.MedicapsRepository
import com.medicapsnav.databinding.FragmentIndoorMapBinding

class IndoorMapFragment : Fragment() {

    private var _binding: FragmentIndoorMapBinding? = null
    private val binding get() = _binding!!
    private val args: IndoorMapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndoorMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val building = MedicapsRepository.getBuildingById(args.buildingId)
        if (building == null || building.floorPlans.isEmpty()) {
            findNavController().popBackStack()
            return
        }

        binding.tvBuildingTitle.text = building.name
        binding.tvFloorCount.text = "${building.floors} floors  •  ${building.floorPlans.size} floor plans available"

        val adapter = FloorPlanPagerAdapter(this, building.floorPlans)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = building.floorPlans[position].floorLabel
        }.attach()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
