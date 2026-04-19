package com.campusnav.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.campusnav.data.model.Building
import com.campusnav.data.model.BuildingCategory
import com.campusnav.data.repository.CampusRepository
import com.campusnav.utils.DirectionsHelper
import com.campusnav.utils.DirectionsResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _buildings = MutableLiveData<List<Building>>(CampusRepository.buildings)
    val buildings: LiveData<List<Building>> = _buildings

    private val _selectedBuilding = MutableLiveData<Building?>()
    val selectedBuilding: LiveData<Building?> = _selectedBuilding

    private val _directionsResult = MutableLiveData<DirectionsResult?>()
    val directionsResult: LiveData<DirectionsResult?> = _directionsResult

    private val _isLoadingDirections = MutableLiveData(false)
    val isLoadingDirections: LiveData<Boolean> = _isLoadingDirections

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _activeCategory = MutableLiveData<BuildingCategory?>(null)
    val activeCategory: LiveData<BuildingCategory?> = _activeCategory

    fun selectBuilding(building: Building?) {
        _selectedBuilding.value = building
        if (building == null) clearDirections()
    }

    fun filterByCategory(category: BuildingCategory?) {
        _activeCategory.value = category
        _buildings.value = if (category == null) {
            CampusRepository.buildings
        } else {
            CampusRepository.getBuildingsByCategory(category)
        }
    }

    fun getDirections(origin: LatLng, destination: Building, apiKey: String) {
        _isLoadingDirections.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = DirectionsHelper.getWalkingDirections(
                origin = origin,
                destination = LatLng(destination.latitude, destination.longitude),
                apiKey = apiKey
            )
            _isLoadingDirections.value = false
            result.onSuccess { directions ->
                _directionsResult.value = directions
            }.onFailure { error ->
                _errorMessage.value = "Could not get directions: ${error.message}"
            }
        }
    }

    fun clearDirections() {
        _directionsResult.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
