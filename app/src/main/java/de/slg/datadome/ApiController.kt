package de.slg.datadome

import java.util.*

const val IP = "http://172.26.210.84"
const val PORT = 25565

fun getLocationList(): List<MapLocation> {
    val response = khttp.get("$IP:$PORT")
    val entries = response.jsonArray
    val articles = mutableListOf<MapLocation>()

    for (i in 0 until entries.length()) {
        val json = entries.getJSONObject(i)
        val categories = mutableListOf<Int>()

        val array = json.getJSONArray("categoryIds")
        (0 until array.length()).mapTo(categories) { array[it] as Int }

        val dates = mutableListOf<DateRange>()
        val geoArray = json.getJSONObject("geo")
        val geo = GeoCoordinates(geoArray.getDouble("lat"), geoArray.getDouble("lon"))
        val article = MapLocation(
                json.getLong("id"),
                mapToUserCategory(categories),
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

    return articles
}

internal fun mapToUserCategory(categories: List<Int>): Short {
    val oldId = categories[0]
    return when (oldId) {
        41, 25 -> 1
        61 -> 2
        60, 63 -> 3
        71 -> 4
        26, 38 -> 5
        else -> 1
    }
}

class MapLocation constructor(val id: Long,
                              val categoryId: Short,
                              val geo: GeoCoordinates,
                              val title: String,
                              val abstractText: String,
                              val article: String,
                              val dates: List<DateRange>,
                              val adress: String,
                              val postalCode: Int
)

class GeoCoordinates constructor(val lat: Double, val lon: Double)

class DateRange constructor(val from: Date, val to: Date?)
