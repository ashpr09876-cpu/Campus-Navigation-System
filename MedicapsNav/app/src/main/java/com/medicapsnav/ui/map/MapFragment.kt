package com.medicapsnav.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.medicapsnav.R
import com.medicapsnav.data.model.Building
import com.medicapsnav.data.model.BuildingCategory
import com.medicapsnav.data.repository.MedicapsRepository
import com.medicapsnav.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()

    private lateinit var mapView: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val markerBuildingMap = mutableMapOf<Marker, Building>()
    private var routePolyline: Polyline? = null

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        if (perms.any { it.value }) enableMyLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // OSMDroid requires this before MapView is created
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        Configuration.getInstance().userAgentValue = requireContext().packageName
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        setupBottomSheet()
        setupCategoryChips()
        setupSearchBar()
        setupObservers()
        checkLocationPermission()

        // If navigated here from search with a building id, select it
        arguments?.getString("buildingId")?.let { id ->
            MedicapsRepository.getBuildingById(id)?.let { building ->
                selectAndShowBuilding(building)
            }
        }
    }

    // ── Map ────────────────────────────────────────────────────────────────
    private fun setupMap() {
        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK) // OpenStreetMap
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(17.5)
        mapView.controller.setCenter(MedicapsRepository.campusCenter)

        placeAllMarkers()
    }

    private fun placeAllMarkers() {
        MedicapsRepository.buildings.forEach { building ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(building.latitude, building.longitude)
                title = building.name
                snippet = building.category.displayName
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = getCategoryIcon(building.category)
                setOnMarkerClickListener { _, _ ->
                    selectAndShowBuilding(building)
                    true
                }
            }
            markerBuildingMap[marker] = building
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    private fun getCategoryIcon(category: BuildingCategory) =
        ContextCompat.getDrawable(requireContext(), when (category) {
            BuildingCategory.ACADEMIC   -> R.drawable.ic_marker_academic
            BuildingCategory.LIBRARY    -> R.drawable.ic_marker_library
            BuildingCategory.ADMIN      -> R.drawable.ic_marker_admin
            BuildingCategory.HOSTEL     -> R.drawable.ic_marker_hostel
            BuildingCategory.SPORTS     -> R.drawable.ic_marker_sports
            BuildingCategory.DINING     -> R.drawable.ic_marker_dining
            BuildingCategory.HEALTH     -> R.drawable.ic_marker_health
            BuildingCategory.LAB        -> R.drawable.ic_marker_lab
            BuildingCategory.AUDITORIUM -> R.drawable.ic_marker_auditorium
            BuildingCategory.ENTRANCE   -> R.drawable.ic_marker_entrance
        })

    // ── Bottom Sheet ───────────────────────────────────────────────────────
    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.btnDirections.setOnClickListener {
            val dest = viewModel.selectedBuilding.value ?: return@setOnClickListener
            val myLoc = locationOverlay.myLocation
            if (myLoc == null) {
                Toast.makeText(requireContext(),
                    "Enable location to get directions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.getRoute(myLoc, dest)
        }

        binding.btnIndoorMap.setOnClickListener {
            val building = viewModel.selectedBuilding.value ?: return@setOnClickListener
            if (building.hasIndoorMap) {
                val action = MapFragmentDirections.actionMapToIndoor(building.id)
                findNavController().navigate(action)
            }
        }

        binding.btnClose.setOnClickListener {
            hideBottomSheet()
            viewModel.selectBuilding(null)
            clearRouteFromMap()
        }
    }

    private fun selectAndShowBuilding(building: Building) {
        viewModel.selectBuilding(building)
        mapView.controller.animateTo(GeoPoint(building.latitude, building.longitude))
        showBottomSheet(building)
    }

    private fun showBottomSheet(building: Building) {
        binding.tvBuildingName.text = building.name
        binding.tvBuildingCategory.text = "${building.category.emoji} ${building.category.displayName}"
        binding.tvDescription.text = building.description
        binding.tvHours.text = "🕐 ${building.openingHours}"
        binding.tvAddress.text = "📍 ${building.address}"
        binding.tvFloors.text = if (building.floors > 1) "🏢 ${building.floors} floors" else ""
        binding.tvFloors.visibility = if (building.floors > 1) View.VISIBLE else View.GONE
        if (building.phone.isNotEmpty()) {
            binding.tvPhone.visibility = View.VISIBLE
            binding.tvPhone.text = "📞 ${building.phone}"
        } else {
            binding.tvPhone.visibility = View.GONE
        }
        // Show Indoor Map button only if building has floor plans
        binding.btnIndoorMap.visibility =
            if (building.hasIndoorMap) View.VISIBLE else View.GONE

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // ── Category Chips ─────────────────────────────────────────────────────
    private fun setupCategoryChips() {
        binding.chipAll.setOnClickListener {
            viewModel.filterByCategory(null)
            updateMarkerVisibility(null)
        }
        BuildingCategory.values().forEach { cat ->
            val chip = Chip(requireContext()).apply {
                text = "${cat.emoji} ${cat.displayName}"
                isCheckable = true
                setOnClickListener {
                    viewModel.filterByCategory(cat)
                    updateMarkerVisibility(cat)
                }
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun updateMarkerVisibility(activeCategory: BuildingCategory?) {
        markerBuildingMap.forEach { (marker, building) ->
            marker.isEnabled = (activeCategory == null || building.category == activeCategory)
        }
        mapView.invalidate()
    }

    // ── Search ─────────────────────────────────────────────────────────────
    private fun setupSearchBar() {
        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_search)
        }
    }

    // ── Observers ──────────────────────────────────────────────────────────
    private fun setupObservers() {
        viewModel.routeResult.observe(viewLifecycleOwner) { route ->
            route?.let {
                drawRoute(it.points)
                binding.tvRouteInfo.visibility = View.VISIBLE
                binding.tvRouteInfo.text =
                    "🚶 ${it.durationText}  •  ${it.distanceText}"
            } ?: run {
                binding.tvRouteInfo.visibility = View.GONE
            }
        }
        viewModel.isLoadingRoute.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    // ── Route Drawing ──────────────────────────────────────────────────────
    private fun drawRoute(points: List<GeoPoint>) {
        clearRouteFromMap()
        routePolyline = Polyline(mapView).apply {
            setPoints(points)
            outlinePaint.color = Color.parseColor("#1A73E8")
            outlinePaint.strokeWidth = 14f
            outlinePaint.isAntiAlias = true
        }
        mapView.overlays.add(routePolyline)
        mapView.invalidate()
        // Zoom to fit route
        if (points.size > 1) {
            val box = org.osmdroid.util.BoundingBox.fromGeoPoints(points)
            mapView.zoomToBoundingBox(box.increaseByScale(1.2f), true)
        }
    }

    private fun clearRouteFromMap() {
        routePolyline?.let { mapView.overlays.remove(it) }
        routePolyline = null
        mapView.invalidate()
    }

    // ── Location ───────────────────────────────────────────────────────────
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    private fun enableMyLocation() {
        locationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(requireContext()), mapView
        )
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)
        mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
