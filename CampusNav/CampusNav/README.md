# 🗺️ Campus Navigation App

A full-featured Android campus navigation app built with Kotlin, Google Maps, and Material Design 3.

## Features

- 📍 **Interactive Map** — Color-coded markers for every building by category
- 🔍 **Smart Search** — Real-time search across all buildings and points of interest
- 🚶 **Walking Directions** — Turn-by-turn routes via Google Directions API
- 🏷️ **Category Filters** — Filter buildings by Academic, Dining, Sports, Health, etc.
- 📋 **Building Details** — Hours, description, address, and phone in a smooth bottom sheet
- 📡 **Live Location** — Shows your current position on the map

## Project Structure

```
app/src/main/java/com/campusnav/
├── MainActivity.kt
├── data/
│   ├── model/
│   │   └── Building.kt              ← Data class + category enum
│   └── repository/
│       └── CampusRepository.kt      ← All campus buildings + search logic
├── ui/
│   ├── map/
│   │   ├── MapFragment.kt           ← Main map screen
│   │   └── MapViewModel.kt          ← State management
│   └── search/
│       ├── SearchFragment.kt        ← Search screen
│       └── BuildingSearchAdapter.kt ← RecyclerView adapter
└── utils/
    └── DirectionsHelper.kt          ← Google Directions API calls
```

## Setup Instructions

### 1. Get a Google Maps API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create or select a project
3. Enable these APIs:
   - **Maps SDK for Android**
   - **Directions API**
4. Go to **Credentials** → **Create Credentials** → **API Key**
5. (Recommended) Restrict the key to your app's package name and SHA-1

### 2. Add Your API Key

Open `local.properties` and replace the placeholder:

```properties
MAPS_API_KEY=AIzaSyYOUR_ACTUAL_KEY_HERE
```

> ⚠️ Never commit your API key to version control. `local.properties` is already in `.gitignore`.

### 3. Add Your Campus Buildings

Open `CampusRepository.kt` and:

1. Update `campusCenter` with your campus's latitude/longitude
2. Update `campusBounds` with your campus's southwest and northeast corners
3. Replace the sample `buildings` list with your real buildings

```kotlin
val campusCenter = LatLng(YOUR_LAT, YOUR_LNG)

val buildings = listOf(
    Building(
        id = "main_building",
        name = "Main Academic Building",
        shortName = "Main",
        description = "Houses the Dean's office and lecture halls",
        category = BuildingCategory.ACADEMIC,
        latitude = YOUR_LAT,
        longitude = YOUR_LNG,
        address = "1 University Ave, Your City, State 00000",
        floors = 5,
        openingHours = "Mon-Fri: 7AM-10PM",
        phone = "(555) 123-4567"
    ),
    // Add more buildings...
)
```

### 4. Build & Run

Open in Android Studio, sync Gradle, and run on a device or emulator with Google Play Services.

## Customization

### Adding New Building Categories

In `Building.kt`, add to the `BuildingCategory` enum:

```kotlin
enum class BuildingCategory(val displayName: String, val emoji: String) {
    // ... existing categories ...
    CHAPEL("Chapel", "⛪"),
    GREENHOUSE("Greenhouse", "🌿"),
}
```

Then update the color mapping in `MapFragment.kt`:

```kotlin
private fun getCategoryHue(category: BuildingCategory): Float = when (category) {
    // ... existing mappings ...
    BuildingCategory.CHAPEL -> BitmapDescriptorFactory.HUE_RED
    BuildingCategory.GREENHOUSE -> BitmapDescriptorFactory.HUE_GREEN
}
```

### Changing Map Style

In `MapFragment.kt → onMapReady()`:

```kotlin
googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE // or HYBRID, TERRAIN, NORMAL
```

Or apply a custom JSON style from [Google Maps Styling Wizard](https://mapstyle.withgoogle.com/).

## Dependencies

| Library | Purpose |
|---------|---------|
| Google Maps SDK | Interactive map |
| Google Location Services | User's current location |
| Maps Android KTX | Kotlin extensions for Maps |
| Navigation Component | Fragment navigation |
| ViewModel + LiveData | MVVM architecture |
| Material Design 3 | UI components |
| OkHttp | HTTP requests to Directions API |
| Room | Local database (ready to use) |
| Coroutines | Async/background work |

## Screenshots

The app includes:
- Full-screen Google Map with colored markers
- Horizontal category filter chips
- Floating search bar
- Slide-up building detail bottom sheet
- Blue route polyline for directions
- Distance/duration info banner

## License

MIT License — feel free to use and modify for your institution.
