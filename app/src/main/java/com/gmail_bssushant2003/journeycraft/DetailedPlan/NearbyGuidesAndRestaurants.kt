package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gmail_bssushant2003.journeycraft.Fragments.GuidesFragment
import com.gmail_bssushant2003.journeycraft.Fragments.RestaurantsFragment
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityNearbyGuidesAndRestaurantsBinding

class NearbyGuidesAndRestaurants : AppCompatActivity() {

    private lateinit var binding : ActivityNearbyGuidesAndRestaurantsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyGuidesAndRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.appbackcolor1, theme)

        //receive latLng list from intent
//        val placesLatLngList = intent.getSerializableExtra("placesLatLngList") as? ArrayList<LatLng>

        //set the fragments
        replaceFragment(GuidesFragment())

        // Handle bottom navigation item selection
        binding.bottomBar.onItemSelected = { position ->
            when (position) {
                0 -> replaceFragment(GuidesFragment())
                1 -> replaceFragment(RestaurantsFragment())
            }
        }


//        binding.buttonSubmit.setOnClickListener {
//            sendLocationsToServer(placesLatLngList)
//        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


}