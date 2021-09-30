package com.sunnyweather.android.ui.diary

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.database.MyDatabaseHelper
import com.sunnyweather.android.logic.model.Diary
import java.text.SimpleDateFormat
import java.util.*

class DiaryEditActivity : AppCompatActivity() {

    private val diaryViewModel: DiaryViewModel by viewModels()

    private val simpleDateFormat = SimpleDateFormat("yyyy年M月d日 H:m", Locale.getDefault())

    private var tempDiary: Diary? = null

    private lateinit var updateTimeText: TextView
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_edit)
        updateTimeText = findViewById(R.id.updateTimeText)
        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        if (intent.getIntExtra("newItem", 1) == 1) {
            val emptyDiary = Diary(
                simpleDateFormat.format(Date(System.currentTimeMillis())),
                "未命名标题", ""
            )
            loadData(emptyDiary)
        } else {
            val diary = intent.getParcelableExtra<Diary>("diary")
            tempDiary = diary
            loadData(diary)
        }
    }

    private fun loadData(diary: Diary?) {
        titleEditText.setText(diary?.title)
        updateTimeText.text = diary?.date
        contentEditText.setText(diary?.content)
    }

    private fun showMyDialog(mode: Int) {
        /*
        * mode = 0 保存新日记
        * mode = 1 更新日记
        * */
        MaterialAlertDialogBuilder(this)
            .setTitle("即将退出")
            .setMessage("是否保存日记？")
            .setPositiveButton("是") { dialog, which ->
                val diary = Diary(
                    updateTimeText.text.toString(),
                    titleEditText.text.toString(),
                    contentEditText.text.toString()
                )
                if (mode == 0) {
                    diaryViewModel.saveDiary(diary)
                } else if(mode == 1) {
                    diaryViewModel.updateDiary(diary)
                }
                finish()
            }
            .setNegativeButton("否") { dialog, which -> finish() }
            .setNeutralButton("取消") { dialog, which -> }
            .show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (tempDiary == null) {
                if (contentEditText.text.isEmpty()) {
                    finish()
                } else {
                    showMyDialog(0)
                }
            } else {
                val updated = (titleEditText.text.toString() != tempDiary?.title) ||
                        (contentEditText.text.toString() != tempDiary?.content)
                if (updated) {
                    showMyDialog(1)
                } else {
                    finish()
                }
            }
        }
        return true
    }

}