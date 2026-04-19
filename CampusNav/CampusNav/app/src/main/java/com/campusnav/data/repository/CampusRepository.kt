package com.campusnav.data.repository

import com.campusnav.data.model.Building
import com.campusnav.data.model.BuildingCategory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 * Central data source for campus buildings and navigation data.
 * Replace the sample data with your actual campus coordinates.
 */
object CampusRepository {

    // ── Campus Center & Bounds ──────────────────────────────────────────────
    // TODO: Replace with your campus center coordinates
    val campusCenter = LatLng(37.8724, -122.2595) // UC Berkeley as example

    val campusBounds = LatLngBounds(
        LatLng(37.8680, -122.2650), // Southwest corner
        LatLng(37.8770, -122.2540)  // Northeast corner
    )

    // ── Sample Buildings ────────────────────────────────────────────────────
    val buildings = listOf(
        Building(
            id = "sather_gate",
            name = "Sather Gate",
            shortName = "Sather Gate",
            description = "Iconic landmark and main entrance to the UC Berkeley campus. A historic bronze gate built in 1910.",
            category = BuildingCategory.OTHER,
            latitude = 37.8702,
            longitude = -122.2595,
            address = "Sather Gate, Berkeley, CA 94720",
            openingHours = "Always Open"
        ),
        Building(
            id = "doe_library",
            name = "Doe Memorial Library",
            shortName = "Doe Library",
            description = "The main research library on campus, housing millions of volumes across multiple floors.",
            category = BuildingCategory.LIBRARY,
            latitude = 37.8724,
            longitude = -122.2597,
            address = "Doe Library, Berkeley, CA 94720",
            floors = 5,
            openingHours = "Mon-Thu: 9AM-9PM, Fri: 9AM-5PM, Sat-Sun: 1PM-5PM",
            phone = "(510) 642-3781"
        ),
        Building(
            id = "campanile",
            name = "Sather Tower (Campanile)",
            shortName = "Campanile",
            description = "307-foot bell tower and symbol of UC Berkeley. Offers panoramic views of the Bay Area from the observation deck.",
            category = BuildingCategory.OTHER,
            latitude = 37.8723,
            longitude = -122.2578,
            address = "Sather Tower, Berkeley, CA 94720",
            openingHours = "Mon-Fri: 10AM-4PM, Sat-Sun: 10AM-5PM"
        ),
        Building(
            id = "sproul_hall",
            name = "Sproul Hall",
            shortName = "Sproul Hall",
            description = "Main administration building and home of the Office of Undergraduate Admissions.",
            category = BuildingCategory.ADMIN,
            latitude = 37.8694,
            longitude = -122.2592,
            address = "Sproul Hall, Berkeley, CA 94720",
            floors = 5,
            openingHours = "Mon-Fri: 8AM-5PM",
            phone = "(510) 642-6000"
        ),
        Building(
            id = "student_union",
            name = "Martin Luther King Jr. Student Union",
            shortName = "MLK Student Union",
            description = "Central hub for student life with dining options, meeting rooms, and student services.",
            category = BuildingCategory.DINING,
            latitude = 37.8691,
            longitude = -122.2601,
            address = "MLK Student Union, Berkeley, CA 94720",
            floors = 4,
            openingHours = "Mon-Fri: 7AM-11PM, Sat-Sun: 9AM-10PM"
        ),
        Building(
            id = "haas_pavilion",
            name = "Haas Pavilion",
            shortName = "Haas Pavilion",
            description = "Multi-purpose arena used for basketball games, graduation ceremonies, and major events. Seats 6,578.",
            category = BuildingCategory.SPORTS,
            latitude = 37.8742,
            longitude = -122.2631,
            address = "Haas Pavilion, Berkeley, CA 94720",
            openingHours = "Varies by event"
        ),
        Building(
            id = "tang_center",
            name = "Tang Center",
            shortName = "Tang Center",
            description = "University Health Services - student health and counseling center.",
            category = BuildingCategory.HEALTH,
            latitude = 37.8673,
            longitude = -122.2607,
            address = "2222 Bancroft Way, Berkeley, CA 94720",
            floors = 3,
            openingHours = "Mon-Fri: 8AM-6PM",
            phone = "(510) 642-2000"
        ),
        Building(
            id = "evans_hall",
            name = "Evans Hall",
            shortName = "Evans Hall",
            description = "Home to the Mathematics and Statistics departments. Known for its distinctive brutalist architecture.",
            category = BuildingCategory.ACADEMIC,
            latitude = 37.8734,
            longitude = -122.2575,
            address = "Evans Hall, Berkeley, CA 94720",
            floors = 10,
            openingHours = "Mon-Fri: 7AM-10PM"
        ),
        Building(
            id = "stanley_hall",
            name = "Stanley Hall",
            shortName = "Stanley Hall",
            description = "Houses the QB3 research institute and bioengineering departments. State-of-the-art research facilities.",
            category = BuildingCategory.LAB,
            latitude = 37.8745,
            longitude = -122.2565,
            address = "Stanley Hall, Berkeley, CA 94720",
            floors = 6,
            openingHours = "Mon-Fri: 7AM-10PM"
        ),
        Building(
            id = "recreational_sports",
            name = "Recreational Sports Facility",
            shortName = "RSF",
            description = "Full-service gym with pools, courts, fitness equipment, and climbing wall.",
            category = BuildingCategory.SPORTS,
            latitude = 37.8685,
            longitude = -122.2626,
            address = "Recreational Sports Facility, Berkeley, CA 94720",
            floors = 3,
            openingHours = "Mon-Fri: 6AM-11PM, Sat-Sun: 8AM-10PM",
            phone = "(510) 642-7796"
        )
    )

    // ── Search ──────────────────────────────────────────────────────────────
    fun searchBuildings(query: String): List<Building> {
        if (query.isBlank()) return buildings
        val q = query.lowercase()
        return buildings.filter { building ->
            building.name.lowercase().contains(q) ||
            building.shortName.lowercase().contains(q) ||
            building.description.lowercase().contains(q) ||
            building.category.displayName.lowercase().contains(q)
        }
    }

    fun getBuildingById(id: String): Building? = buildings.find { it.id == id }

    fun getBuildingsByCategory(category: BuildingCategory): List<Building> =
        buildings.filter { it.category == category }
}
