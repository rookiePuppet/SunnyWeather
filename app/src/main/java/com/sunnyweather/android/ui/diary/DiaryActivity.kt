package com.sunnyweather.android.ui.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunnyweather.android.R

class DiaryActivity : AppCompatActivity() {

    val diaryViewModel: DiaryViewModel by viewModels()

    private lateinit var diaryAdapter: DiaryAdapter

    private lateinit var diaryRecyclerView: RecyclerView
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var addDiaryFAB: FloatingActionButton
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        diaryRecyclerView = findViewById(R.id.diaryRecyclerView)
        bottomAppBar = findViewById(R.id.bottomAppBar)
        addDiaryFAB = findViewById(R.id.addDiaryFAB)
        searchEditText = findViewById(R.id.searchEditText)

        addDiaryFAB.setOnClickListener {
            val intent = Intent(this, DiaryEditActivity::class.java)
            intent.putExtra("newItem", 1)
            startActivity(intent)
        }
        bottomAppBar.setNavigationOnClickListener {
            Toast.makeText(this, "you clicked bottomAppBar.", Toast.LENGTH_SHORT).show()
        }
        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.search -> {
                    if (searchEditText.visibility == View.GONE) {
                        searchEditText.visibility = View.VISIBLE
                    }
                    true
                }
                else -> false
            }
        }
        searchEditText.addTextChangedListener { text: Editable? ->
            val content = text.toString()
            if (content != "") {
                val result = diaryViewModel.searchDiary(content)
                diaryAdapter.refreshDiaryList(result)
            } else {
                diaryAdapter.refreshDiaryList(diaryViewModel.getSavedDiary())
                searchEditText.clearFocus()
            }
        }

        diaryAdapter = DiaryAdapter(diaryViewModel.getSavedDiary(),this)
        diaryRecyclerView.adapter = diaryAdapter
        val layoutManager = LinearLayoutManager(this)
        diaryRecyclerView.layoutManager = layoutManager

    }

    override fun onResume() {
        super.onResume()
        diaryAdapter.refreshDiaryList(diaryViewModel.getSavedDiary())
    }


}