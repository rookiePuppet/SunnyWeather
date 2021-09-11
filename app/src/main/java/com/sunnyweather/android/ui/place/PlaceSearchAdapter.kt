package com.sunnyweather.android.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place

class PlaceSearchAdapter(private val fragment: PlaceSearchFragment, private val placeList: ArrayList<Place>) :
    RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceSearchAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.place_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceSearchAdapter.ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            val place = placeList[position]
            val activity = fragment.activity
            fragment.placeSearchViewModel.savePlace(place)
            fragment.placeSavedViewModel.placeList.value?.add(place)
            val drawerLayout: DrawerLayout = activity!!.findViewById(R.id.drawerLayout)
            drawerLayout.closeDrawer(GravityCompat.START)
            fragment.setFragmentResult("requestKey", bundleOf("hadAddedPlace" to 1))
        }
    }

    override fun getItemCount(): Int = placeList.size

}