package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.Fragments.GuideDetailsDialogFragment
import com.gmail_bssushant2003.journeycraft.Fragments.RestaurantDetailsDialogFragment
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.Models.Restaurant
import com.gmail_bssushant2003.journeycraft.databinding.IndividualGuidesItemBinding
import com.gmail_bssushant2003.journeycraft.databinding.IndividualRestaurantsItemBinding

class RestaurantsAdapter(var context : Context, var restaurantsList : ArrayList<Restaurant>,   private val fragmentManager: FragmentManager) : RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding : IndividualRestaurantsItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualRestaurantsItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurantsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.restName.text = restaurantsList[position].name
        holder.binding.restMobileNumber.text = restaurantsList[position].phoneNo

//        holder.binding.guideCard.setOnClickListener {
//            val intent = Intent(context, PlanActivity::class.java)
//            intent.putExtra("tripData", detailedTripRecordList[position].response)
//            holder.itemView.context.startActivity(intent)

//        }

        holder.binding.expandCard.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL).apply {
//                data = "tel:${restaurantsList[position].phoneNo}".toUri()
//            }
//            holder.itemView.context.startActivity(intent)


//            val locationUrl = restaurantsList[position].locationLink
//
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl))
//            intent.setPackage("com.google.android.apps.maps") // Try to open in Google Maps
//
//            try {
//                context.startActivity(intent)
//            } catch (e: ActivityNotFoundException) {
//                // Open in browser if Google Maps is not available
//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl))
//                context.startActivity(browserIntent)
//            }


            val dialog = RestaurantDetailsDialogFragment(restaurantsList[position])  // Pass Guide object
            dialog.show(fragmentManager, "RestaurantDetailsDialog")

        }
    }
}