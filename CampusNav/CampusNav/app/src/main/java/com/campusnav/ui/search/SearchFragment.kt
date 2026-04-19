package com.campusnav.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.campusnav.data.model.Building
import com.campusnav.data.repository.CampusRepository
import com.campusnav.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BuildingSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupBackButton()

        // Show all results by default
        adapter.submitList(CampusRepository.buildings)
    }

    private fun setupRecyclerView() {
        adapter = BuildingSearchAdapter { building ->
            navigateToBuilding(building)
        }
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResults.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.requestFocus()
        binding.etSearch.doAfterTextChanged { text ->
            val results = CampusRepository.searchBuildings(text?.toString() ?: "")
            adapter.submitList(results)
            binding.tvNoResults.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToBuilding(building: Building) {
        // Navigate back to map and select the building
        val action = SearchFragmentDirections.actionSearchToMap(building.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
