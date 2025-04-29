package com.gmail_bssushant2003.journeycraft

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.DetailedPlan.PlanActivity
import com.gmail_bssushant2003.journeycraft.Login.SendOTPActivity
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.Models.Preferences
import com.gmail_bssushant2003.journeycraft.Models.TripRecord
import com.gmail_bssushant2003.journeycraft.NavBar.PlanHistory
import com.gmail_bssushant2003.journeycraft.Notifications.LocationService
import com.gmail_bssushant2003.journeycraft.Questions.PreferenceActivity
import com.gmail_bssushant2003.journeycraft.databinding.ActivityDestinationListBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

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

        //stop the service
        binding.notification.setOnClickListener {
            val intent = Intent(this, LocationService::class.java)
            if (LocationService.isRunning) {
                stopService(intent)
                Toast.makeText(this, "Location tracking stopped", Toast.LENGTH_SHORT).show()
            } else {
                startService(intent)
                Toast.makeText(this, "Location tracking started", Toast.LENGTH_SHORT).show()
            }
        }

        //start the service
        val intent = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

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

                    val recordFile = getSharedPreferences("records", MODE_PRIVATE)
                    val editor = recordFile.edit()
                    editor.putBoolean("isUserValid", false)
                    editor.apply()

                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(this, SendOTPActivity::class.java))
                    finish()
                    true
                }
                R.id.navEditPreference -> {
                    startActivity(Intent(this, PreferenceActivity::class.java))
                }
                R.id.navActivePlan -> {
                    val recordFile = getSharedPreferences("records", MODE_PRIVATE)
                    val phoneNumber = recordFile.getString("phoneNumber", "")
                    lifecycleScope.launch {
                        val tripData = getLastTripData(phoneNumber!!)

                        if(tripData == null){
                            Toast.makeText(this@DestinationListActivity, "No active plan", Toast.LENGTH_LONG).show()
                        }
                        else{
                            val intent = Intent(this@DestinationListActivity, PlanActivity::class.java)
                            intent.putExtra("tripData", tripData)
                            startActivity(intent)
                        }
                    }
                }
                R.id.navHistory -> {
                    val recordFile = getSharedPreferences("records", MODE_PRIVATE)
                    val phoneNumber = recordFile.getString("phoneNumber", "")

                    lifecycleScope.launch {
                        val historyList = getHistory(phoneNumber!!);
                        if(historyList.isEmpty()){
                            Toast.makeText(this@DestinationListActivity, "Please plan a trip", Toast.LENGTH_LONG).show()
                        }
                        else{
                            val intent = Intent(this@DestinationListActivity, PlanHistory::class.java)
                            intent.putExtra("historyList", historyList)
                            startActivity(intent)
                        }
                    }
                }
            }
            drawerLayout.close()
            true
        }
    }

    suspend fun getLastTripData(phoneNumber: String): String? = suspendCoroutine { continuation ->
        val database = FirebaseDatabase.getInstance()
        val tripRef = database.getReference("trips").child(phoneNumber)

        tripRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Gaurav", snapshot.children.firstOrNull()?.getValue().toString())
                val lastData = snapshot.children.firstOrNull()?.getValue(TripRecord::class.java)
                continuation.resume(lastData?.response) // Return the data
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resume(null) // Return null if there's an error
            }
        })
    }

    private suspend fun getHistory(phoneNumber: String): ArrayList<TripRecord> = suspendCoroutine { continuation ->
        val database = FirebaseDatabase.getInstance()
        val tripRef = database.getReference("trips").child(phoneNumber)

        tripRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tripList = ArrayList<TripRecord>()

                for (child in snapshot.children) {
                    val trip = child.getValue(TripRecord::class.java)
                    if (trip != null) {
                        tripList.add(trip)
                    }
                }
                continuation.resume(tripList) // Return the list of trips
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resume(ArrayList()) // Return empty list on error
            }
        })
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