package com.medicapsnav.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.medicapsnav.data.model.Building
import com.medicapsnav.data.model.BuildingCategory
import com.medicapsnav.data.repository.MedicapsRepository
import com.medicapsnav.utils.OsrmRoutingHelper
import com.medicapsnav.utils.RouteResult
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _buildings = MutableLiveData<List<Building>>(MedicapsRepository.buildings)
    val buildings: LiveData<List<Building>> = _buildings

    private val _selectedBuilding = MutableLiveData<Building?>()
    val selectedBuilding: LiveData<Building?> = _selectedBuilding

    private val _routeResult = MutableLiveData<RouteResult?>()
    val routeResult: LiveData<RouteResult?> = _routeResult

    private val _isLoadingRoute = MutableLiveData(false)
    val isLoadingRoute: LiveData<Boolean> = _isLoadingRoute

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _activeCategory = MutableLiveData<BuildingCategory?>(null)
    val activeCategory: LiveData<BuildingCategory?> = _activeCategory

    fun selectBuilding(building: Building?) {
        _selectedBuilding.value = building
        if (building == null) clearRoute()
    }

    fun filterByCategory(category: BuildingCategory?) {
        _activeCategory.value = category
        _buildings.value = if (category == null) MedicapsRepository.buildings
                           else MedicapsRepository.getBuildingsByCategory(category)
    }

    fun getRoute(origin: GeoPoint, destination: Building) {
        _isLoadingRoute.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            val result = OsrmRoutingHelper.getWalkingRoute(
                origin = origin,
                destination = GeoPoint(destination.latitude, destination.longitude)
            )
            _isLoadingRoute.value = false
            result.onSuccess { _routeResult.value = it }
                  .onFailure { _errorMessage.value = "Could not get route: ${it.message}" }
        }
    }

    fun clearRoute() { _routeResult.value = null }
    fun clearError() { _errorMessage.value = null }
}
