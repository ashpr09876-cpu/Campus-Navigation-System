# 🏫 Medicaps University Campus Navigation App

A fully offline-capable Android navigation app for **Medicaps University, A.B. Road, Pigdamber, Rau, Indore – 453331**.

Built with **Kotlin + OSMDroid (OpenStreetMap)** — **no API key required**.

---

## ✨ Features

| Feature | Details |
|---------|---------|
| 🗺️ Interactive OSM Map | Real campus map centered on Medicaps University (22.6212°N, 75.8043°E) |
| 📍 Color-coded Markers | 10 categories: Academic, Library, Admin, Hostel, Sports, Dining, Health, Lab, Auditorium, Gate |
| 🔍 Smart Search | Search buildings AND rooms (e.g. search "AI Lab" to find A Block → 2nd Floor) |
| 🚶 Walking Directions | Free OSRM routing — no Google Maps API key needed |
| 🏢 Indoor Floor Plans | Interactive schematic floor plans for A Block, B Block, C Block, D Block, Library, Admin Block |
| 🏷️ Category Filter | Horizontal chip bar to filter by building type |
| 📡 Live GPS | Your location shown as blue dot on map |

---

## 🏗️ Project Structure

```
app/src/main/java/com/medicapsnav/
├── MainActivity.kt
├── data/
│   ├── model/Building.kt                    ← Data classes (Building, FloorPlan, Room)
│   └── repository/MedicapsRepository.kt     ← ALL campus buildings + floor plan data
├── ui/
│   ├── map/
│   │   ├── MapFragment.kt                   ← Main OSMDroid map screen
│   │   └── MapViewModel.kt                  ← State + OSRM routing calls
│   ├── search/
│   │   ├── SearchFragment.kt                ← Real-time search
│   │   └── BuildingSearchAdapter.kt
│   └── indoor/
│       ├── IndoorMapFragment.kt             ← Floor tab host
│       ├── FloorPlanFragment.kt             ← Per-floor view
│       ├── FloorPlanCanvasView.kt           ← Custom canvas: draws rooms
│       ├── FloorPlanPagerAdapter.kt
│       ├── RoomListAdapter.kt
│       └── RoomParcel.kt
└── utils/
    └── OsrmRoutingHelper.kt                 ← OSRM API (free, no key)
```

---

## 🚀 Setup (3 steps, no API key!)

### 1. Clone / Open in Android Studio
Open the `MedicapsNav` folder in Android Studio Hedgehog or newer.

### 2. Add Safe Args plugin to project build.gradle
```groovy
// In project-level build.gradle, add to dependencies:
classpath 'androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6'
```

### 3. Run!
Connect your Android device (API 24+) or start an emulator and press ▶.  
The app works fully offline for the map after the first tile load.

---

## 🏢 Campus Buildings Included

| Building | Category | Indoor Map |
|----------|----------|-----------|
| Main Gate | Gate / Entry | — |
| A Block (CSE / IT) | Academic | ✅ 4 floors |
| B Block (ECE / EE / Mech) | Academic | ✅ 2 floors |
| C Block (MBA / BBA) | Academic | ✅ 2 floors |
| D Block (Pharmacy / Science) | Lab | ✅ 2 floors |
| R Block – Student Hub | Dining | — |
| Central Library | Library | ✅ 2 floors |
| Administrative Block | Administration | ✅ 2 floors |
| Auditorium (750-seat) | Auditorium | — |
| Seminar Hall (200-seat) | Auditorium | — |
| Training & Placement Cell | Administration | — |
| Cricket & Football Ground | Sports | — |
| Indoor Sports & Gymnasium | Sports | — |
| Boys' Hostel (325 beds) | Hostel | — |
| Girls' Hostel (280 beds) | Hostel | — |
| Health Centre | Health | — |
| Main Cafeteria | Dining | — |

---

## 🗺️ Customizing Building Coordinates

The coordinates in `MedicapsRepository.kt` are approximate. To get precise coordinates for each building:

1. Open [OpenStreetMap](https://www.openstreetmap.org/) and search "Medicaps University Indore"
2. Right-click on each building → "Show address" to get exact lat/lng
3. Update the `latitude` / `longitude` fields in `MedicapsRepository.kt`

---

## 🏢 Adding Indoor Floor Plans

To add real floor plan images instead of the schematic view:

1. Place floor plan images in `res/drawable/` (e.g., `floor_plan_a_block_g.png`)
2. In `FloorPlanFragment.kt`, replace `FloorPlanCanvasView` with an `ImageView`
3. Load the image based on `floorPlan.floorNumber` and building ID

---

## 📦 Dependencies

| Library | Purpose | Cost |
|---------|---------|------|
| OSMDroid 6.1.18 | OpenStreetMap tiles + map UI | Free |
| OSRM (API) | Walking directions routing | Free |
| Navigation Component | Fragment navigation | Free |
| Material Design 3 | UI components | Free |
| OkHttp | HTTP for OSRM calls | Free |
| ViewPager2 | Floor plan tab swiper | Free |

**Total API cost: ₹0** 🎉

---

## 📞 Medicaps University Contact
- Address: A.B. Road, Pigdamber, Rau, Indore – 453331, MP
- Phone: +91-7969024444 / 0731-4259500
- Website: medicaps.ac.in
