package com.gmail_bssushant2003.journeycraft.Questions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.DestinationListActivity
import com.gmail_bssushant2003.journeycraft.Models.Preferences
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPreferenceBinding

class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding
    private var isLiftedTemple = false
    private var isLiftedBeach = false
    private var isLiftedHistorical = false
    private var isLiftedMountain = false
    private var isLiftedLake = false
    private var isLiftedMuseum = false
    private var isLiftedPark = false
    private var isLiftedWildlife = false
    private lateinit var recordFile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change color of status bar
        window.statusBarColor = resources.getColor(R.color.white, theme)
        recordFile = getSharedPreferences("records", Context.MODE_PRIVATE)

        retriveFromPref()
        animateCardView(binding.templeCard, isLiftedTemple)
        animateCardView(binding.beachCard, isLiftedBeach)
        animateCardView(binding.historicalPlacesCard, isLiftedHistorical)
        animateCardView(binding.mountainCard, isLiftedMountain)
        animateCardView(binding.lakesCard, isLiftedLake)
        animateCardView(binding.museumCard, isLiftedMuseum)
        animateCardView(binding.parkCard, isLiftedPark)
        animateCardView(binding.wildlifeCard, isLiftedWildlife)

        preferenceSelection()

        binding.buttonDone.setOnClickListener {
            if(isAnyTrue()){
                val preferences = Preferences(isLiftedTemple, isLiftedBeach, isLiftedHistorical, isLiftedMountain, isLiftedLake, isLiftedMuseum, isLiftedPark, isLiftedWildlife)
                val mobileNumber = intent.getStringExtra("mobilenumber")
                val name = intent.getStringExtra("name")
                val intent = Intent(this, DestinationListActivity::class.java)
                intent.putExtra("mobilenumber", mobileNumber)
                intent.putExtra("name", name)
                intent.putExtra("preferences", preferences)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Please select your preference", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun retriveFromPref() {
        isLiftedBeach = recordFile.getBoolean("isLiftedBeach", false)
        isLiftedTemple = recordFile.getBoolean("isLiftedTemple", false)
        isLiftedHistorical = recordFile.getBoolean("isLiftedHistorical", false)
        isLiftedMountain = recordFile.getBoolean("isLiftedMountain", false)
        isLiftedMuseum = recordFile.getBoolean("isLiftedMuseum", false)
        isLiftedLake = recordFile.getBoolean("isLiftedLake", false)
        isLiftedPark = recordFile.getBoolean("isLiftedPark", false)
        isLiftedWildlife = recordFile.getBoolean("isLiftedWildlife", false)
    }

    private fun isAnyTrue(): Boolean {
        return (isLiftedBeach || isLiftedTemple || isLiftedHistorical || isLiftedMountain || isLiftedMuseum
                || isLiftedLake || isLiftedPark || isLiftedWildlife)
    }

    private fun preferenceSelection() {

        binding.templeCard.setOnClickListener {
            isLiftedTemple = !isLiftedTemple
            animateCardView(binding.templeCard, isLiftedTemple)
        }

        binding.beachCard.setOnClickListener {
            isLiftedBeach = !isLiftedBeach
            animateCardView(binding.beachCard, isLiftedBeach)
        }

        binding.historicalPlacesCard.setOnClickListener {
            isLiftedHistorical = !isLiftedHistorical
            animateCardView(binding.historicalPlacesCard, isLiftedHistorical)
        }

        binding.mountainCard.setOnClickListener {
            isLiftedMountain = !isLiftedMountain
            animateCardView(binding.mountainCard, isLiftedMountain)
        }

        binding.lakesCard.setOnClickListener {
            isLiftedLake = !isLiftedLake
            animateCardView(binding.lakesCard, isLiftedLake)
        }

        binding.museumCard.setOnClickListener {
            isLiftedMuseum = !isLiftedMuseum
            animateCardView(binding.museumCard, isLiftedMuseum)
        }

        binding.parkCard.setOnClickListener {
            isLiftedPark = !isLiftedPark
            animateCardView(binding.parkCard, isLiftedPark)
        }

        binding.wildlifeCard.setOnClickListener {
            isLiftedWildlife = !isLiftedWildlife
            animateCardView(binding.wildlifeCard, isLiftedWildlife)
        }
    }

    private fun animateCardView(cardView: CardView, isLifted: Boolean) {

        val scaleFactor = if (isLifted) 0.85f else 1.0f
        cardView.animate()
            .scaleX(scaleFactor)
            .scaleY(scaleFactor)
            .setDuration(100)
            .start()

        val context = cardView.context

        // Find existing tick icon in the card view
        val imageView = cardView.findViewWithTag<ImageView>("tick_icon")

        // If the tick icon doesn't exist, create a new one and add it to the card view
        if (isLifted && imageView == null) {
            var layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val newImageView = ImageView(context).apply {
                layoutParams = layoutParams
                setImageResource(R.drawable.baseline_done_24)
                tag = "tick_icon" // Set a tag to identify the tick icon
            }
            cardView.addView(newImageView)
        } else if (!isLifted && imageView != null) { // If the tick icon exists and should be removed
            cardView.removeView(imageView)
        }
    }
}