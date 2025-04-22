package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityRouteDesignerBinding
import com.google.android.gms.maps.model.LatLng
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RouteDesigner : AppCompatActivity() {

    private lateinit var binding : ActivityRouteDesignerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDesignerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.buttonSubmit.setOnClickListener { solve() }

    }

    private fun solve() {
        val place = binding.place.text.toString()
        val st = binding.st.text.toString()
        val et = binding.et.text.toString()

        callAPI(place, st, et)
    }

    private fun callAPI(place: String, st: String, et: String) {

        val client = OkHttpClient()

        val baseUrl = "http://192.168.1.4:5000/data?startloc=$place&starttime=$st&endtime=$et"

        val request = Request.Builder()
            .url(baseUrl)
            .get()
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
                        var arrayList : ArrayList<String> = arrayListOf()

                        responseBody?.let { body ->
                            arrayList = body.split(",").map { it.trim() }.toMutableList() as ArrayList<String>
                        }

                        for(i in arrayList) Log.d("Gaurav", i.toString())

                    }
                    catch (e : Exception){
                        e.printStackTrace()
                    }
                }
                else{
                    val responseBody = response.body?.string()
                    Log.d("Gaurav", "Failed to load response due to: $responseBody")
                }
            }

        })
    }
}