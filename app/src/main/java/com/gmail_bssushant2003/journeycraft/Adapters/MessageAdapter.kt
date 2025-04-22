package com.gmail_bssushant2003.journeycraft.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail_bssushant2003.journeycraft.Models.Message
import com.gmail_bssushant2003.journeycraft.databinding.ChatItemBinding

class MessageAdapter(private val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message : Message = messageList[position]
        if(message.sendBy == "me"){
            holder.binding.leftChatView.visibility = View.GONE
            holder.binding.rightChatView.visibility = View.VISIBLE
            holder.binding.rightChatText.text = message.message
        }
        else{
            holder.binding.rightChatView.visibility = View.GONE
            holder.binding.leftChatView.visibility = View.VISIBLE
            holder.binding.leftChatText.text = message.message
        }
    }

}