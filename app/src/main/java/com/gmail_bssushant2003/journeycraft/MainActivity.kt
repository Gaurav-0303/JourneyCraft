package com.gmail_bssushant2003.journeycraft

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gmail_bssushant2003.journeycraft.DetailedPlan.InputActivity
import com.gmail_bssushant2003.journeycraft.DetailedPlan.NearbyGuidesAndRestaurants
import com.gmail_bssushant2003.journeycraft.IntercityTransport.IntercityTransportActivity
import com.gmail_bssushant2003.journeycraft.MustVisitPlaces.MustVisitPlaces
import com.gmail_bssushant2003.journeycraft.Notifications.LocationService
import com.gmail_bssushant2003.journeycraft.Transport.TransportActivity
import com.gmail_bssushant2003.journeycraft.Weather.Activity.WeatherMainActivity
import com.gmail_bssushant2003.journeycraft.databinding.ActivityMainBinding
import com.google.maps.model.LatLng

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val latLngMap : Map<String, LatLng> = mapOf(
        "Kolhapur" to LatLng(16.691307, 74.244865),
        "Goa" to LatLng(15.496777, 73.827827),
        "Ooty" to LatLng(11.410000,76.699997),
        "Mahabaleshwar" to LatLng(17.921721, 73.655602),
        "Lakshadweep" to LatLng(11.7056501,72.7152889),
        "Manali" to LatLng(32.239632,77.188713)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var latLng : LatLng = LatLng(16.691307, 74.244865)

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)

        val individualDestination = intent.getStringExtra("individualDestination")
        val destinationstate = intent.getStringExtra("statedestination")

        val textViewindidest = findViewById<TextView>(R.id.individualdest)
        val textViewstate = findViewById<TextView>(R.id.statedest)

        textViewindidest.text = individualDestination
        textViewstate.text = destinationstate

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        //on click available transport
        binding.availableTransport.setOnClickListener {
            val intent = Intent(this, TransportActivity::class.java)
            intent.putExtra("destinationLat", latLngMap[individualDestination]!!.lat)
            intent.putExtra("destinationLng", latLngMap[individualDestination]!!.lng)
            startActivity(intent)
        }

        //on click intercity transport
        binding.intercityTransport.setOnClickListener {
            val intent = Intent(this, IntercityTransportActivity::class.java)
            startActivity(intent)
        }


        //on click weather card
        binding.weatherCard.setOnClickListener {
            val intent = Intent(this, WeatherMainActivity::class.java)
            intent.putExtra("lat", latLngMap[individualDestination]!!.lat)
            intent.putExtra("lon", latLngMap[individualDestination]!!.lng)
            intent.putExtra("name", individualDestination)
            startActivity(intent)
        }

        binding.musttryfooditems.setOnClickListener{
            val intent = Intent(this,MustVisitPlaces::class.java)
            startActivity(intent)
        }

        binding.cardDetailedPlan.setOnClickListener{
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        binding.guidesandrestaurants.setOnClickListener {
            val intent = Intent(this, NearbyGuidesAndRestaurants::class.java)
            startActivity(intent)
        }

    }
}