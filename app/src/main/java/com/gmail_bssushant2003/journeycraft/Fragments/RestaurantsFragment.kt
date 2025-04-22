package com.gmail_bssushant2003.journeycraft.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.GuidesAdapter
import com.gmail_bssushant2003.journeycraft.Adapters.RestaurantsAdapter
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants.nearbyRestGuideApiUrl
import com.gmail_bssushant2003.journeycraft.Constants.ApiService
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import com.gmail_bssushant2003.journeycraft.Models.Restaurant
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.FragmentGuidesBinding
import com.gmail_bssushant2003.journeycraft.databinding.FragmentRestaurantsBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantsBinding
    private lateinit var restaurantsList : ArrayList<Restaurant>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantsList = arrayListOf()

        binding.restaurantRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RestaurantsAdapter(requireContext(), restaurantsList, parentFragmentManager)
        binding.restaurantRv.adapter = adapter

        sendLocationsToServer(adapter)
    }

    private fun sendLocationsToServer(adapter: RestaurantsAdapter) {

        val ref = com.google.firebase.database.FirebaseDatabase
            .getInstance()
            .getReference("restaurants")  // Make sure this matches your Firebase node

        ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                restaurantsList.clear()
                for (restaurantSnapshot in snapshot.children) {
                    val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)
                    restaurant?.let { restaurantsList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
            }
        })


//        RetrofitClient.apiService.findAllRestaurants().enqueue(object :
//            Callback<List<Restaurant>> {
//            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
//                if (response.isSuccessful) {
//                    val fetchedRestaurants = response.body() ?: emptyList()
//                    restaurantsList.clear()  // Clear old data
//                    restaurantsList.addAll(fetchedRestaurants)  // Add new data
//                    adapter.notifyDataSetChanged()  // Refresh UI
//                } else {
//                    Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
//                Log.e("NetworkError", "Failed: ${t.message}")
//            }
//        })

    }

    object RetrofitClient {
        private var BASE_URL = nearbyRestGuideApiUrl

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }

}