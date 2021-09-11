package com.sunnyweather.android.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place

class PlaceSavedAdapter(val placeList: ArrayList<Place>, private val activity: PlaceManageActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class ViewHolderForManage(view: View) : RecyclerView.ViewHolder(view) {
        val savedPlaceName: TextView = view.findViewById(R.id.savedPlaceName)
        val deleteBtn: Button = view.findViewById(R.id.deleteBtn)
        val sortImage: ImageView = view.findViewById(R.id.sortImage)
    }

    inner class ViewHolderForEdit(view: View) : RecyclerView.ViewHolder(view) {
        val savedPlaceName: TextView = view.findViewById(R.id.savedPlaceName)
        val deleteBtn: Button = view.findViewById(R.id.deleteBtn)
        val sortImage: ImageView = view.findViewById(R.id.sortImage)
    }

    override fun getItemViewType(position: Int): Int =
        if (activity.statusViewModel.statusLiveData.value == StatusViewModel.STATUS_MANAGE) {
            0
        } else {
            1
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_manage_item, parent, false)
        return if (viewType == 0) {
            ViewHolderForManage(view)
        } else {
            ViewHolderForEdit(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderForManage -> {
                holder.savedPlaceName.text = placeList[position].name
                holder.deleteBtn.visibility = View.GONE
                holder.sortImage.visibility = View.GONE
                holder.itemView.setOnLongClickListener {
                    activity.statusViewModel.transformStatus()
                    true
                }
            }
            is ViewHolderForEdit -> {
                holder.savedPlaceName.text = placeList[position].name
                holder.deleteBtn.visibility = View.VISIBLE
                holder.sortImage.visibility = View.VISIBLE
                holder.deleteBtn.setOnClickListener {
                    Toast.makeText(activity, "你删除了${placeList[position].name}", Toast.LENGTH_SHORT).show()
                    activity.placeSavedViewModel.placeList.value?.removeAt(position)
                    notifyDataSetChanged()
                }
                holder.itemView.setOnLongClickListener {
                    activity.statusViewModel.transformStatus()
                    true
                }
            }
        }

    }

    override fun getItemCount(): Int = placeList.size

}





