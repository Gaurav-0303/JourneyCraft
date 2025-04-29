package com.gmail_bssushant2003.journeycraft.GuidesAndRestaurants

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityDetailedGuideBinding

class DetailedGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive Guide object from intent
        val guide = intent.getSerializableExtra("guideData") as? Guide

        // Populate UI with guide details
        guide?.let {
            binding.name.text = it.guidename ?: "XYZ"
            binding.experience.text = it.experience.toString()
//            binding.guideLanguages.text = "Languages: ${it.language ?: "Not specified"}"
            binding.bio.text = it.bio
            binding.phoneNumber.text = it.phoneNo
//            binding.guideLocation.text = "Location: ${it.latitude}, ${it.longitude}"

            val ph = it.phoneNo

//            binding.callButton.setOnClickListener {
//                ph?.let { number ->
//                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
//                    startActivity(dialIntent)
//                }
//            }
        }
    }
}