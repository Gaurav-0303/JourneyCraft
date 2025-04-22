package com.gmail_bssushant2003.journeycraft.IntercityTransport

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityIntercityTransportBinding
import com.gmail_bssushant2003.journeycraft.databinding.ActivityTransportBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class IntercityTransportActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var coordinatesList: ArrayList<LatLng>
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityIntercityTransportBinding
    private val LOCATION_REQUEST_CODE = 100
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var userLatLng: LatLng
    private var client = OkHttpClient()
    private var isMetro = false
    private var isTaxi = false
    private var isBus = false
    private lateinit var metroLatLng : LatLng
    private lateinit var taxiLatLng : LatLng
    private lateinit var busLatLng : LatLng

    var firstApiCall = 0
    var secondApiCall = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntercityTransportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.WHITE

        coordinatesList = arrayListOf()
        userLatLng = LatLng(16.3914, 74.1544)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        checkPermissionStatus()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        metroLatLng = LatLng(0.0, 0.0)
        taxiLatLng = LatLng(0.0, 0.0)
        busLatLng = LatLng(0.0, 0.0)

        uiWork(mMap)
    }

    private fun uiWork(mMap : GoogleMap) {
        binding.cardMetro.setOnClickListener {
            isMetro = !isMetro
            isTaxi = false
            isBus = false
            changeLayout(isMetro, isTaxi, isBus)

            if(isMetro){
                mMap.clear()
                findNearestPoint("metro station", userLatLng)
            }
        }
        binding.cardTaxi.setOnClickListener {
            isTaxi = !isTaxi
            isMetro = false
            isBus = false
            changeLayout(isMetro, isTaxi, isBus)

            if(isTaxi){
                mMap.clear()
                findNearestPoint("taxi station", userLatLng)
            }
        }
        binding.cardBus.setOnClickListener {
            isBus = !isBus
            isTaxi = false
            isMetro = false
            Log.d("Gaurav", userLatLng.toString())
            changeLayout(isMetro, isTaxi, isBus)

            if(isBus){
                mMap.clear()
                findNearestPoint("bus station", userLatLng)
            }
        }
    }

    private fun changeLayout(isMetro: Boolean, isTaxi: Boolean, isBus: Boolean){
        if(isMetro){
            binding.cardMetro.backgroundTintList = getColorStateList(R.color.colorPrimary)
        }
        if(!isMetro){
            binding.cardMetro.backgroundTintList = getColorStateList(R.color.white)
        }

        if(isTaxi){
            binding.cardTaxi.backgroundTintList = getColorStateList(R.color.colorPrimary)
        }
        if(!isTaxi){
            binding.cardTaxi.backgroundTintList = getColorStateList(R.color.white)
        }

        if(isBus){
            binding.cardBus.backgroundTintList = getColorStateList(R.color.colorPrimary)
        }
        if(!isBus){
            binding.cardBus.backgroundTintList = getColorStateList(R.color.white)
        }
    }

    private fun findNearestPoint(
        place: String,
        userCurrentLocation: LatLng
    ){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Generating Route...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("${ApiConstants.findNearestLocationApiUrl}?query=$place&lat=${userCurrentLocation.latitude}&lng=${userCurrentLocation.longitude}&limit=5&language=en&region=us")
            .get()
            .addHeader("X-RapidAPI-Key", ApiConstants.findNearestLocationApiKey)
            .addHeader("X-RapidAPI-Host", "local-business-data.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                responseData?.let {
                    try {
                        val jsonResponse = JSONObject(it)
                        val dataArray = jsonResponse.getJSONArray("data")
                        val firstElement = dataArray.getJSONObject(1)
                        val lat = firstElement.getDouble("latitude")
                        val lng = firstElement.getDouble("longitude")
                        val nearestLocation = LatLng(lat, lng)

                        runOnUiThread {
                            val markerOptions = MarkerOptions()
                            markerOptions.position(nearestLocation)
                            mMap.addMarker(markerOptions)

                            // Add user current location because we cleared map
                            mMap.addMarker(MarkerOptions().position(userLatLng))
                            coordinatesList.clear()
                        }
                        firstApiCall++
                        callApi(userLatLng, nearestLocation, progressDialog)
                    } catch (e: JSONException) {
                        Log.e("Gaurav", "Error parsing JSON: ${e.message}")
                        runOnUiThread {
                            Toast.makeText(this@IntercityTransportActivity, "Some error occurred", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }
                    }
                } ?: runOnUiThread {
                    Toast.makeText(this@IntercityTransportActivity, "Some error occurred", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            }


            override fun onFailure(call: Call, e: IOException) {
                Log.e("Gaurav", "Network request failed: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@IntercityTransportActivity, "Some error occurred", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            }
        })
    }

    private fun callApi(
        userLatLng: LatLng,
        destinationLatLng: LatLng,
        progressDialog: ProgressDialog
    ) {

//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Generating route...")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
        secondApiCall++

        val baseUrl = "https://trueway-directions2.p.rapidapi.com/"
        val endpoint = "FindDrivingPath"
        val origin = "${userLatLng.latitude},${userLatLng.longitude}" // Example coordinates, replace with actual values
        val destination = "${destinationLatLng.latitude},${destinationLatLng.longitude}" // Example coordinates, replace with actual values

        val url = "${ApiConstants.findDrivingPathApiUrl}?origin=$origin&destination=$destination"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", ApiConstants.findDrivingPathApiKey)
            .addHeader("X-RapidAPI-Host", "trueway-directions2.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Gaurav", "1]failed to load response due to ${e.message}")
//                progressDialog.dismiss()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    try {
                        val responseBody = response.body?.string()
                        Log.d("Gaurav", "Response body: $responseBody")

                        val jsonObject = JSONObject(responseBody)
                        val distance = jsonObject.getJSONObject("route").getInt("distance")
                        val geometryObject = jsonObject.getJSONObject("route").getJSONObject("geometry")
                        val coordinatesArray = geometryObject.getJSONArray("coordinates")

                        Log.d("XXX", distance.toString())

                        for (i in 0 until coordinatesArray.length()) {
                            val coordinateArray = coordinatesArray.getJSONArray(i)
                            val latitude = coordinateArray.getDouble(0)
                            val longitude = coordinateArray.getDouble(1)
                            val latLng = LatLng(latitude, longitude)
                            coordinatesList.add(latLng)
                        }

                        runOnUiThread {
                            drawPolyline(coordinatesList)
                            progressDialog.dismiss()
                        }
                    }
                    catch (e : Exception){
                        e.printStackTrace()
                        runOnUiThread {
                            Toast.makeText(this@IntercityTransportActivity, "Some error occurred", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }
                    }
                }
                else{
                    val responseBody = response.body?.string()
                    Log.d("Gaurav", "Failed to load response due to: $responseBody")
                    runOnUiThread {
                        Toast.makeText(this@IntercityTransportActivity, "Some error occurred", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
                }
            }

        })
    }

    private fun drawPolyline(coordinatesList: java.util.ArrayList<LatLng>) {
        val polylineOptions = PolylineOptions()
            .addAll(coordinatesList)
            .color(Color.RED)
            .width(12f)
            .startCap(RoundCap())
            .endCap(RoundCap())

        mMap.addPolyline(polylineOptions)
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lan = location.longitude
                userLatLng = LatLng(lat, lan)

                mMap.addMarker(
                    MarkerOptions()
                        .position(userLatLng)
                        .title("Current Location")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0F))
                mMap.uiSettings.isMapToolbarEnabled = true
            }
        }

    }

    private fun checkPermissionStatus() {
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            //get the location
            getUserLocation()
        } else {
            requestForPermission()
        }
    }

    private fun requestForPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Accepted", Toast.LENGTH_LONG).show()
                getUserLocation()
            } else {
                Toast.makeText(this, "Permission Rejected", Toast.LENGTH_LONG).show()
            }
        }
    }

}