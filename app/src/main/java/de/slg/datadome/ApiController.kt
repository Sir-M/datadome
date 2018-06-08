package de.slg.datadome

import java.util.*

open class Article constructor(val id: Long,
                               val categoryIds: List<Short>,
                               val geo: GeoCoordinates,
                               val title: String,
                               val abstractText: String,
                               val article: String,
                               val dates: List<DateRange>,
                               val adress: String,
                               val postalCode: Int
)

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


