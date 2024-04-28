package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.databinding.IndividualLocationBinding

class MyAdapter(private val context: Context, val itemsList: ArrayList<Items>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private lateinit var MyListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener){
        MyListener = listener
    }

    inner class MyViewHolder(val binding: IndividualLocationBinding,  listener: OnItemClickListener): RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IndividualLocationBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding, MyListener)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.title.text = itemsList[position].title
        holder.binding.location.text = itemsList[position].location
        holder.binding.image.setImageResource(itemsList[position].image)
    }

}
