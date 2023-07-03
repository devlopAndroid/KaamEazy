package com.opentechspace.hire.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.opentechspace.hire.Model.CheckOutItemModel
import com.opentechspace.hire.Model.ItemNextModel
import com.opentechspace.hire.R

class CustomCheckOUtAdapter(private val context: Context, val list : List<CheckOutItemModel>,
       val  onDeleteListener: OnDeleteListener)
    : RecyclerView.Adapter<CustomCheckOUtAdapter.myViewHolder>()
{
    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val setTitle = itemView.findViewById<TextView>(R.id.checkout_Title)
        val setImage = itemView.findViewById<ImageView>(R.id.checkout_image)
        val setPrice = itemView.findViewById<TextView>(R.id.checkout_Price1)
        val remove = itemView.findViewById<TextView>(R.id.checkOut_remove)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.customcheckout,parent,false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val result = list[position]
        holder.setTitle.text = result.Name
        holder.setPrice.text = result.Price
        Glide.with(context)
            .load(result.ImageUrl)
            .into(holder.setImage)
        holder.remove.setOnClickListener {
            onDeleteListener.onDelete(result)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
interface OnDeleteListener{
    fun onDelete(checkOutItemModel: CheckOutItemModel)
}