package com.sunnyweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel(private var placeSavedAdapter: PlaceSavedAdapter) : ViewModel() {

    companion object {
        const val STATUS_MANAGE = 0
        const val STATUS_EDIT = 1
    }

    val statusLiveData: LiveData<Int>
        get() = _statusLiveData

    private val _statusLiveData = MutableLiveData(STATUS_MANAGE)

    fun transformStatus() {
        _statusLiveData.value = if (_statusLiveData.value == STATUS_MANAGE) {
            STATUS_EDIT
        } else {
            STATUS_MANAGE
        }
        placeSavedAdapter.notifyDataSetChanged()
    }

}