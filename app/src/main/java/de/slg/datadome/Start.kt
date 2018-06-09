package de.slg.datadome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import android.net.ConnectivityManager



var baseLocations = listOf<MapLocation>()

class Start : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        async(UI) {
            try {
                if (isNetworkAvailable()) {
                    val job = async(CommonPool) {
                        baseLocations = getLocationList()
                    }
                    job.await()
                }
                startActivity(Intent(this@Start, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
