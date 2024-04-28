package com.gmail_bssushant2003.journeycraft.Questions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.gmail_bssushant2003.journeycraft.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        window.statusBarColor = resources.getColor(R.color.white, theme)

        findViewById<ImageButton>(R.id.next_button).setOnClickListener {
            val mobileNumber = intent.getStringExtra("mobilenumber")
            Log.d("Prateek", mobileNumber.toString())

            val intent = Intent(this, NameActivity::class.java)
            intent.putExtra("mobilenumber", mobileNumber)
            startActivity(intent)
        }



    }
}