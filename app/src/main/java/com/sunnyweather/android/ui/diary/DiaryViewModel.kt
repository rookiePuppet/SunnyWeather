package com.sunnyweather.android.ui.diary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.dao.DiaryDao
import com.sunnyweather.android.logic.model.Diary

class DiaryViewModel: ViewModel() {

    fun saveDiary(diary: Diary) = Repository.saveDiary(diary)

    fun searchDiary(titleText: String) = Repository.searchDiary(titleText)

    fun updateDiary(diary: Diary) = Repository.updateDiary(diary)

    fun deleteDiary(date: String) = Repository.deleteDiary(date)

    fun getSavedDiary(): ArrayList<Diary> = Repository.getSavedDiary()

}