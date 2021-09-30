package com.sunnyweather.android.ui.forum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Post
import com.sunnyweather.android.logic.model.User
import com.sunnyweather.android.toList
import com.sunnyweather.android.ui.login.UserViewModel

class SpecificPostActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    private lateinit var specificPostToolbar: Toolbar
    private lateinit var specificPostRecyclerView: RecyclerView
    private lateinit var specificPostAdapter: SpecificPostAdapter

    private val postList =  ArrayList<Post>()
    private var toolbarTitle: String = ""

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_post)
        user = userViewModel.searchUserByPhoneNumber(intent.getStringExtra("userPN")!!)!!
        userViewModel.loginUser = user
        loadData(intent.getIntExtra("specific_type", 0))
        specificPostToolbar = findViewById(R.id.specificPostToolbar)
        specificPostRecyclerView = findViewById(R.id.specificPostRecyclerView)
        setSupportActionBar(specificPostToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
            it.title = toolbarTitle
        }

        specificPostAdapter = SpecificPostAdapter(postList, userViewModel)
        specificPostRecyclerView.adapter = specificPostAdapter
        val layoutManager = LinearLayoutManager(this)
        specificPostRecyclerView.layoutManager = layoutManager
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun loadData(type: Int) {
        when(type) {
            0 -> { //published
                val keyList = user.publishedList.toList()
                for (postKey in keyList) {
                    val post = postViewModel.searchPostByKey(postKey)
                    if (post != null) postList.add(post)
                }
                toolbarTitle = "我发布的帖子"
            }
            1 -> { //liked
                val keyList = user.likedList.toList()
                for (postKey in keyList) {
                    val post = postViewModel.searchPostByKey(postKey)
                    if (post != null) postList.add(post)
                }
                toolbarTitle = "我点赞的帖子"
            }
            2 -> { //stared
                val keyList = user.staredList.toList()
                for (postKey in keyList) {
                    val post = postViewModel.searchPostByKey(postKey)
                    if (post != null) postList.add(post)
                }
                toolbarTitle = "我收藏的帖子"
            }
        }
    }

}