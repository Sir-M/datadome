package de.slg.datadome

import java.util.*

const val IP = "http://172.26.210.84"
const val PORT = 25565

fun getArticleList(): List<Article> {
    val response = khttp.get("$IP:$PORT")
    val entries = response.jsonArray
    val articles = mutableListOf<Article>()

    for (i in 0 until entries.length()) {
        val json = entries.getJSONObject(i)
        val categories = mutableListOf<Short>()

        val array = json.getJSONArray("categoryIds")
        (0 until array.length()).mapTo(categories) { array[it] as Short }

        val dates = mutableListOf<DateRange>()
        val geoArray = json.getJSONObject("geo")
        val geo = GeoCoordinates(geoArray.getDouble("lat"), geoArray.getDouble("lon"))
        val article = Article(
                json.getLong("id"),
                categories,
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

open class Article constructor(val id: Long,
                               val categoryIds: List<Short>,
                               val geo: GeoCoordinates,
                               val title: String,
                               val abstractText: String,
                               val article: String,
                               val dates: List<DateRange>,
                               val adress: String,
                               val postalCode: Int)

class MapLocation constructor(val categoryId: Int,
                              id: Long,
                              categoryIds: List<Short>,
                              geo: GeoCoordinates,
                              title: String,
                              abstractText: String,
                              article: String,
                              dates: List<DateRange>,
                              adress: String,
                              postalCode: Int
) : Article(id, categoryIds, geo, title, abstractText, article, dates, adress, postalCode)

class GeoCoordinates constructor(val lat: Double, val lon: Double)

class DateRange constructor(val from: Date, val to: Date?)