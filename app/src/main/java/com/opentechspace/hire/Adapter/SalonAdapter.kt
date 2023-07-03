package com.opentechspace.hire.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.opentechspace.hire.Model.ItemCleaningModel
import com.opentechspace.hire.Model.ItemModel
import com.opentechspace.hire.Model.ItemPlumberModel
import com.opentechspace.hire.Model.ItemSalonModel
import com.opentechspace.hire.R

class SalonAdapter(private val context: Context, val list : List<ItemSalonModel>,
                     val onItemClickListener: OnSalonItemClickListener)
    : RecyclerView.Adapter<SalonAdapter.myViewHolder>()
{
    private val itemlimit : Int = 5
    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val setTitle = itemView.findViewById<TextView>(R.id.setTitle1)
        val setImage = itemView.findViewById<ImageView>(R.id.setImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.customimage,parent,false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val result = list[position]
        holder.setTitle.text = result.Title
        Glide.with(context)
            .load(result.ImageUrl)
            .into(holder.setImage)
        holder.itemView.setOnClickListener {
            onItemClickListener.onSalonClickItem(result.Title!!)
        }
    }

    override fun getItemCount(): Int {
        return if(list.size>itemlimit) {
            itemlimit
        } else {
            list.size
        }
    }

}
interface OnSalonItemClickListener{
    fun onSalonClickItem(title : String)
}
