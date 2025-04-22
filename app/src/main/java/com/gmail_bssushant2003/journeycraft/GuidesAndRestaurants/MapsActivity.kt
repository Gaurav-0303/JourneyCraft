package com.gmail_bssushant2003.journeycraft.GuidesAndRestaurants

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gmail_bssushant2003.journeycraft.Fragments.GuideDetailsDialogFragment
import com.gmail_bssushant2003.journeycraft.Fragments.GuidesFragment.RetrofitClient
import com.gmail_bssushant2003.journeycraft.Fragments.RestaurantDetailsDialogFragment
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.Models.Restaurant
import com.gmail_bssushant2003.journeycraft.Models.RestaurantFirebase
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.math.ln

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityMapsBinding
    private lateinit var googleMap: GoogleMap
    private var guideList = ArrayList<Guide>()
    private var restaurantList = ArrayList<Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.white, theme)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.guide_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val placeLatLng = intent.getParcelableExtra<LatLng>("placeLatLng")
        googleMap.addMarker(MarkerOptions().position(placeLatLng!!).title("Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15f))

        googleMap.uiSettings.isMapToolbarEnabled = true

        Log.d("Gaurav", "Location ${placeLatLng}")

        val placeLatLngList : ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng> = arrayListOf(com.gmail_bssushant2003.journeycraft.Models.LatLng(placeLatLng.latitude, placeLatLng.longitude))

        lifecycleScope.launch {
            showLoadingBar(true)
            sendLocationsToServerForGuide(placeLatLngList)
            sendLocationToServerForRestaurant(placeLatLngList)
            showLoadingBar(false)
        }

        googleMap.setOnMarkerClickListener { marker ->
            val guide = marker.tag as? Guide
            val restaurant = marker.tag as? Restaurant

            when {
                guide != null -> showGuideDetailsDialog(guide)
                restaurant != null -> showRestaurantDetailsDialog(restaurant)
            }
            true  // Return true to consume the click event
        }
    }

    private fun showLoadingBar(isVisible: Boolean) {
        binding.loadingBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    private suspend fun sendLocationToServerForRestaurant(placeLatLngList: ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng>) {
        val restaurantList = mutableListOf<Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>>()

        try {
            RetrofitClient.apiService.findNearbyRestaurants(placeLatLngList).enqueue(object :
                Callback<List<Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>>> {
                override fun onResponse(
                    call: Call<List<Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>>>,
                    response: Response<List<Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>>>
                ) {
                    if (response.isSuccessful) {
                        val fetchedData = response.body() ?: emptyList()
                        restaurantList.addAll(fetchedData)

                        for (restaurant in restaurantList) {
                            Log.d("Gaurav", "Restaurant Name: ${restaurant.second.name}")
                            addPinToRestaurantLocation(restaurant)
                        }
                    } else {
                        Log.e("ResponseError", "API failed: ${response.errorBody()?.string()}")
                        fetchRestFromFirebaseFallback(placeLatLngList)
                    }
                }

                override fun onFailure(
                    call: Call<List<Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>>>,
                    t: Throwable
                ) {
                    Log.e("NetworkError", "Failed: ${t.message}")
                    fetchRestFromFirebaseFallback(placeLatLngList)
                }
            })
        } catch (e: Exception) {
            Log.e("Exception", "Error: ${e.message}")
            fetchRestFromFirebaseFallback(placeLatLngList)
        }
    }

    private fun fetchRestFromFirebaseFallback(placeLatLngList: ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng>) {
        val ref = FirebaseDatabase.getInstance().getReference("restaurants")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val restaurantFirebase = child.getValue(RestaurantFirebase::class.java)
                    if (restaurantFirebase != null) {
                        val lat = restaurantFirebase.latitude ?: 0.0
                        val lng = restaurantFirebase.longitude ?: 0.0

                        for (latLng in placeLatLngList) {
                            val distanceResult = FloatArray(1)
                            Location.distanceBetween(
                                latLng.latitude, latLng.longitude,
                                lat,
                                lng,
                                distanceResult
                            )

                            if (distanceResult[0] <= 10_000) { // 10km radius
                                // 🔄 Convert RestaurantFirebase → Restaurant
                                val restaurant = Restaurant(
                                    id = restaurantFirebase.id ?: 0,
                                    name = restaurantFirebase.name ?: "Unknown",
                                    description = restaurantFirebase.description ?: "",
                                    rating = restaurantFirebase.rating ?: 0.0,
                                    phoneNo = restaurantFirebase.phoneNo ?: "999999999",
                                    openTime = restaurantFirebase.openTime ?: "10:10",
                                    closeTime = restaurantFirebase.closeTime ?: "10:10",
                                    fssaiLicense = restaurantFirebase.fssaiLicense ?: "142424",
                                    locationLink = restaurantFirebase.locationLink ?: "",
                                    averageCost = restaurantFirebase.averageCost ?: 500.0,

                                    foodType = try {
                                        Restaurant.FoodType.valueOf((restaurantFirebase.foodType ?: "BOTH").toString())
                                    } catch (e: Exception) {
                                        Restaurant.FoodType.BOTH
                                    }                                )

                                restaurantList.add(Pair(com.gmail_bssushant2003.journeycraft.Models.LatLng(lat, lng), restaurant))
                                Log.d("Gaurav", "Restaurant from Firebase: ${restaurant.name}")
                                addPinToRestaurantLocation(Pair(com.gmail_bssushant2003.journeycraft.Models.LatLng(lat, lng), restaurant))
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Failed: ${error.message}")
            }
        })
    }



    private fun addPinToRestaurantLocation(restaurant: Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, Restaurant>) {
        val location = LatLng(restaurant.first.latitude, restaurant.first.longitude)


        // Create a custom green marker
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(resizeMarker(this,R.drawable.restaurant_pin,120,120))  // Green Pin

        googleMap.addMarker(markerOptions)


        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = restaurant.second

    }

    private fun addPinToRestaurantLocationFirebase(restaurant: Pair<com.gmail_bssushant2003.journeycraft.Models.LatLng, RestaurantFirebase>) {
        val location = LatLng(restaurant.first.latitude, restaurant.first.longitude)


        // Create a custom green marker
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(resizeMarker(this,R.drawable.restaurant_pin,120,120))  // Green Pin

        googleMap.addMarker(markerOptions)


        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = restaurant.second

    }

    private fun addPinToGuideLocation(guide: Guide) {
        val location = LatLng(guide.latitude, guide.longitude)


        // Create a custom green marker
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(resizeMarker(this,R.drawable.ic_guide_icon,120,120))  // Green Pin

        googleMap.addMarker(markerOptions)


        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = guide
    }

    private fun showGuideDetailsDialog(guide: Guide) {
        val dialog = GuideDetailsDialogFragment(guide)
        dialog.show(supportFragmentManager, "GuideDetailsDialog")
    }

    private fun showRestaurantDetailsDialog(restaurant: Restaurant) {
        val dialog = RestaurantDetailsDialogFragment(restaurant)
        dialog.show(supportFragmentManager, "RestaurantDetailsDialog")
    }


    private suspend fun sendLocationsToServerForGuide(placesLatLngList: ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng>?) {
        try {
            RetrofitClient.apiService.findNearbyGuides(placesLatLngList!!).enqueue(object :
                Callback<List<Guide>> {
                override fun onResponse(call: Call<List<Guide>>, response: retrofit2.Response<List<Guide>>) {
                    if (response.isSuccessful) {
                        val fetchedData = response.body() ?: emptyList()
                        guideList.addAll(fetchedData)

                        for (guide in guideList) {
                            Log.d("Gaurav", "Guide Name: ${guide.name}")
                            addPinToGuideLocation(guide)
                        }
                    } else {
                        Log.e("GuideAPI", "API failed: ${response.errorBody()?.string()}")
                        fetchGuidesFromFirebase(placesLatLngList)
                    }
                }

                override fun onFailure(call: Call<List<Guide>>, t: Throwable) {
                    Log.e("GuideAPI", "API error: ${t.message}")
                    fetchGuidesFromFirebase(placesLatLngList)
                }
            })
        } catch (e: Exception) {
            Log.e("GuideAPI", "Exception: ${e.message}")
            fetchGuidesFromFirebase(placesLatLngList!!)
        }
    }

    private fun fetchGuidesFromFirebase(placesLatLngList: ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng>) {
        val ref = FirebaseDatabase.getInstance().getReference("guides")

        ref.get().addOnSuccessListener { snapshot ->
            for (guideSnap in snapshot.children) {
                val guide = guideSnap.getValue(Guide::class.java)
                guide?.let {
                    for (latLng in placesLatLngList) {
                        val distanceResult = FloatArray(1)
                        Location.distanceBetween(
                            latLng.latitude, latLng.longitude,
                            it.latitude ?: 0.0,
                            it.longitude ?: 0.0,
                            distanceResult
                        )

                        if (distanceResult[0] <= 10_000) { // 10km radius
                            guideList.add(it)
                            Log.d("Gaurav", "Guide from Firebase: ${it.name}")
                            addPinToGuideLocation(it)
                        }
                    }
                }
            }
        }.addOnFailureListener {
            Log.e("FirebaseFallback", "Failed to fetch guides from Firebase: ${it.message}")
        }
    }



    fun resizeMarker(context: Context, drawableRes: Int, width: Int, height: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}