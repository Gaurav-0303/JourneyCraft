package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.Adapters.PlacesAdapter
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants
import com.gmail_bssushant2003.journeycraft.GuidesAndRestaurants.MapsActivity
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import com.gmail_bssushant2003.journeycraft.Models.TripRecord
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPlanBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PlanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlanBinding
    private lateinit var placesList : ArrayList<String>
    private lateinit var timeList : ArrayList<String>
    private lateinit var finalPlacesList : ArrayList<String>
    private lateinit var tempList : ArrayList<String>
    private lateinit var myAdapter : PlacesAdapter
    private lateinit var mainList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placesList = arrayListOf()
        finalPlacesList = arrayListOf()
        tempList = arrayListOf()
        timeList = arrayListOf()
        mainList = arrayListOf()

        window.statusBarColor = resources.getColor(R.color.white, theme)

        //take data from intent
        val place = intent.getIntExtra("Place", 0)
        val startTime = intent.getStringExtra("StartTime")
        val endTime = intent.getStringExtra("EndTime")


        //check if it is an active plan
        val tripData = intent.getStringExtra("tripData")
        if(tripData != null){

            var cnt = 0

            for (item in tripData.split(";")) {
                cnt++
                if(cnt % 2 != 0) placesList.add(item.trim())
                else timeList.add(item.trim())
            }

            updateUI(placesList, timeList)
        }
        else{
            tempList = arrayListOf(
                "Shri Chatrapati Shahu Museum (New Palace)",
                "Dajipur Wildlife Sanctuary",
                "Panhala Fort",
                "Mahalaxmi Temple, Kolhapur",
                "Jyotiba Temple",
                "DYP City Mall",
                "Siddhagiri Museum"
            )
            callAPI(place.toString(), startTime!!, endTime!!)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        myAdapter = PlacesAdapter(this, placesList)
//        binding.recyclerView.adapter = myAdapter



        //help page
        binding.imageViewHelp.setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))

//            lifecycleScope.launch {
//                var placesLatLngList = readCSV(this@PlanActivity, mainList)
//                Log.d("Gaurav", placesLatLngList.toString())
//                val intent = Intent(this@PlanActivity, NearbyGuidesAndRestaurants::class.java)
//                intent.putExtra("placesLatLngList", ArrayList(placesLatLngList))
//                startActivity(intent)
//            }


        }

    }

    private fun goInMap(placeList : ArrayList<String>) {
        
        var loc : String = ""

        for(i in placeList){
            loc = "$loc$i/"
        }


        val gmmIntentUri1 = Uri.parse("https://www.google.com/maps/dir/$loc")

        val mapIntent1 = Intent(Intent.ACTION_VIEW, gmmIntentUri1)

        mapIntent1.`package` = "com.google.android.apps.maps"

        if (mapIntent1.resolveActivity(packageManager) != null) {
            startActivity(mapIntent1)
        } else {
            Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
        }

    }


//
//    private suspend fun readCSV(context: Context, places: ArrayList<String>): List<LatLng> = withContext(
//        Dispatchers.IO) {
//        val placesLatLngList = mutableListOf<LatLng>()
//
//        val inputStream = context.assets.open("places_lat_lng.csv")
//        val reader = BufferedReader(InputStreamReader(inputStream))
//
//        reader.useLines { lines ->
//            lines.drop(1).forEach { line -> // Skip header row
//                val tokens = line.split("~")
//                if (tokens.size >= 3) {
//                    val placeFromCsv = tokens[0].trim()
//                    val lat = tokens[1].trim().toDoubleOrNull() ?: 0.0
//                    val lng = tokens[2].trim().toDoubleOrNull() ?: 0.0
//
//                    if (places.any { it.equals(placeFromCsv, ignoreCase = true) }) {
//                        placesLatLngList.add(LatLng(lat, lng))
//                    }
//                }
//            }
//        }
//
//        return@withContext placesLatLngList
//    }


    private fun callAPI(place: String, st: String, et: String) {

        val client = OkHttpClient()

        Log.d("Sushant", place)
        Log.d("Sushant", st)
        Log.d("Sushant", et)

        val progressBar = ProgressDialog(this@PlanActivity).apply {
            setMessage("Loading...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }

        val baseUrl = "${ApiConstants.showDetailedPlanApiUrl}?startloc=$place&starttime=$st&endtime=$et"

        val request = Request.Builder()
            .url(baseUrl)
            .get()
            .build()

//        progressBar.show()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Gaurav", "1]failed to load response due to ${e.message}")
//                progressDialog.dismiss()

                updateUI(tempList, timeList)
            }

            override fun onResponse(call: Call, response: Response) {
//                progressBar.hide()
                if(response.isSuccessful){
                    try {
                        val responseBody = response.body?.string()
                        var cnt = 0

                        //store response in firebase
                        storeResponseInFirebase(responseBody!!)

                        Log.d("Adarsh", responseBody.toString())

                        if (responseBody != null) {
                            for (item in responseBody.split(";")) {
                                cnt++
                                if(cnt % 2 != 0) placesList.add(item.trim())
                                else timeList.add(item.trim())
                            }
                        }


                        for(i in placesList){
                            if(i == "" || i == "Kolhapur") continue
                            finalPlacesList.add(i)
                        }

                        updateUI(finalPlacesList, timeList)

                    }
                    catch (e : Exception){
                        progressBar.hide()
                        e.printStackTrace()
                    }
                }
                else{
                    val responseBody = response.body?.string()
                    progressBar.hide()
                    Log.d("Gaurav", "Failed to load response due to: $responseBody")
                }
            }

            private fun storeResponseInFirebase(response: String) {
                val recordFile = getSharedPreferences("records", MODE_PRIVATE)
                val phoneNumber = recordFile.getString("phoneNumber", "")

                val database = FirebaseDatabase.getInstance()
                val tripRef = database.getReference("trips").child("$phoneNumber")

                val customUid = System.currentTimeMillis().toString()
                val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date()
                )

                // Create a TripRecord object
                val tripRecord = TripRecord(response, currentDateTime)

                tripRef.child(customUid).setValue(tripRecord)
            }


        })

    }

    private suspend fun getLatLngForPlace(context: Context, placeName: String): LatLng? = withContext(Dispatchers.IO) {
        var placeLatLng: LatLng? = null

        val inputStream = context.assets.open("places_lat_lng.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.drop(1).forEach { line -> // Skip header row
                val tokens = line.split("~")
                if (tokens.size >= 3) {
                    val placeFromCsv = tokens[0].trim()
                    val lat = tokens[1].trim().toDoubleOrNull() ?: 0.0
                    val lng = tokens[2].trim().toDoubleOrNull() ?: 0.0


                    // Check if the place name matches
                    if (placeFromCsv.equals(placeName, ignoreCase = true)) {
                        placeLatLng = LatLng(lat, lng)
                    }
                } else {
                    Log.e("CSVParser", "Skipping line with invalid format: $line")
                }
            }
        }

        return@withContext placeLatLng
    }


    fun updateUI(placeList : ArrayList<String>, timeList : ArrayList<String>) {
        runOnUiThread {
            myAdapter = PlacesAdapter(this@PlanActivity, placeList, timeList)
            binding.recyclerView.adapter = myAdapter

            mainList = placeList


            // by map logo
            binding.mapLogo.setOnClickListener {
                goInMap(placeList)
            }

            myAdapter.setOnClickListener(object : MyAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
//                    val gmmIntentUri1 =
//                        "https://www.google.com/maps/dir/?api=1&destination=${placeList[position]}".toUri()
//
//                    val mapIntent1 = Intent(Intent.ACTION_VIEW, gmmIntentUri1)
//
//                    mapIntent1.`package` = "com.google.android.apps.maps"
//
//                    if (mapIntent1.resolveActivity(packageManager) != null) {
//                        startActivity(mapIntent1)
//                    } else {
//                        Toast.makeText(
//                            this@PlanActivity,
//                            "Google Maps app is not installed",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }

                    lifecycleScope.launch {
                        val placeLatLng = getLatLngForPlace(this@PlanActivity, placeList[position])
                        val intent = Intent(this@PlanActivity, MapsActivity::class.java)
                        val googleMapLatLng = com.google.android.gms.maps.model.LatLng(placeLatLng!!.latitude, placeLatLng!!.longitude)

                        intent.putExtra("placeLatLng", googleMapLatLng)
                        startActivity(intent)
                    }
                }
            })
        }
    }
}