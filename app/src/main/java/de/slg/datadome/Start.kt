package de.slg.datadome

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

var baseLocations = listOf<MapLocation>()

class Start : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        async(UI) {
            try {
                val job = async(CommonPool) {
                    baseLocations = getLocationList()
                }
                job.await()
                startActivity(Intent(this@Start, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
            }
        }
    }

}
