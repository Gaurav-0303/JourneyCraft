package com.gmail_bssushant2003.journeycraft.Models

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
import java.io.Serializable

data class Items (
    val image : Int,
    val title : String,
    val location : String
) : Serializable
