package com.gsixacademy.android.atmfinder.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gsixacademy.android.atmfinder.R


class CityAdapter (
    val context : Activity, val data : Array<String>) : BaseAdapter(){

    private var inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val vh : ItemHolder

        if (convertView == null){
            view = inflater.inflate(R.layout.custom_dropdown_menu,parent,false)
            vh = ItemHolder (view)
            view?.tag = vh
        } else {

            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.cityname.text = data.get(position)
        return view
    }

    override fun getItem(position: Int): String? {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    private class ItemHolder (row : View?){
        val cityname : TextView

        init {
            cityname = row?.findViewById(R.id.pick_a_city_dropdown) as TextView
        }
    }

}