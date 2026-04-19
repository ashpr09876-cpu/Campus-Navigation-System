package com.medicapsnav.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.osmdroid.util.GeoPoint

data class RouteResult(
    val points: List<GeoPoint>,
    val distanceMeters: Double,
    val durationSeconds: Double,
    val distanceText: String,
    val durationText: String,
    val steps: List<RouteStep>
)

data class RouteStep(
    val instruction: String,
    val distanceText: String,
    val location: GeoPoint
)

/**
 * Uses OSRM public routing API — completely free, no API key required.
 * Docs: https://project-osrm.org/
 */
object OsrmRoutingHelper {

    private val client = OkHttpClient()
    private const val BASE_URL = "https://router.project-osrm.org/route/v1/foot"

    suspend fun getWalkingRoute(
        origin: GeoPoint,
        destination: GeoPoint
    ): Result<RouteResult> = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/${origin.longitude},${origin.latitude};" +
                      "${destination.longitude},${destination.latitude}" +
                      "?overview=full&geometries=geojson&steps=true&annotations=false"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()
                ?: return@withContext Result.failure(Exception("Empty response"))

            parseOsrmResponse(body)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseOsrmResponse(json: String): Result<RouteResult> {
        return try {
            val obj = JSONObject(json)
            val code = obj.getString("code")
            if (code != "Ok") {
                return Result.failure(Exception("OSRM error: $code"))
            }

            val route = obj.getJSONArray("routes").getJSONObject(0)
            val distanceM = route.getDouble("distance")
            val durationS = route.getDouble("duration")

            // GeoJSON coordinates
            val coords = route
                .getJSONObject("geometry")
                .getJSONArray("coordinates")
            val points = (0 until coords.length()).map { i ->
                val pt = coords.getJSONArray(i)
                GeoPoint(pt.getDouble(1), pt.getDouble(0)) // [lng, lat] → GeoPoint(lat,lng)
            }

            // Steps
            val steps = mutableListOf<RouteStep>()
            val legs = route.getJSONArray("legs")
            for (li in 0 until legs.length()) {
                val legSteps = legs.getJSONObject(li).getJSONArray("steps")
                for (si in 0 until legSteps.length()) {
                    val step = legSteps.getJSONObject(si)
                    val maneuver = step.getJSONObject("maneuver")
                    val locArr = maneuver.getJSONArray("location")
                    val stepLoc = GeoPoint(locArr.getDouble(1), locArr.getDouble(0))
                    val dist = step.getDouble("distance")
                    val instruction = buildInstruction(maneuver, step)
                    steps.add(RouteStep(instruction, formatDistance(dist), stepLoc))
                }
            }

            Result.success(
                RouteResult(
                    points = points,
                    distanceMeters = distanceM,
                    durationSeconds = durationS,
                    distanceText = formatDistance(distanceM),
                    durationText = formatDuration(durationS),
                    steps = steps
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildInstruction(maneuver: JSONObject, step: JSONObject): String {
        val type = maneuver.optString("type", "")
        val modifier = maneuver.optString("modifier", "")
        val name = step.optString("name", "")
        return when (type) {
            "depart"  -> "Start walking${if (name.isNotEmpty()) " on $name" else ""}"
            "arrive"  -> "You have arrived at your destination"
            "turn"    -> "Turn ${modifier}${if (name.isNotEmpty()) " onto $name" else ""}"
            "new name"-> "Continue on $name"
            "continue"-> "Continue straight${if (name.isNotEmpty()) " on $name" else ""}"
            "merge"   -> "Merge ${modifier}"
            "fork"    -> "Keep ${modifier}"
            "roundabout" -> "Enter roundabout"
            "exit roundabout" -> "Exit roundabout"
            else      -> "${type.replaceFirstChar { it.uppercase() }} $modifier".trim()
        }.trim()
    }

    fun formatDistance(meters: Double): String = when {
        meters >= 1000 -> String.format("%.1f km", meters / 1000)
        else           -> "${meters.toInt()} m"
    }

    fun formatDuration(seconds: Double): String {
        val mins = (seconds / 60).toInt()
        return when {
            mins < 1  -> "< 1 min"
            mins == 1 -> "1 min"
            mins < 60 -> "$mins mins"
            else      -> "${mins / 60}h ${mins % 60}m"
        }
    }
}
