package com.gsixacademy.android.atmfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gsixacademy.android.atmfinder.R
import com.gsixacademy.android.atmfinder.data.AtmsData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_atm.view.*

class AtmListAdapter ( var atmList : ArrayList<AtmsData>, val atmsAdapterClickEvent : (AtmsAdapterClickEvent.onAtmsAdapterItemClicked) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder ( LayoutInflater.from(parent.context).inflate(R.layout.activity_atm,parent,false))
    }

    override fun getItemCount(): Int {
        return atmList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myViewHolder = holder as MyViewHolder
        myViewHolder.bindData(atmList[position], position)
    }

    inner class MyViewHolder (view : View) : RecyclerView.ViewHolder(view){
        fun bindData (itemModel : AtmsData, position: Int){
            itemView.text_view_bank_name.text = itemModel.bank
            itemView.text_view_address.text = itemModel.address
            itemView.text_view_city.text = itemModel.city
            Picasso.get().load(itemModel.logo).centerCrop().fit().into(itemView.image_view_logo)

        }
    }
}