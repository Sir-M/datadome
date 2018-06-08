package de.slg.datadome

import java.util.*

fun filterCategory(locations: List<MapLocation>, categories: List<Short>): List<MapLocation> {

    val filteredList = mutableListOf<MapLocation>()

    for (loc in locations) {
        for (s in categories) {
            if (loc.categoryId == s) {
                filteredList.add(loc)
                break
            }
        }
    }

    return filteredList
}

fun filterTime(articleList: List<MapLocation>, from: Date, to: Date): List<MapLocation> {
    val filteredList: MutableList<MapLocation> = mutableListOf()
    for (current in articleList) {
        if (current.dates.isEmpty())
            filteredList.add(current)
        else {
            for (d in current.dates) {
                if (d.from.before(from) && d.to!!.after(to))
                    filteredList.add(current)
                else if (d.from.before(from) && d.to == null)
                    filteredList.add(current)

            }
        }

    }

    return filteredList
}