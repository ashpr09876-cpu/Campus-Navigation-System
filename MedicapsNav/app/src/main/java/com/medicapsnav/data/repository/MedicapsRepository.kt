package com.medicapsnav.data.repository

import com.medicapsnav.data.model.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.BoundingBox

/**
 * Medicaps University, A.B. Road, Pigdamber, Rau, Indore – 453331
 * Campus center: ~22.6212° N, 75.8043° E
 * 29-acre campus on a hillside along A.B. Road.
 */
object MedicapsRepository {

    // ── Campus Geography ───────────────────────────────────────────────────
    val campusCenter = GeoPoint(22.6212, 75.8043)

    val campusBounds = BoundingBox(
        22.6240,   // North
        75.8070,   // East
        22.6185,   // South
        75.8015    // West
    )

    // ── Buildings & POIs ───────────────────────────────────────────────────
    val buildings = listOf(

        // ── Main Entrance ──────────────────────────────────────────────────
        Building(
            id = "main_gate",
            name = "Main Gate",
            shortName = "Main Gate",
            description = "Primary entrance to Medicaps University campus off A.B. Road, Pigdamber, Rau.",
            category = BuildingCategory.ENTRANCE,
            latitude = 22.6188,
            longitude = 75.8043,
            address = "A.B. Road, Pigdamber, Rau, Indore – 453331",
            openingHours = "Always Open",
            phone = "+91-7969024444"
        ),

        // ── Academic Blocks ────────────────────────────────────────────────
        Building(
            id = "block_a",
            name = "A Block – Engineering (CSE / IT)",
            shortName = "A Block",
            description = "Houses the Computer Science & Engineering and Information Technology departments. " +
                    "Smart classrooms (up to 180 students), well-equipped CS and IT labs, faculty offices.",
            category = BuildingCategory.ACADEMIC,
            latitude = 22.6218,
            longitude = 75.8048,
            address = "A Block, Medicaps University Campus",
            floors = 4,
            openingHours = "Mon-Sat: 8:30 AM – 8:00 PM",
            hasIndoorMap = true,
            floorPlans = listOf(
                FloorPlan(0, "Ground Floor", listOf(
                    Room("a_g_reception", "Reception / Enquiry", RoomType.RECEPTION, 0.5f, 0.9f),
                    Room("a_g_lab1", "CS Lab 1", RoomType.LAB, 0.2f, 0.4f),
                    Room("a_g_lab2", "CS Lab 2", RoomType.LAB, 0.75f, 0.4f),
                    Room("a_g_wc1", "Washroom", RoomType.WASHROOM, 0.9f, 0.8f),
                    Room("a_g_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                )),
                FloorPlan(1, "1st Floor", listOf(
                    Room("a_1_cr101", "Classroom 101", RoomType.CLASSROOM, 0.2f, 0.3f),
                    Room("a_1_cr102", "Classroom 102", RoomType.CLASSROOM, 0.5f, 0.3f),
                    Room("a_1_cr103", "Classroom 103", RoomType.CLASSROOM, 0.8f, 0.3f),
                    Room("a_1_it_lab", "IT Lab", RoomType.LAB, 0.3f, 0.7f),
                    Room("a_1_hod", "HOD – CSE Office", RoomType.OFFICE, 0.75f, 0.7f),
                    Room("a_1_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                )),
                FloorPlan(2, "2nd Floor", listOf(
                    Room("a_2_cr201", "Classroom 201", RoomType.CLASSROOM, 0.2f, 0.3f),
                    Room("a_2_cr202", "Classroom 202", RoomType.CLASSROOM, 0.5f, 0.3f),
                    Room("a_2_dsa_lab", "DSA / Algorithms Lab", RoomType.LAB, 0.3f, 0.7f),
                    Room("a_2_ai_lab", "AI & ML Lab", RoomType.LAB, 0.75f, 0.7f),
                    Room("a_2_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                )),
                FloorPlan(3, "3rd Floor", listOf(
                    Room("a_3_seminar", "Seminar Hall (200 seats)", RoomType.CLASSROOM, 0.5f, 0.35f),
                    Room("a_3_research", "Research Lab", RoomType.LAB, 0.2f, 0.7f),
                    Room("a_3_faculty", "Faculty Cabins", RoomType.OFFICE, 0.75f, 0.7f),
                    Room("a_3_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                ))
            )
        ),

        Building(
            id = "block_b",
            name = "B Block – Engineering (ECE / EE / Mech)",
            shortName = "B Block",
            description = "Hosts Electronics & Communication, Electrical Engineering, and Mechanical Engineering departments. " +
                    "Includes electronics labs, electrical lab, mechanical workshop, and CNC/Bosch Automotive Centre.",
            category = BuildingCategory.ACADEMIC,
            latitude = 22.6222,
            longitude = 75.8055,
            address = "B Block, Medicaps University Campus",
            floors = 4,
            openingHours = "Mon-Sat: 8:30 AM – 8:00 PM",
            hasIndoorMap = true,
            floorPlans = listOf(
                FloorPlan(0, "Ground Floor", listOf(
                    Room("b_g_mech_workshop", "Mechanical Workshop", RoomType.LAB, 0.25f, 0.4f),
                    Room("b_g_cnc_lab", "CNC / Bosch Auto Centre", RoomType.LAB, 0.7f, 0.4f),
                    Room("b_g_reception", "Reception", RoomType.RECEPTION, 0.5f, 0.85f),
                    Room("b_g_wc", "Washroom", RoomType.WASHROOM, 0.9f, 0.7f),
                    Room("b_g_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.6f)
                )),
                FloorPlan(1, "1st Floor", listOf(
                    Room("b_1_elec_lab", "Electrical Lab", RoomType.LAB, 0.25f, 0.35f),
                    Room("b_1_ece_lab", "ECE Lab", RoomType.LAB, 0.7f, 0.35f),
                    Room("b_1_cr101", "Classroom 101", RoomType.CLASSROOM, 0.25f, 0.7f),
                    Room("b_1_cr102", "Classroom 102", RoomType.CLASSROOM, 0.7f, 0.7f),
                    Room("b_1_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                ))
            )
        ),

        Building(
            id = "block_c",
            name = "C Block – Management & Commerce (MBA / BBA)",
            shortName = "C Block",
            description = "Faculty of Management Studies and Commerce. Home to MBA and BBA programs with seminar halls, " +
                    "business simulation lab, and case-study rooms.",
            category = BuildingCategory.ACADEMIC,
            latitude = 22.6215,
            longitude = 75.8035,
            address = "C Block, Medicaps University Campus",
            floors = 3,
            openingHours = "Mon-Sat: 8:30 AM – 6:00 PM",
            hasIndoorMap = true,
            floorPlans = listOf(
                FloorPlan(0, "Ground Floor", listOf(
                    Room("c_g_reception", "Reception", RoomType.RECEPTION, 0.5f, 0.88f),
                    Room("c_g_cr1", "Classroom 1", RoomType.CLASSROOM, 0.25f, 0.4f),
                    Room("c_g_cr2", "Classroom 2", RoomType.CLASSROOM, 0.72f, 0.4f),
                    Room("c_g_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.6f)
                )),
                FloorPlan(1, "1st Floor", listOf(
                    Room("c_1_biz_lab", "Business Simulation Lab", RoomType.LAB, 0.3f, 0.35f),
                    Room("c_1_seminar", "Seminar Room", RoomType.CLASSROOM, 0.7f, 0.35f),
                    Room("c_1_hod_mba", "HOD – MBA Office", RoomType.OFFICE, 0.5f, 0.7f),
                    Room("c_1_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                ))
            )
        ),

        Building(
            id = "block_d",
            name = "D Block – Pharmacy & Science",
            shortName = "D Block",
            description = "Faculty of Pharmacy and Faculty of Science. Houses pharmacy labs, chemistry lab, " +
                    "physics lab, biology lab, and biotech labs.",
            category = BuildingCategory.LAB,
            latitude = 22.6225,
            longitude = 75.8038,
            address = "D Block, Medicaps University Campus",
            floors = 3,
            openingHours = "Mon-Sat: 8:30 AM – 6:00 PM",
            hasIndoorMap = true,
            floorPlans = listOf(
                FloorPlan(0, "Ground Floor", listOf(
                    Room("d_g_chem_lab", "Chemistry Lab", RoomType.LAB, 0.2f, 0.35f),
                    Room("d_g_physics_lab", "Physics Lab", RoomType.LAB, 0.7f, 0.35f),
                    Room("d_g_reception", "Reception", RoomType.RECEPTION, 0.5f, 0.85f),
                    Room("d_g_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.6f)
                )),
                FloorPlan(1, "1st Floor", listOf(
                    Room("d_1_pharma_lab", "Pharmacy Lab", RoomType.LAB, 0.3f, 0.35f),
                    Room("d_1_bio_lab", "Biotechnology Lab", RoomType.LAB, 0.7f, 0.35f),
                    Room("d_1_cr", "Classroom", RoomType.CLASSROOM, 0.5f, 0.7f),
                    Room("d_1_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                ))
            )
        ),

        // ── R Block (Student Hub) ──────────────────────────────────────────
        Building(
            id = "r_block",
            name = "R Block – Student Hub & Canteen",
            shortName = "R Block",
            description = "Popular student hangout area with canteen, refreshment stalls, stationery shop, " +
                    "and common lounge. Students often relax here between classes.",
            category = BuildingCategory.DINING,
            latitude = 22.6210,
            longitude = 75.8042,
            address = "R Block, Medicaps University Campus",
            floors = 1,
            openingHours = "Mon-Sat: 8:00 AM – 8:00 PM"
        ),

        // ── Central Library ────────────────────────────────────────────────
        Building(
            id = "central_library",
            name = "Central Library",
            shortName = "Library",
            description = "One of the largest libraries in Central India. Spans ~35,000 sq. ft. and houses " +
                    "over 1,00,000 books, 1,200+ national & international journals, digital resources, " +
                    "and quiet reading zones.",
            category = BuildingCategory.LIBRARY,
            latitude = 22.6220,
            longitude = 75.8043,
            address = "Central Library, Medicaps University Campus",
            floors = 2,
            openingHours = "Mon-Sat: 8:30 AM – 6:00 PM",
            phone = "+91-0731-4259500",
            hasIndoorMap = true,
            floorPlans = listOf(
                FloorPlan(0, "Ground Floor", listOf(
                    Room("lib_g_issue", "Issue / Return Desk", RoomType.RECEPTION, 0.5f, 0.88f),
                    Room("lib_g_reading1", "Reading Hall – A", RoomType.CLASSROOM, 0.25f, 0.4f),
                    Room("lib_g_reading2", "Reading Hall – B", RoomType.CLASSROOM, 0.72f, 0.4f),
                    Room("lib_g_digital", "Digital Resource Centre", RoomType.LAB, 0.5f, 0.25f),
                    Room("lib_g_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.6f)
                )),
                FloorPlan(1, "1st Floor", listOf(
                    Room("lib_1_journals", "Journals & Periodicals", RoomType.CLASSROOM, 0.25f, 0.35f),
                    Room("lib_1_archives", "Archives & References", RoomType.CLASSROOM, 0.72f, 0.35f),
                    Room("lib_1_seminar", "Seminar / Discussion Room", RoomType.CLASSROOM, 0.5f, 0.7f),
                    Room("lib_1_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                ))
            )
        ),

        // ── Administration ─────────────────────────────────────────────────
        Building(
            id = "admin_block",
            name = "Administrative Block",
            shortName = "Admin Block",
            description = "Houses the Vice Chancellor's office, Registrar, Admission Cell, Examination Cell, " +
                    "Finance office, and all university administrative departments.",
            category = BuildingCategory.ADMIN,
            latitude = 22.6213,
            longitude = 75.8030,
            address = "Administrative Block, Medicaps University Campus",
            floors = 3,
            openingHours = "Mon-Fri: 9:00 AM – 5:00 PM",
            phone = "+91-7969024444",
            hasIndoorMap = true,
            floorPlans = listOf(
                FloorPlan(0, "Ground Floor", listOf(
                    Room("adm_g_reception", "Main Reception", RoomType.RECEPTION, 0.5f, 0.88f),
                    Room("adm_g_admission", "Admission Cell", RoomType.OFFICE, 0.25f, 0.4f),
                    Room("adm_g_finance", "Finance Office", RoomType.OFFICE, 0.72f, 0.4f),
                    Room("adm_g_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.6f)
                )),
                FloorPlan(1, "1st Floor", listOf(
                    Room("adm_1_registrar", "Registrar Office", RoomType.OFFICE, 0.3f, 0.35f),
                    Room("adm_1_exam", "Examination Cell", RoomType.OFFICE, 0.7f, 0.35f),
                    Room("adm_1_vc", "Vice Chancellor's Office", RoomType.OFFICE, 0.5f, 0.7f),
                    Room("adm_1_stairs", "Staircase", RoomType.STAIRCASE, 0.5f, 0.5f)
                ))
            )
        ),

        // ── Auditorium ─────────────────────────────────────────────────────
        Building(
            id = "auditorium",
            name = "Auditorium (750-seat)",
            shortName = "Auditorium",
            description = "Fully air-conditioned main auditorium with a seating capacity of 750. " +
                    "Used for convocations, cultural festivals, guest lectures, and major events. " +
                    "Equipped with PA system and projector facilities.",
            category = BuildingCategory.AUDITORIUM,
            latitude = 22.6208,
            longitude = 75.8050,
            address = "Auditorium, Medicaps University Campus",
            openingHours = "Varies by Event"
        ),

        Building(
            id = "seminar_hall",
            name = "Seminar Hall (200-seat)",
            shortName = "Seminar Hall",
            description = "Air-conditioned seminar hall with 200-seat capacity. " +
                    "Used for departmental seminars, workshops, and guest sessions.",
            category = BuildingCategory.AUDITORIUM,
            latitude = 22.6216,
            longitude = 75.8060,
            address = "Seminar Hall Block, Medicaps University Campus",
            openingHours = "Mon-Sat: 8:30 AM – 8:00 PM"
        ),

        // ── Training & Placement Cell ──────────────────────────────────────
        Building(
            id = "tpc",
            name = "Training & Placement Cell",
            shortName = "T&P Cell",
            description = "Central placement hub connecting students with 350+ recruiting companies " +
                    "(TCS, Capgemini, Amazon, Deloitte, Wipro, etc.). " +
                    "Hosts pre-placement training, mock interviews, and campus drives.",
            category = BuildingCategory.ADMIN,
            latitude = 22.6211,
            longitude = 75.8038,
            address = "Training & Placement Cell, Medicaps University Campus",
            floors = 1,
            openingHours = "Mon-Fri: 9:00 AM – 5:30 PM",
            phone = "+91-0731-4259500"
        ),

        // ── Sports ────────────────────────────────────────────────────────
        Building(
            id = "sports_ground",
            name = "Cricket & Football Ground",
            shortName = "Sports Ground",
            description = "Dedicated cricket ground and football pitch on campus. " +
                    "Inter-hostel tournaments and inter-college matches are held here. " +
                    "Scenic hilltop view of the surrounding area.",
            category = BuildingCategory.SPORTS,
            latitude = 22.6230,
            longitude = 75.8050,
            address = "Sports Ground, Medicaps University Campus",
            openingHours = "Daily: 6:00 AM – 8:00 PM"
        ),

        Building(
            id = "indoor_sports",
            name = "Indoor Sports & Gymnasium",
            shortName = "Gym / Indoor Sports",
            description = "Well-equipped gymnasium and indoor sports facility. " +
                    "Activities include table tennis, carrom, chess, volleyball, basketball, and badminton.",
            category = BuildingCategory.SPORTS,
            latitude = 22.6227,
            longitude = 75.8035,
            address = "Indoor Sports Complex, Medicaps University Campus",
            openingHours = "Mon-Sat: 6:00 AM – 9:00 PM"
        ),

        // ── Hostels ───────────────────────────────────────────────────────
        Building(
            id = "boys_hostel",
            name = "Boys' Hostel",
            shortName = "Boys' Hostel",
            description = "On-campus hostel for male students with 325 beds. " +
                    "Spacious rooms, mess facility, Wi-Fi, medical support, and public transport connectivity. " +
                    "Temperature-regulated structure.",
            category = BuildingCategory.HOSTEL,
            latitude = 22.6235,
            longitude = 75.8042,
            address = "Boys' Hostel, Medicaps University Campus",
            openingHours = "24/7",
            phone = "+91-7969024444"
        ),

        Building(
            id = "girls_hostel",
            name = "Girls' Hostel",
            shortName = "Girls' Hostel",
            description = "On-campus hostel for female students with 280 beds. " +
                    "Spacious rooms, mess facility, Wi-Fi, warden-monitored security, and medical support.",
            category = BuildingCategory.HOSTEL,
            latitude = 22.6232,
            longitude = 75.8030,
            address = "Girls' Hostel, Medicaps University Campus",
            openingHours = "24/7",
            phone = "+91-7969024444"
        ),

        // ── Health ────────────────────────────────────────────────────────
        Building(
            id = "health_center",
            name = "Health Centre / Medical Room",
            shortName = "Health Centre",
            description = "On-campus medical facility providing first aid and basic healthcare to students and staff. " +
                    "Ambulance service available for emergencies.",
            category = BuildingCategory.HEALTH,
            latitude = 22.6218,
            longitude = 75.8025,
            address = "Health Centre, Medicaps University Campus",
            openingHours = "Mon-Sat: 9:00 AM – 6:00 PM",
            phone = "+91-7969024444"
        ),

        // ── Canteen / Cafeteria ────────────────────────────────────────────
        Building(
            id = "main_canteen",
            name = "Main Cafeteria",
            shortName = "Canteen",
            description = "Central canteen serving hygienic and affordable meals, snacks, and beverages. " +
                    "Popular among students and faculty throughout the day.",
            category = BuildingCategory.DINING,
            latitude = 22.6209,
            longitude = 75.8046,
            address = "Main Canteen, Medicaps University Campus",
            openingHours = "Mon-Sat: 7:30 AM – 9:00 PM"
        )
    )

    // ── Helpers ────────────────────────────────────────────────────────────
    fun searchBuildings(query: String): List<Building> {
        if (query.isBlank()) return buildings
        val q = query.lowercase()
        return buildings.filter { b ->
            b.name.lowercase().contains(q) ||
            b.shortName.lowercase().contains(q) ||
            b.description.lowercase().contains(q) ||
            b.category.displayName.lowercase().contains(q) ||
            b.floorPlans.any { fp ->
                fp.rooms.any { r -> r.name.lowercase().contains(q) }
            }
        }
    }

    fun getBuildingById(id: String) = buildings.find { it.id == id }
    fun getBuildingsByCategory(cat: BuildingCategory) = buildings.filter { it.category == cat }
}
