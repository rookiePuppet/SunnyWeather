package com.sunnyweather.android.ui.forum

import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.dao.PostDao
import com.sunnyweather.android.logic.model.Post

class PostViewModel: ViewModel() {

    fun savePost(post: Post) = Repository.savePost(post)

    fun searchPostByKey(key: Int): Post? = Repository.searchPostByKey(key)

    fun searchPostByTitle(title: String) = Repository.searchPostByTitle(title)

    fun deletePostByKey(key: Int) = Repository.deletePostByKey(key)

    fun getSavedPost() = Repository.getSavedPost()

    fun isKeyExisted(key: Int) = Repository.isKeyExisted(key)

}