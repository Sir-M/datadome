package de.slg.datadome

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {

    private var googleMap: GoogleMap? = null
    private val perm = 5
    private val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    private val ZOOM_LEVEL = 14f
    private lateinit var bottomSheetDialog: BottomSheetBehavior<NestedScrollView>
    private var locations: List<MapLocation> = mutableListOf()
    private val enabledCategories = mutableMapOf(1 to true, 2 to true, 3 to true, 4 to true, 5 to true)
    private var seekBarProgress = 4

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

        findViewById<ImageButton>(R.id.btn_close_2).setOnClickListener { bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN }
        bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN

    }

    override fun onMapReady(p0: GoogleMap?) {
        p0 ?: return
        googleMap = p0
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(AACHEN, ZOOM_LEVEL)) //Zoom on Aachen

        val b = googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_custom))
        Log.i("Map", "map loading sucess: " + b.toString())

        val ui = googleMap?.uiSettings
        ui?.isRotateGesturesEnabled = false
        ui?.isMapToolbarEnabled = false
        googleMap?.setOnInfoWindowClickListener(this)

        locations = baseLocations
        filterList(4, enabledCategories)

        enableMyLocation() //location services
    }


    private fun drawOnMap(m: MapLocation, id: Int) {
        googleMap ?: return
        val geoPos = LatLng(m.geo.lat, m.geo.lon)

        var icon = getMarkerIconFromDrawable(getDrawable(R.drawable.ic_ausgehen))
        when (m.categoryId.toInt()) {
            1 -> {
                icon = getMarkerIconFromDrawable(getDrawable(R.drawable.ic_buehne))

            }
            2 -> {
                icon = getMarkerIconFromDrawable(getDrawable(R.drawable.ic_restaurant))

            }
            3 -> {
                icon = getMarkerIconFromDrawable(getDrawable(R.drawable.ic_ausgehen))

            }
            4 -> {
                icon = getMarkerIconFromDrawable(getDrawable(R.drawable.ic_museum))

            }
            5 -> {
                icon = getMarkerIconFromDrawable(getDrawable(R.drawable.ic_music))

            }
        }
        val marker = googleMap?.addMarker(MarkerOptions().position(geoPos).title(Html.fromHtml(m.title).toString()).icon(icon))

        marker?.tag = id


    }

    override fun onInfoWindowClick(marker: Marker) {
        val markerID = marker.tag
        Log.i("MARKER Type", markerID.toString())
        val markedLoc = locations[markerID as Int]

        val title = findViewById<TextView>(R.id.title)
        val abstract = findViewById<TextView>(R.id.abstractText)
        val content = findViewById<TextView>(R.id.content)
        val address = findViewById<TextView>(R.id.address)

        title.text = Html.fromHtml(markedLoc.title)
        abstract.text = Html.fromHtml(markedLoc.abstractText)
        content.text = Html.fromHtml(markedLoc.article)
        address.text = markedLoc.address

        var sumHeight = 100
        title.doOnPreDraw {
            sumHeight += title.measuredHeight
            abstract.doOnPreDraw {
                sumHeight += abstract.measuredHeight
                bottomSheetDialog.peekHeight = sumHeight
                bottomSheetDialog.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

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
        dialog.setContentView(R.layout.dialog_filter)

        dialog.findViewById<ImageButton>(R.id.btn_close).setOnClickListener { dialog.cancel() }
        val textDate = dialog.findViewById<TextView>(R.id.textView_date)
        val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar)
        seekBar.progress = seekBarProgress
        textDate.text = getDateName(seekBarProgress)

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textDate.text = getDateName(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        val cardStage = dialog.findViewById<CardView>(R.id.cardStage)
        cardStage.setOnClickListener(this)
        val cardFood = dialog.findViewById<CardView>(R.id.cardFood)
        cardFood.setOnClickListener(this)
        val cardNight = dialog.findViewById<CardView>(R.id.cardNight)
        cardNight.setOnClickListener(this)
        val cardMuseum = dialog.findViewById<CardView>(R.id.cardMuseum)
        cardMuseum.setOnClickListener(this)
        val cardMusic = dialog.findViewById<CardView>(R.id.cardMusic)
        cardMusic.setOnClickListener(this)

        dialog.setOnDismissListener {
            seekBarProgress = seekBar.progress
            filterList(seekBarProgress, enabledCategories)
        }
        val wlp = dialog.window.attributes
        wlp.gravity = Gravity.TOP
        dialog.window.attributes = wlp
        dialog.show()
    }

    private fun filterList(timeIndex: Int, categories: MutableMap<Int, Boolean>) {
        val hashliste = categories.toList()
        val liste = mutableListOf<Short>()
        for ((a, current) in hashliste.withIndex()) {
            val value = current.second
            if (value) {
                liste.add(a, current.first.toShort())
            }
        }
        val speicher = getTimespans(timeIndex, baseLocations)
        val speicher2 = filterCategory(speicher, liste)
        i = 0
        for ((i, current2) in speicher2.withIndex()) {
            drawOnMap(current2, i)

        }
        locations = speicher2

    }

    private fun getTimespans(p0: Int, liste: List<MapLocation>): List<MapLocation> {
        val d = Date()
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

    /**
     * Handles the clicks on the buttons used in this class, mainly used for the ImageButtons in the filtering dialog,
     * which have an changing background.
     *
     * @param v
     *
     */
    override fun onClick(v: View?) {
        if (v?.id == R.id.cardStage) {
            Log.i("onClick", "fabStage")
            if (enabledCategories.getOrDefaultExtended(0, true)) {
                v.findViewById<ImageButton>(R.id.fabStage).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(0, false)
            } else {
                Log.i("onClick", "fabStage false")
                v.findViewById<ImageButton>(R.id.fabStage).background = getDrawable(R.drawable.background)
                enabledCategories.put(0, true)
            }

        } else if (v?.id == R.id.cardFood) {
            if (enabledCategories.getOrDefaultExtended(1, true)) {
                v.findViewById<ImageButton>(R.id.fabFood).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(1, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabFood).background = getDrawable(R.drawable.background)
                enabledCategories.put(1, true)
            }

        } else if (v?.id == R.id.cardNight) {
            if (enabledCategories.getOrDefaultExtended(2, true)) {
                v.findViewById<ImageButton>(R.id.fabNight).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(2, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabNight).background = getDrawable(R.drawable.background)
                enabledCategories.put(2, true)
            }

        } else if (v?.id == R.id.cardMuseum) {
            if (enabledCategories.getOrDefaultExtended(3, true)) {
                v.findViewById<ImageButton>(R.id.fabMuseum).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(3, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabMuseum).background = getDrawable(R.drawable.background)
                enabledCategories.put(3, true)
            }

        } else if (v?.id == R.id.cardMusic) {
            if (enabledCategories.getOrDefaultExtended(4, true)) {
                v.findViewById<ImageButton>(R.id.fabMusic).background = getDrawable(R.drawable.background_white)
                enabledCategories.put(4, false)
            } else {
                v.findViewById<ImageButton>(R.id.fabMusic).background = getDrawable(R.drawable.background)
                enabledCategories.put(4, true)
            }

        }
    }

    private fun MutableMap<Int, Boolean>.getOrDefaultExtended(index: Int, default: Boolean) : Boolean {
        return this[index] ?: default
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

