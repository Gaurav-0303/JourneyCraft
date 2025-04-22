package com.gmail_bssushant2003.journeycraft.NavBar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.PlanHistoryAdapter
import com.gmail_bssushant2003.journeycraft.Models.DetailedTripRecord
import com.gmail_bssushant2003.journeycraft.Models.TripRecord
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPlanHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlanHistory : AppCompatActivity() {

    private lateinit var binding : ActivityPlanHistoryBinding
    private lateinit var detailedTripRecordList : ArrayList<DetailedTripRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailedTripRecordList = arrayListOf()

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.appbackcolor1, theme)

        //get history list from intent

        val historyList = intent.getSerializableExtra("historyList") as? ArrayList<TripRecord>

        binding.historyRv.layoutManager = LinearLayoutManager(this)
        val adapter = PlanHistoryAdapter(this, detailedTripRecordList)
        binding.historyRv.adapter = adapter

        putData(historyList!!, adapter)
    }

    private fun putData(historyList: ArrayList<TripRecord>, adapter: PlanHistoryAdapter) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) // 26 March 2025
        val outputTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // 12:35 AM / PM

        for (record in historyList) {
            val timestamp = record.timestamp
            try {
                val date = inputFormat.parse(timestamp)
                val formattedDate = outputDateFormat.format(date!!)
                val formattedTime = outputTimeFormat.format(date)

                // Log or use the formatted values
                detailedTripRecordList.add(DetailedTripRecord(record.response, formattedDate, formattedTime))
                println("Date: $formattedDate, Time: $formattedTime")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        adapter.notifyDataSetChanged()
    }
}