package com.campusnav.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a campus building or point of interest.
 */
@Entity(tableName = "buildings")
data class Building(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String,
    val description: String,
    val category: BuildingCategory,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val floors: Int = 1,
    val imageResId: Int = 0,
    val openingHours: String = "Mon-Fri: 8:00 AM - 10:00 PM",
    val phone: String = "",
    val website: String = "",
    val isFavorite: Boolean = false
)

enum class BuildingCategory(val displayName: String, val emoji: String) {
    ACADEMIC("Academic", "🎓"),
    LIBRARY("Library", "📚"),
    DINING("Dining", "🍽️"),
    SPORTS("Sports & Recreation", "⚽"),
    ADMIN("Administration", "🏛️"),
    HOUSING("Housing", "🏠"),
    HEALTH("Health & Wellness", "🏥"),
    PARKING("Parking", "🅿️"),
    LAB("Labs & Research", "🔬"),
    OTHER("Other", "📍")
}
