package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.DetailedPlan.PlanActivity
import com.gmail_bssushant2003.journeycraft.Models.DetailedTripRecord
import com.gmail_bssushant2003.journeycraft.databinding.IndividualHistoryItemBinding

class PlanHistoryAdapter(var context : Context, var detailedTripRecordList : ArrayList<DetailedTripRecord>) : RecyclerView.Adapter<PlanHistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding : IndividualHistoryItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return detailedTripRecordList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.dateTextView.text = detailedTripRecordList[position].date
        holder.binding.timeTextView.text = detailedTripRecordList[position].time

        holder.binding.historyCard.setOnClickListener {
            val intent = Intent(context, PlanActivity::class.java)
            intent.putExtra("tripData", detailedTripRecordList[position].response)
            holder.itemView.context.startActivity(intent)
        }
    }
}