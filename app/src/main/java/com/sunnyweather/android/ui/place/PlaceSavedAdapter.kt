package com.sunnyweather.android.ui.place

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

class PlaceSavedAdapter(private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceSavedAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val savedPlaceName: TextView = view.findViewById(R.id.savedPlaceName)
        val deleteBtn: Button = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceSavedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_saved_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceSavedAdapter.ViewHolder, position: Int) {
        holder.savedPlaceName.text = placeList[position].name
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = placeList.size

}



