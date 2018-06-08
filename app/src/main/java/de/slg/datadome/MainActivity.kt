package de.slg.datadome

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ImageButton
import android.view.Gravity



class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var googleMap: GoogleMap? = null
    private val perm = 5
    private val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    private val ZOOM_LEVEL = 14f

    private lateinit var locations: List<MapLocation>
    private val enabledCategories = mapOf(1 to true, 2 to true, 3 to true, 4 to true, 5 to true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)  //the map is loaded asynchronously

        val fab = findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
            showDialogFilter()


        }


    }


    override fun onMapReady(p0: GoogleMap?) {
        p0 ?: return
        googleMap = p0
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(AACHEN, ZOOM_LEVEL)) //Zoom on Aachen

        val b = googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_custom))
        Log.i("Map", "map loading sucess: " + b.toString())

        //  val circleDrawable = ContextCompat.getDrawable(this, R.drawable.ic_marker_bus)
        // val markerIcon = getMarkerIconFromDrawable(circleDrawable!!)

        val m1 = googleMap?.addMarker(MarkerOptions().position(AACHEN).snippet("Geschloseeeen. Dahaha!").title("Cassolette")) //MARKER, testing purposes in Aachen
        m1?.tag = 0

        val ui = googleMap?.uiSettings
        ui?.isRotateGesturesEnabled = false
        ui?.isMapToolbarEnabled = false
        googleMap?.setOnMarkerClickListener(this)

        enableMyLocation() //location services

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

    }

    /*   private fun test() {


           val dat = mutableListOf<DateRange>()
           val geo = GeoCoordinates(1.0, 2.0)
           val katliste = mutableListOf<Short>(2)
           val obj1 = MapLocation(334787, 2, geo, "hallo", "hallo1", "hallo1", dat, "dffi", 424523)
           val obj2 = MapLocation(334787, 1, geo, "hallo", "hallo", "hallo", dat, "dffi", 424523)
           val testliste = mutableListOf<MapLocation>(obj1, obj2)

           val listefertig = filterCategory(testliste,katliste)

           val xy = listefertig.get(0)

           Log.d("Main","xy: "+xy.abstractText)*/


    override fun onMarkerClick(p0: Marker?): Boolean {
        var markerID = p0?.tag
        // if(markerID != null){


        var b = BottomSheetDialog()
        b.setTitle("test0")
        b.show(this.supportFragmentManager, "test")
        b.
                //  }
                return false


    }


  /*  private fun fullBottomSheet() {
        b = findViewById(R.i)
    }*/


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


    private fun showDialogFilter() {
        val dialog = Dialog(this)
        //val v = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_filter, null)
        // val v = layoutInflaterAndroid.inflate (R.layout.dialog_filter);
        dialog.setContentView(R.layout.dialog_filter)
        // val builderInfo = AlertDialog.Builder(this)

        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            dialog.cancel()
        }


        val wlp = dialog.window.attributes
        //  wlp.x = 150
        // wlp.y = 200
        wlp.gravity = Gravity.TOP
        dialog.window.attributes = wlp

        Log.i("MainActivity", "btnClose: " + btnClose.toString())
        //  builderInfo.setTitle(getString(app_name))
        ///  builderInfo.setIcon(R.drawable.ic_pigmentv3)
        //builderInfo.setMessage(getString(info1))

        //     builderInfo.setPositiveButton(
        //      getString(ok), DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
///
        //  builderInfo.setNeutralButton(
        //          getString(website),
        //         DialogInterface.OnClickListener { dialog, id ->
        //        })

        // ImageView appLogo = findViewById(R.id.applogo);
        //  appLogo.setImageResource(R.drawable.ic_pigmentv3);


        //builderInfo.setView(v)
        // val alertDialog = builderInfo.create()
        //  alertDialog.show()
        // / val wlp = alertDialog.window.attributes
        //   wlp.gravity = Gravity.TOP
        dialog.show()
    }
}


