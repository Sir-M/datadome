package de.slg.datadome

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.graphics.Color
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
import android.view.Gravity
import android.view.View
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.async
import android.view.Window
import android.widget.*
import android.icu.util.*
import android.widget.*
import kotlinx.android.synthetic.main.dialog_filter.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {


    private var googleMap: GoogleMap? = null
    private val perm = 5
    private val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    private val ZOOM_LEVEL = 14f
    private lateinit var locations: List<MapLocation>
    private val enabledCategories = mutableMapOf(1 to true, 2 to true, 3 to true, 4 to true, 5 to true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)  //the map is loaded asynchronously

        findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
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
=======
    private fun test() {
        val dat = mutableListOf<DateRange>()
        val geo = GeoCoordinates(1.0, 2.0)
        val katliste = mutableListOf<Short>(2)
        val obj1 = MapLocation(334787, 2, geo, "hallo", "hallo1", "hallo1", dat, "dffi", 424523)
        val obj2 = MapLocation(334787, 1, geo, "hallo", "hallo", "hallo", dat, "dffi", 424523)
        val testliste = mutableListOf<MapLocation>(obj1, obj2)

        val listefertig = filterCategory(testliste, katliste)
>>>>>>> c59968da3e677cdb936e9a8b03cdfdecd4bc258d

           val xy = listefertig.get(0)

           Log.d("Main","xy: "+xy.abstractText)*/


    override fun onMarkerClick(p0: Marker?): Boolean {
        var markerID = p0?.tag
        // if(markerID != null){


        var b = BottomSheetDialog()
        b.setTitle("test0")
        b.show(this.supportFragmentManager, "test")

        //  }
        return false
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

        val textDate = dialog.findViewById<TextView>(R.id.textView_date)
        textDate.text = getString(R.string.date_today)
        val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar)
        seekBar.progress = 0

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textDate.text = getDateName(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is started.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        val fabStage = dialog.findViewById<ImageButton>(R.id.fabStage)
        fabStage.setOnClickListener(this)

        val fabFood = dialog.findViewById<ImageButton>(R.id.fabFood)
        fabFood.setOnClickListener(this)

        val fabNight = dialog.findViewById<ImageButton>(R.id.fabNight)
        fabNight.setOnClickListener(this)

        val fabMuseum = dialog.findViewById<ImageButton>(R.id.fabMuseum)
        fabMuseum.setOnClickListener(this)

        val fabMusic = dialog.findViewById<ImageButton>(R.id.fabMusic)
        fabMusic.setOnClickListener(this)

        val wlp = dialog.window.attributes
        //  wlp.x = 150
        // wlp.y = 200
        wlp.gravity = Gravity.TOP
        dialog.window.attributes = wlp

        Log.i("MainActivity", "btnClose: " + btnClose.toString())
        dialog.show()
    }

    private fun getDateName(p0: Int): String {
        when (p0) {
            0 -> {
                return getString(R.string.date_today)
            }
            1 -> {
                return getString(R.string.date_tomorrow)
            }
            2 -> {
                return getString(R.string.date_week)
            }
            3 -> {
                return getString(R.string.date_month)
            }
            4 -> {
                return getString(R.string.date_all)
            }
        }
        return ""
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fabStage) {
            Log.i("onClick", "fabStage")
            if (enabledCategories.getOrDefault(0, true)) {
                v.findViewById<ImageButton>(R.id.fabStage).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(0, false)
            } else {
                Log.i("onClick", "fabStage false")
                v.findViewById<ImageButton>(R.id.fabStage).background = getDrawable(R.drawable.background)
                enabledCategories.put(0, true)
            }

        } else if (v?.id == R.id.fabFood) {
            if (enabledCategories.getOrDefault(1, true)) {
                v.findViewById<ImageButton>(R.id.fabFood).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(1, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabFood).background = getDrawable(R.drawable.background)
                enabledCategories.put(1, true)
            }

        } else if (v?.id == R.id.fabNight) {
            if (enabledCategories.getOrDefault(2, true)) {
                v.findViewById<ImageButton>(R.id.fabNight).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(2, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabNight).background = getDrawable(R.drawable.background)
                enabledCategories.put(2, true)
            }

        } else if (v?.id == R.id.fabMuseum) {
            if (enabledCategories.getOrDefault(3, true)) {
                v.findViewById<ImageButton>(R.id.fabMuseum).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(3, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabMuseum).background = getDrawable(R.drawable.background)
                enabledCategories.put(3, true)
            }

        } else if (v?.id == R.id.fabMusic) {
            if (enabledCategories.getOrDefault(4, true)) {
                v.findViewById<ImageButton>(R.id.fabMusic).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(4, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabMusic).background = getDrawable(R.drawable.background)
                enabledCategories.put(4, true)
            }

        }
    }
}


