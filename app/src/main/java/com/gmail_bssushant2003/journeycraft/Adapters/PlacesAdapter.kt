package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.databinding.IndividualItemPlanBinding

class PlacesAdapter(private val context: Context, private val placesList: ArrayList<String>, private val timeList: ArrayList<String>) :
    RecyclerView.Adapter<PlacesAdapter.MyViewHolder>() {

    private lateinit var MyListener: MyAdapter.OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: MyAdapter.OnItemClickListener){
        MyListener = listener
    }

    inner class MyViewHolder(val binding: IndividualItemPlanBinding,  listener: MyAdapter.OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualItemPlanBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding, MyListener)
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.namePlace.text = placesList[position]
        val x = position+1
        holder.binding.numberPlace.text = x.toString()

        if(timeList.size > 0) holder.binding.timePlace.text = timeList[position]
        else holder.binding.timePlace.text = "10:10"

        if(placesList[position].length > 16){
            val layoutParams = holder.binding.lineStraight.layoutParams
            layoutParams.height =  200// Set your desired height in pixels
            holder.binding.lineStraight.layoutParams = layoutParams
        }

        if(position == placesList.size - 1){
//            holder.binding.lineStraight.visibility = View.GONE
            holder.binding.lineStraight.background = ColorDrawable(Color.parseColor("#ffffff"))
        }
    }


}