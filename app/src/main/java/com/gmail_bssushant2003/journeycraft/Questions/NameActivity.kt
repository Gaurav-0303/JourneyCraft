package com.gmail_bssushant2003.journeycraft.Questions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.gmail_bssushant2003.journeycraft.R

class NameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        window.statusBarColor = resources.getColor(R.color.white, theme)

        val name = findViewById<EditText>(R.id.input_name)

        findViewById<ImageButton>(R.id.next_button).setOnClickListener {
            val nameText = name.text.toString().trim()
            if(nameText.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                val mobileNumber = intent.getStringExtra("mobilenumber")
                val intent = Intent(this, PreferenceActivity::class.java)
                intent.putExtra("mobilenumber", mobileNumber)
                intent.putExtra("name", nameText)
                startActivity(intent)
            }
        }
    }
}