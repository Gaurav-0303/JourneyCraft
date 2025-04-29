package com.gmail_bssushant2003.journeycraft.Fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.DialogGuideDetailsBinding
import androidx.core.net.toUri
import androidx.transition.Visibility
import com.gmail_bssushant2003.journeycraft.Models.Restaurant
import com.gmail_bssushant2003.journeycraft.databinding.DialogRestaurantDetailsBinding

class RestaurantDetailsDialogFragment(private val restaurant: Restaurant) : DialogFragment() {

    private var _binding: DialogRestaurantDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            _binding = DialogRestaurantDetailsBinding.inflate(it.layoutInflater)

//            binding.root.setBackgroundResource(android.R.color.transparent)

            binding.restaurantName.text = restaurant.name ?: "XYZ"
            binding.rating.text = restaurant.rating.toString()
    //            binding.guideLanguages.text = "Languages: ${it.language ?: "Not specified"}"
            binding.desciption.text = restaurant.description
            binding.restaurantNumber.text = restaurant.phoneNo
    //            binding.guideLocation.text = "Location: ${it.latitude}, ${it.longitude}"

            binding.averageCost.text = restaurant.averageCost.toString()


            if (restaurant.foodType == Restaurant.FoodType.BOTH) {  // Assuming 'both' means unspecified

            }
            else if (restaurant.foodType == Restaurant.FoodType.VEG) {
                binding.nonvegIndicator.visibility = View.GONE
            }
            else if (restaurant.foodType == Restaurant.FoodType.NON_VEG) {
                binding.nonvegIndicator.visibility = View.GONE
                binding.vegIndicator.setImageResource(R.drawable.ic_nonveg)
            }

            binding.directionIcon.setOnClickListener {
//                val callIntent = Intent(Intent.ACTION_DIAL).apply {
//                    data = "tel:${restaurant.phoneNo}".toUri()
//                }
//                startActivity(callIntent)

                val mapIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = restaurant.locationLink?.toUri()
                }
                startActivity(mapIntent)
            }

            // Build the AlertDialog
            AlertDialog.Builder(it,R.style.TransparentDialog)
                .setView(binding.root)
                .create()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }
}
