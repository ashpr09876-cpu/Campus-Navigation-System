package com.campusnav.utils

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class DirectionsResult(
    val polylinePoints: List<LatLng>,
    val distanceText: String,
    val durationText: String,
    val steps: List<DirectionStep>
)

data class DirectionStep(
    val instruction: String,
    val distanceText: String,
    val startLocation: LatLng,
    val endLocation: LatLng
)

object DirectionsHelper {

    private val client = OkHttpClient()

    /**
     * Fetches walking directions from Google Directions API.
     * @param apiKey Your Google Maps API key
     */
    suspend fun getWalkingDirections(
        origin: LatLng,
        destination: LatLng,
        apiKey: String
    ): Result<DirectionsResult> = withContext(Dispatchers.IO) {
        try {
            val url = buildDirectionsUrl(origin, destination, apiKey)
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return@withContext Result.failure(
                Exception("Empty response from Directions API")
            )
            parseDirectionsResponse(body)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildDirectionsUrl(origin: LatLng, dest: LatLng, key: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json" +
               "?origin=${origin.latitude},${origin.longitude}" +
               "&destination=${dest.latitude},${dest.longitude}" +
               "&mode=walking" +
               "&key=$key"
    }

    private fun parseDirectionsResponse(json: String): Result<DirectionsResult> {
        return try {
            val obj = JSONObject(json)
            val status = obj.getString("status")

            if (status != "OK") {
                return Result.failure(Exception("Directions API error: $status"))
            }

            val route = obj.getJSONArray("routes").getJSONObject(0)
            val leg = route.getJSONArray("legs").getJSONObject(0)

            val distance = leg.getJSONObject("distance").getString("text")
            val duration = leg.getJSONObject("duration").getString("text")

            // Decode polyline
            val encodedPolyline = route.getJSONObject("overview_polyline").getString("points")
            val polylinePoints = decodePolyline(encodedPolyline)

            // Parse steps
            val stepsArray = leg.getJSONArray("steps")
            val steps = mutableListOf<DirectionStep>()
            for (i in 0 until stepsArray.length()) {
                val step = stepsArray.getJSONObject(i)
                val htmlInstruction = step.getString("html_instructions")
                val cleanInstruction = htmlInstruction.replace(Regex("<[^>]*>"), "")
                val stepDistance = step.getJSONObject("distance").getString("text")
                val startLoc = step.getJSONObject("start_location")
                val endLoc = step.getJSONObject("end_location")

                steps.add(
                    DirectionStep(
                        instruction = cleanInstruction,
                        distanceText = stepDistance,
                        startLocation = LatLng(startLoc.getDouble("lat"), startLoc.getDouble("lng")),
                        endLocation = LatLng(endLoc.getDouble("lat"), endLoc.getDouble("lng"))
                    )
                )
            }

            Result.success(
                DirectionsResult(
                    polylinePoints = polylinePoints,
                    distanceText = distance,
                    durationText = duration,
                    steps = steps
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Decodes a Google Maps encoded polyline string into LatLng points.
     */
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        var lat = 0
        var lng = 0

        while (index < encoded.length) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            poly.add(LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5))
        }
        return poly
    }
}
