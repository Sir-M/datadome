package de.slg.datadome

import android.util.Log
import java.util.*

const val IP = "https://evening-badlands-73526.herokuapp.com/"

fun getLocationList(): List<MapLocation> {
    val response = khttp.get(IP)

    val entries = response.jsonArray
    val articles = mutableListOf<MapLocation>()

    for (i in 0 until entries.length()) {
        val json = entries.getJSONObject(i)
        val categories = mutableListOf<Int>()

        val array = json.getJSONArray("categoryIds")

        (0 until array.length()).mapTo(categories) { array[it].toString().toInt() }
        val dates = mutableListOf<DateRange>()

        val geoArray = json.getJSONObject("geo")
        val geo = GeoCoordinates(geoArray.getDouble("lat"), geoArray.getDouble("lon"))
        val article = MapLocation(
                json.getLong("id"),
                mapToUserCategory(categories).toShort(),
                geo,
                json.getString("title"),
                json.getString("abstractText"),
                json.getString("articleText"),
                dates,
                json.getString("address"),
                json.getInt("postalCode")
        )

        articles.add(article)

    }
    Log.d("ApiController", articles.size.toString())
    return articles
}

internal fun mapToUserCategory(categories: List<Int>): Int {
    return if (41 in categories || 25 in categories) 1
    else if (61 in categories) 2
    else if (60 in categories || 63 in categories) 3
    else if (71 in categories) 4
    else if (26 in categories || 38 in categories) 5
    else 1
}

data class MapLocation constructor(val id: Long,
                                   val categoryId: Short,
                                   val geo: GeoCoordinates,
                                   val title: String,
                                   val abstractText: String,
                                   val article: String,
                                   val dates: List<DateRange>,
                                   val address: String,
                                   val postalCode: Int
)

class GeoCoordinates constructor(val lat: Double, val lon: Double)

class DateRange constructor(val from: Date, val to: Date?)
