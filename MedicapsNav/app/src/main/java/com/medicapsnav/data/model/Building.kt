package com.medicapsnav.data.model

/**
 * Represents a building or point of interest on the Medicaps University campus.
 */
data class Building(
    val id: String,
    val name: String,
    val shortName: String,
    val description: String,
    val category: BuildingCategory,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val floors: Int = 1,
    val openingHours: String = "Mon-Sat: 8:30 AM – 5:00 PM",
    val phone: String = "",
    val hasIndoorMap: Boolean = false,
    val floorPlans: List<FloorPlan> = emptyList()
)

data class FloorPlan(
    val floorNumber: Int,
    val floorLabel: String,       // e.g. "Ground Floor", "1st Floor"
    val rooms: List<Room>
)

data class Room(
    val id: String,
    val name: String,
    val type: RoomType,
    // Relative position on floor plan canvas (0.0–1.0)
    val relativeX: Float,
    val relativeY: Float
)

enum class RoomType(val emoji: String) {
    CLASSROOM("🏫"),
    LAB("🔬"),
    OFFICE("🗂️"),
    WASHROOM("🚻"),
    STAIRCASE("🪜"),
    LIFT("🛗"),
    CAFETERIA("☕"),
    RECEPTION("📋"),
    OTHER("📍")
}

enum class BuildingCategory(val displayName: String, val emoji: String) {
    ACADEMIC("Academic Block", "🎓"),
    LIBRARY("Library", "📚"),
    ADMIN("Administration", "🏛️"),
    HOSTEL("Hostel", "🏠"),
    SPORTS("Sports & Recreation", "⚽"),
    DINING("Dining & Canteen", "🍽️"),
    HEALTH("Health Center", "🏥"),
    LAB("Labs & Research", "🔬"),
    AUDITORIUM("Auditorium", "🎭"),
    ENTRANCE("Gate / Entry", "🚪")
}
