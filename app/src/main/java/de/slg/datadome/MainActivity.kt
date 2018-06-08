package de.slg.datadome

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Rect
import android.icu.util.Calendar
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.doOnPreDraw
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
    private lateinit var bottomSheetDialog: BottomSheetBehavior<NestedScrollView>
    private lateinit var locations: List<MapLocation>
    private val enabledCategories = mutableMapOf(1 to true, 2 to true, 3 to true, 4 to true, 5 to true)
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)  //the map is loaded asynchronously

        findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
            showDialogFilter()


        }

        bottomSheetDialog = BottomSheetBehavior.from<NestedScrollView>(findViewById(R.id.bottom_sheet))

        bottomSheetDialog
        bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            if (bottomSheetDialog.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN

                val outRect = Rect()
                findViewById<NestedScrollView>(R.id.bottom_sheet).getGlobalVisibleRect(outRect)

                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt()))
                    bottomSheetDialog.state = BottomSheetBehavior.STATE_COLLAPSED

            }
        }
        return super.dispatchTouchEvent(ev)
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

        val iterator = baseLocations.iterator()
        i = 0
        iterator.forEach { }

        enableMyLocation() //location services

    }

    private fun drawOnMap(m: MapLocation) {
        googleMap ?: return
        val geoPos = LatLng(m.geo.lat, m.geo.lon)
        googleMap?.addMarker(MarkerOptions().position(geoPos).title(m.title))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        var markerID = p0?.tag
        // if(markerID != null){

        //  }
        val title = findViewById<TextView>(R.id.title)
        val abstract = findViewById<TextView>(R.id.abstractText)
        val content = findViewById<TextView>(R.id.content)

        abstract.setText("Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands" +
                "Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands" +
                "Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands ")

        content.setText("Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands" +
                "Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands" +
                "Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands " +
                "sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands\" +\n" +
                "                \"Das ist ein sehr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das dasdkajdands" +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "" +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das " +
                "hr langer text, der über mehrere Zeilen geht ez nadknajkdlkd adaksda das ")

        var sumHeight = 100
        title.doOnPreDraw {
            sumHeight += title.measuredHeight
            abstract.doOnPreDraw {
                sumHeight += abstract.measuredHeight
                bottomSheetDialog.peekHeight = sumHeight
                bottomSheetDialog.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
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
        textDate.text = getString(R.string.date_all)
        val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar)
        seekBar.progress = 4

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

        dialog.setOnDismissListener {
            val hashliste = enabledCategories.toList()
            val liste = mutableListOf<Short>()
            for ((a, current) in hashliste.withIndex()) {
                val value = current.second
                if (value) {
                    liste.add(a, current.first.toShort())
                }
            }
            val speicher = getTimespans(seekBar.progress, baseLocations)
            val speicher2 = filterCategory(speicher, liste)
            for ((i, current2) in speicher2.withIndex()) {
            //    drawOnMap(current2, i) toDo
            }


        }

        val wlp = dialog.window.attributes
        //  wlp.x = 150
        // wlp.y = 200
        wlp.gravity = Gravity.TOP
        dialog.window.attributes = wlp

        Log.i("MainActivity", "btnClose: " + btnClose.toString())
        dialog.show()
    }

    private fun getTimespans(p0: Int, liste: List<MapLocation>): List<MapLocation> {
        val d = Date();
        when (p0) {
            0 -> {
                return filterTime(liste, d, d)

            }
            1 -> {
                val cur = Date()
                ++cur.date

                return filterTime(liste, cur, cur)

            }
            2 -> {
                val cur = Date()
                7 + cur.date

                return filterTime(liste, d, cur)

            }
            3 -> {
                val cur = Date()
                ++cur.month
                return filterTime(liste, d, cur)

            }
            4 -> {
                val cur = Date()
                10 + cur.year

                return filterTime(liste, Date(), cur)

            }
        }
        return emptyList()
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


