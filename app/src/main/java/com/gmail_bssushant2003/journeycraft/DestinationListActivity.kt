package com.gmail_bssushant2003.journeycraft

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.Login.SendOTPActivity
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.Models.Preferences
import com.gmail_bssushant2003.journeycraft.Questions.PreferenceActivity
import com.gmail_bssushant2003.journeycraft.databinding.ActivityDestinationListBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class DestinationListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDestinationListBinding
    private lateinit var itemsList: ArrayList<Items>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var buttondrawer: ImageButton
    private lateinit var navigationView: NavigationView
    private lateinit var recordFile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemsList = arrayListOf()
        recordFile = getSharedPreferences("records", Context.MODE_PRIVATE)

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)

        //retrieve data from intent
        val preferences = intent.getSerializableExtra("preferences") as? Preferences
        val mobileNumber = intent.getStringExtra("mobilenumber")
        val name = intent.getStringExtra("name")

        if (preferences != null) {
            savePreferences(preferences)
        }

        Log.d("Gaurav", preferences.toString())

        if (!recordFile.contains("mobilenumber") && !recordFile.contains("name")) {
            recordFile.edit {
                putString("mobileNumber", mobileNumber)
                putString("name", name)
            }
        }
        else if(!mobileNumber.isNullOrBlank() && !name.isNullOrBlank()){
            recordFile.edit {
                putString("mobileNumber", mobileNumber)
                putString("name", name)
            }
        }

        destinationList()
        leftSideNavBar()
    }

    private fun savePreferences(preferences: Preferences) {
        val editor = recordFile.edit()
        editor.putBoolean("isLiftedTemple", preferences.isLiftedTemple)
        editor.putBoolean("isLiftedBeach", preferences.isLiftedBeach)
        editor.putBoolean("isLiftedHistorical", preferences.isLiftedHistorical)
        editor.putBoolean("isLiftedMountain", preferences.isLiftedMountain)
        editor.putBoolean("isLiftedLake", preferences.isLiftedLake)
        editor.putBoolean("isLiftedMuseum", preferences.isLiftedMuseum)
        editor.putBoolean("isLiftedPark", preferences.isLiftedPark)
        editor.putBoolean("isLiftedWildlife", preferences.isLiftedWildlife)
        editor.apply()
    }

    private fun leftSideNavBar() {
        drawerLayout = findViewById(R.id.drawer_layout)
        buttondrawer = findViewById(R.id.three_lines)

        buttondrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)

            findViewById<TextView>(R.id.text_name_nav).text = recordFile.getString("name", "XYZ")
            findViewById<TextView>(R.id.text_mobile_number_nav).text = recordFile.getString("mobileNumber", "XXXXXXXXXX")
        }

        navigationView = findViewById(R.id.navview)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navlogout -> {
                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(this, SendOTPActivity::class.java))
                    finish()
                    true
                }
                R.id.navEditPreference -> {
                    startActivity(Intent(this, PreferenceActivity::class.java))
                }
            }
            drawerLayout.close()
            true
        }
    }

    private fun destinationList() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        itemsList = arrayListOf(
            Items(R.drawable.kolhapur, "Kolhapur", "Maharashtra, India"),
            Items(R.drawable.goa, "Goa", "Goa, India"),
            Items(R.drawable.ooty, "Ooty", "Tamil Nadu, India"),
            Items(R.drawable.mahabaleshwar, "Mahabaleshwar", "Maharashtra, India"),
            Items(R.drawable.lakshadweep, "Lakshadweep", "Lakshadweep , India"),
            Items(R.drawable.manali, "Manali", "Himachal Pradesh, India"),
        )


        val myAdapter = MyAdapter(this, itemsList)
        binding.recyclerView.adapter = myAdapter

        myAdapter.setOnClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@DestinationListActivity, MainActivity::class.java)
                intent.putExtra("individualDestination", itemsList[position].title)
                intent.putExtra("statedestination",itemsList[position].location)
                startActivity(intent)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}