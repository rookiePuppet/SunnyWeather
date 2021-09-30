package com.sunnyweather.android.ui.login

import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.dao.UserDao
import com.sunnyweather.android.logic.model.User

class UserViewModel : ViewModel() {

    lateinit var loginUser: User

    fun saveUser(user: User) = Repository.saveUser(user)

    fun deleteUserByPhoneNumber(pn: String) = Repository.deleteUserByPhoneNumber(pn)

    fun addPublished(userPN: String, postKey: Int) = Repository.addPublished(userPN, postKey)

    fun removePublished(userPN: String, postKey: Int) = Repository.removePublished(userPN, postKey)

    fun searchUserByPhoneNumber(pn: String) = Repository.searchUserByPhoneNumber(pn)

    fun addLiked(userPN: String, postKey: Int) = Repository.addLiked(userPN, postKey)

    fun addStared(userPN: String, postKey: Int) = Repository.addStared(userPN, postKey)

    fun removeLiked(userPN: String, postKey: Int) = Repository.removeLiked(userPN, postKey)

    fun removeStared(userPN: String, postKey: Int) = Repository.removeStared(userPN, postKey)

}