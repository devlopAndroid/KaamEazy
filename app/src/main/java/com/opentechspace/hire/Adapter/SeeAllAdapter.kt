package com.opentechspace.hire.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.opentechspace.hire.Model.ItemModel
import com.opentechspace.hire.Model.SeeAllItemModel
import com.opentechspace.hire.R

class SeeAllAdapter(private val context: Context, val list : List<SeeAllItemModel>,
                    val onItemClickListener: OnItemSeeAllClickListener)
    : RecyclerView.Adapter<SeeAllAdapter.myViewHolder>()
{
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
            onItemClickListener.onSeeAllClickItem(result.Title)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
interface OnItemSeeAllClickListener{
    fun onSeeAllClickItem(title : String?)
}
