package com.medicapsnav.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.medicapsnav.data.repository.MedicapsRepository
import com.medicapsnav.databinding.FragmentSearchBinding

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

        adapter = BuildingSearchAdapter { building ->
            val action = SearchFragmentDirections.actionSearchToMap(building.id)
            findNavController().navigate(action)
        }
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResults.adapter = adapter
        adapter.submitList(MedicapsRepository.buildings)

        binding.etSearch.requestFocus()
        binding.etSearch.doAfterTextChanged { text ->
            val results = MedicapsRepository.searchBuildings(text?.toString() ?: "")
            adapter.submitList(results)
            binding.tvNoResults.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
