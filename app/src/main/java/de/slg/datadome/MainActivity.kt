package de.slg.datadome

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.compat.R.id.async
import android.support.v4.app.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    private var googleMap: GoogleMap? = null
    private val perm = 5;
    private val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    private val ZOOM_LEVEL = 14f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)  //the map is loaded asynchronously

    }

    // inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) { //manages and initialises fragments
    //  val fragmentTransaction = beginTransaction()
    //   fragmentTransaction.func()
    //    fragmentTransaction.commit()
    //  }

    override fun onMapReady(p0: GoogleMap?) {
        p0 ?: return
        googleMap = p0

        googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_custom))
        //var b = googleMap?.setMapStyle(MapStyleOptions(getString(R.string.custom_look)))
        //Log.i("Map", "map loading sucess: " + b.toString())

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(AACHEN, ZOOM_LEVEL)) //Zoom on Aachen

        //  val circleDrawable = ContextCompat.getDrawable(this, R.drawable.ic_marker_bus)
        // val markerIcon = getMarkerIconFromDrawable(circleDrawable!!)

        googleMap?.addMarker(MarkerOptions().position(AACHEN).snippet("Geschloseeeen. Dahaha!").title("Cassolette")) //MARKER, testing purposes in Aachen

        var ui = googleMap?.uiSettings
        ui?.isRotateGesturesEnabled = false
        ui?.isMapToolbarEnabled = false

        // async(UI) {
        // var list: List<Bus.GPSData> = listOf()
        // val job = async(CommonPool) {
        //    list = getAllGPSData()
        //  }
        //  job.await()
        Log.i("GPS1", "Job done")

        //  for (a in list) {
        //     val latLng = LatLng(a.latitude, a.longitude)
        //  Log.i("GPS1", "LAT: ${a.latitude}")
        //googleMap?.addMarker(MarkerOptions().position(AACHEN).icon(markerIcon).title("BUSNUMMER: 17").snippet("Passagiere: 7")) //Hard-coded. Will be changed
        //  Log.i("GPS1", "Marker set")

        // }

        //}


        enableMyLocation() //location services
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.wtf("TAG", "enableL")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), perm)
        } else if (googleMap != null) {
            googleMap?.isMyLocationEnabled = true

        }
    }

    fun onMyLocationButtonClick(): Boolean {
        //default behaviour
        return false
    }

    private fun test() {
        val nums2 = mutableListOf<Short>(38)
        val nums = mutableListOf<Short>(61, 71)
        val dat = mutableListOf<DateRange>()
        val geo = GeoCoordinates(1.0, 2.0)
        val katliste = mutableListOf<Short>(2)
        val obj1 = Article(334787, nums, geo, "hallo", "hallo", "hallo", dat, "dffi", 424523)
        val obj2 = Article(334787, nums2, geo, "hallo", "hallo", "hallo", dat, "dffi", 424523)
        val testliste = mutableListOf<Article>(obj1, obj2)

        Filter.filterCategory()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            perm -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    enableMyLocation()
                }
                //else no perm
            }
            else -> {
            }
        }
    }


}
