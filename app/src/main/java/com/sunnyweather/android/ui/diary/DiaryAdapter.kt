package com.sunnyweather.android.ui.diary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.database.MyDatabaseHelper
import com.sunnyweather.android.logic.model.Diary

class DiaryAdapter(private val diaryList: ArrayList<Diary>,private val activity: DiaryActivity) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val timeAndContentTextView: TextView = view.findViewById(R.id.timeAndContentTextView)
        val deleteDiaryBtn: ImageButton = view.findViewById(R.id.deleteDiaryBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diary_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = diaryList[position]
        holder.titleTextView.text = diary.title
        holder.timeAndContentTextView.text = "${diary.date} | ${diary.content}"
        holder.deleteDiaryBtn.setOnClickListener {
            activity.diaryViewModel.deleteDiary(diary.date)
            notifyItemRemoved(position)
            diaryList.removeAt(position)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DiaryEditActivity::class.java)
            intent.putExtra("newItem", 0)
            intent.putExtra("diary", diaryList[position])
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = diaryList.size

    fun refreshDiaryList(replaceDiaryList: ArrayList<Diary>) {
        diaryList.apply {
            clear()
            addAll(replaceDiaryList)
        }
        notifyDataSetChanged()
    }

}