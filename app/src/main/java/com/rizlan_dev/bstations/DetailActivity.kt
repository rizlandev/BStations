package com.rizlan_dev.bstations

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mLocationRequest: LocationRequest? = null

    private val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private val latitude = 0.0
    private val longitude = 0.0

    private lateinit var mGoogleMap: GoogleMap

    lateinit var txt_view_id_lable: TextView
    lateinit var txt_view_avb_bike: TextView
    lateinit var txt_view_avb_space: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_view)



        var bundle :Bundle ?=intent.extras
        val id_lbl = bundle!!.getString("id_label")
        val avb_bike = bundle!!.getString("avb_bike")
        val avb_space = bundle!!.getString("avb_space")
        val lat = bundle!!.getString("lat")
        val lon = bundle!!.getString("lon")



        // findViewById and set view id
        txt_view_id_lable = findViewById(R.id.tv_id_name)
        txt_view_avb_bike = findViewById(R.id.tv_avb_bike)
        txt_view_avb_space = findViewById(R.id.tv_avb_place)

        txt_view_id_lable.text = id_lbl
        txt_view_avb_bike.text = avb_bike
        txt_view_avb_space.text = avb_space

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment



        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    protected fun startLocationUpdates() {
        // initialize location request object
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.run {
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            setInterval(UPDATE_INTERVAL)
            setFastestInterval(FASTEST_INTERVAL)
        }

        // initialize locationo setting request builder object
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // initialize location service object
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient!!.checkLocationSettings(locationSettingsRequest)

        // call register location listner
        //registerLocationListner()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mGoogleMap = googleMap;

        if (mGoogleMap != null) {
            mGoogleMap!!.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title("Current Location"))
        }

    }

//    private fun registerLocationListner() {
//        // initialize location callback object
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                onLocationChanged(locationResult!!.getLastLocation())
//            }
//        }
//        // add permission if android version is greater then 23
//        if(Build.VERSION.SDK_INT >= 23 && checkPermission()) {
//            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
//        }
//    }

    private fun onLocationChanged(location: Location) {
        // create message for toast with updated latitude and longitudefa
        var msg = "Updated Location: " + location.latitude  + " , " +location.longitude

        // show toast message with updated location
        //Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
        val location = LatLng(location.latitude, location.longitude)
        mGoogleMap!!.clear()
        mGoogleMap!!.addMarker(MarkerOptions().position(location).title("Current Location"))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        Toast.makeText(this, "Debug XXXX "+msg, Toast.LENGTH_LONG).show()
    }

    private fun checkPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions()
            return false
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"),1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1) {
            if (permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION ) {
               // registerLocationListner()
            }
        }
    }




}
