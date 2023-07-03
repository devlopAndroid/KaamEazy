package com.opentechspace.hire.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.opentechspace.hire.Model.SlidingImageModel
import com.opentechspace.hire.R

class SlidingImageAdapter( private val context : Context,
    val list : List<SlidingImageModel> , val viewPager : ViewPager2) :
            RecyclerView.Adapter<SlidingImageAdapter.MyViewHolder>(){

    private var newList : ArrayList<SlidingImageModel> = ArrayList<SlidingImageModel>()

    init {
        newList.addAll(list)
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val imageUrl = itemView.findViewById<ImageView>(R.id.sliding_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.custom,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val data = newList[position]
       Glide.with(context)
           .load(data.ImageUrl)
           .into(holder.imageUrl)
       if (position == newList.size-1)
       {
           viewPager.post(runnable)
       }
    }

    override fun getItemCount(): Int {
        return newList.size
    }
    private val runnable = Runnable {
        newList.addAll(newList)
        notifyDataSetChanged()
    }
}