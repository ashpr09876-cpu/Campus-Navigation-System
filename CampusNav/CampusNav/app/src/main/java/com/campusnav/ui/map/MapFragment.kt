package com.campusnav.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campusnav.R
import com.campusnav.data.model.Building
import com.campusnav.data.model.BuildingCategory
import com.campusnav.data.repository.CampusRepository
import com.campusnav.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val markerBuildingMap = mutableMapOf<Marker, Building>()
    private var routePolyline: Polyline? = null
    private var userLocation: com.google.android.gms.maps.model.LatLng? = null

    // ── Permission Launcher ────────────────────────────────────────────────
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            enableMyLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupMap()
        setupBottomSheet()
        setupCategoryChips()
        setupSearchBar()
        setupObservers()
    }

    // ── Map Setup ──────────────────────────────────────────────────────────
    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Style & settings
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = false
        }

        // Move to campus
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(CampusRepository.campusCenter, 16f)
        )

        // Place markers
        placeAllMarkers()

        // Marker click
        googleMap.setOnMarkerClickListener { marker ->
            markerBuildingMap[marker]?.let { building ->
                viewModel.selectBuilding(building)
                showBuildingBottomSheet(building)
                true
            } ?: false
        }

        // Map click hides bottom sheet
        googleMap.setOnMapClickListener {
            hideBottomSheet()
            viewModel.selectBuilding(null)
        }

        // Request location
        checkLocationPermission()
    }

    private fun placeAllMarkers() {
        CampusRepository.buildings.forEach { building ->
            val hue = getCategoryHue(building.category)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(com.google.android.gms.maps.model.LatLng(building.latitude, building.longitude))
                    .title(building.name)
                    .snippet(building.category.displayName)
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))
            )
            marker?.let { markerBuildingMap[it] = building }
        }
    }

    private fun getCategoryHue(category: BuildingCategory): Float = when (category) {
        BuildingCategory.ACADEMIC  -> BitmapDescriptorFactory.HUE_BLUE
        BuildingCategory.LIBRARY   -> BitmapDescriptorFactory.HUE_VIOLET
        BuildingCategory.DINING    -> BitmapDescriptorFactory.HUE_ORANGE
        BuildingCategory.SPORTS    -> BitmapDescriptorFactory.HUE_GREEN
        BuildingCategory.ADMIN     -> BitmapDescriptorFactory.HUE_RED
        BuildingCategory.HOUSING   -> BitmapDescriptorFactory.HUE_YELLOW
        BuildingCategory.HEALTH    -> BitmapDescriptorFactory.HUE_ROSE
        BuildingCategory.PARKING   -> BitmapDescriptorFactory.HUE_AZURE
        BuildingCategory.LAB       -> BitmapDescriptorFactory.HUE_CYAN
        BuildingCategory.OTHER     -> BitmapDescriptorFactory.HUE_MAGENTA
    }

    // ── Bottom Sheet ───────────────────────────────────────────────────────
    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = 300

        binding.btnDirections.setOnClickListener {
            val building = viewModel.selectedBuilding.value ?: return@setOnClickListener
            val loc = userLocation

            if (loc == null) {
                Toast.makeText(requireContext(),
                    "Enable location to get directions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val apiKey = getString(R.string.maps_api_key)
            viewModel.getDirections(loc, building, apiKey)
        }

        binding.btnClose.setOnClickListener {
            hideBottomSheet()
            viewModel.selectBuilding(null)
            clearRoute()
        }
    }

    private fun showBuildingBottomSheet(building: Building) {
        binding.tvBuildingName.text = building.name
        binding.tvBuildingCategory.text = "${building.category.emoji} ${building.category.displayName}"
        binding.tvBuildingDescription.text = building.description
        binding.tvBuildingHours.text = "🕐 ${building.openingHours}"
        binding.tvBuildingAddress.text = "📍 ${building.address}"
        if (building.phone.isNotEmpty()) {
            binding.tvBuildingPhone.visibility = View.VISIBLE
            binding.tvBuildingPhone.text = "📞 ${building.phone}"
        } else {
            binding.tvBuildingPhone.visibility = View.GONE
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // ── Category Filter Chips ──────────────────────────────────────────────
    private fun setupCategoryChips() {
        binding.chipAll.setOnClickListener {
            viewModel.filterByCategory(null)
            updateMapMarkers(null)
        }
        BuildingCategory.values().forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = "${category.emoji} ${category.displayName}"
                isCheckable = true
                setOnClickListener {
                    viewModel.filterByCategory(category)
                    updateMapMarkers(category)
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun updateMapMarkers(activeCategory: BuildingCategory?) {
        markerBuildingMap.forEach { (marker, building) ->
            marker.isVisible = activeCategory == null || building.category == activeCategory
        }
    }

    // ── Search ─────────────────────────────────────────────────────────────
    private fun setupSearchBar() {
        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_search)
        }
    }

    // ── Observers ──────────────────────────────────────────────────────────
    private fun setupObservers() {
        viewModel.directionsResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                drawRoute(it.polylinePoints)
                binding.tvDirectionsInfo.visibility = View.VISIBLE
                binding.tvDirectionsInfo.text = "🚶 ${it.durationText}  •  ${it.distanceText}"
            }
        }

        viewModel.isLoadingDirections.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    // ── Route Drawing ──────────────────────────────────────────────────────
    private fun drawRoute(points: List<com.google.android.gms.maps.model.LatLng>) {
        routePolyline?.remove()
        routePolyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(points)
                .width(12f)
                .color(Color.parseColor("#1A73E8"))
                .geodesic(true)
                .jointType(JointType.ROUND)
                .startCap(RoundCap())
                .endCap(RoundCap())
        )

        // Zoom to fit route
        if (points.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            points.forEach { boundsBuilder.include(it) }
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 120)
            )
        }
    }

    private fun clearRoute() {
        routePolyline?.remove()
        routePolyline = null
        binding.tvDirectionsInfo.visibility = View.GONE
    }

    // ── Location ───────────────────────────────────────────────────────────
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                enableMyLocation()
            }
            else -> {
                locationPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }
    }

    private fun enableMyLocation() {
        try {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLocation = com.google.android.gms.maps.model.LatLng(it.latitude, it.longitude)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
