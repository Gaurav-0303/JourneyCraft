package com.gmail_bssushant2003.journeycraft.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.GuidesAdapter
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants.nearbyRestGuideApiUrl
import com.gmail_bssushant2003.journeycraft.Constants.ApiService
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import com.gmail_bssushant2003.journeycraft.databinding.FragmentGuidesBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GuidesFragment : Fragment() {

    private lateinit var binding: FragmentGuidesBinding
    private lateinit var guidesList : ArrayList<Guide>

//    private var placesLatLngList: ArrayList<LatLng>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        guidesList = arrayListOf()

        binding.guidesRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = GuidesAdapter(requireContext(), guidesList, parentFragmentManager)
        binding.guidesRv.adapter = adapter

        sendLocationsToServer(adapter)
    }


    private fun sendLocationsToServer(adapter: GuidesAdapter) {


        val ref = com.google.firebase.database.FirebaseDatabase
            .getInstance()
            .getReference("guides")

        ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                guidesList.clear()
                for (guideSnapshot in snapshot.children) {
                    val guide = guideSnapshot.getValue(Guide::class.java)
                    guide?.let { guidesList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("FirebaseError", "Error: ${error.message}")
            }
        })

//        RetrofitClient.apiService.findAllGuides().enqueue(object : Callback<List<Guide>> {
//            override fun onResponse(call: Call<List<Guide>>, response: Response<List<Guide>>) {
//                if (response.isSuccessful) {
//                    val fetchedGuides = response.body() ?: emptyList()
//                    guidesList.clear()  // Clear old data
//                    guidesList.addAll(fetchedGuides)  // Add new data
//                    adapter.notifyDataSetChanged()  // Refresh RecyclerView
//                } else {
//                    Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Guide>>, t: Throwable) {
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