package de.slg.datadome

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Rect
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


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var googleMap: GoogleMap? = null
    private val perm = 5
    private val AACHEN = LatLng(50.77580397992759, 6.091018809604975)
    private val ZOOM_LEVEL = 14f
    private lateinit var bottomSheetDialog: BottomSheetBehavior<NestedScrollView>
    private val enabledCategories = mapOf(1 to true, 2 to true, 3 to true, 4 to true, 5 to true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        this.test()
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

    private fun test() {
        val dat = mutableListOf<DateRange>()
        val geo = GeoCoordinates(1.0, 2.0)
        val katliste = mutableListOf<Short>(2)
        val obj1 = MapLocation(334787, 2, geo, "hallo", "hallo1", "hallo1", dat, "dffi", 424523)
        val obj2 = MapLocation(334787, 1, geo, "hallo", "hallo", "hallo", dat, "dffi", 424523)
        val testliste = mutableListOf(obj1, obj2)

        val listefertig = filterCategory(testliste, katliste)

        val xy = listefertig[0]

        Log.d("Main", "xy: " + xy.abstractText)
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
}


