package de.slg.datadome

class Article constructor(val id: Long,
                          val categoryIds: List<Short>,
                          val geo: GeoCoordinates,
                          val title: String,
                          val abstractText: String,
                          val article: String,
                          val dates: List<Long>,
                          val adress: String,
                          val postalCode: Int)

class GeoCoordinates constructor(val lat: Double, val lon: Double)