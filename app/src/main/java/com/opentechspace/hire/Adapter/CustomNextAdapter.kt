package com.opentechspace.hire.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.opentechspace.hire.Model.ItemNextModel
import com.opentechspace.hire.R

class CustomNextAdapter(private val context: Context, val list : List<ItemNextModel>,
        val onAddData: OnAddData)
    : RecyclerView.Adapter<CustomNextAdapter.myViewHolder>()
{

    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val setTitle = itemView.findViewById<TextView>(R.id.service_title)
        val setImage = itemView.findViewById<ImageView>(R.id.service_image)
        val setPrice = itemView.findViewById<TextView>(R.id.price)
        val setTime = itemView.findViewById<TextView>(R.id.time)
        val setDetails = itemView.findViewById<TextView>(R.id.discription)
        val add = itemView.findViewById<TextView>(R.id.add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.customservice,parent,false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val result = list[position]
        holder.setTitle.text = result.Title
        holder.setTime.text = result.Time
        holder.setDetails.text = result.Details
        holder.setPrice.text = result.Price
        Glide.with(context)
            .load(result.ImageUrl)
            .into(holder.setImage)
        holder.add.setOnClickListener {
            onAddData.onAddData(result.Title!!,result.Price!!,result.ImageUrl!!)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
interface OnAddData
{
    fun onAddData(title : String,price : String,imageUrl : String)
}