package com.sunnyweather.android.ui.forum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Post
import com.sunnyweather.android.ui.login.UserViewModel
import kotlin.properties.Delegates

class PostDetailActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    private lateinit var postDetailToolbar: Toolbar
    private lateinit var detailPosterImg: ImageView
    private lateinit var detailPosterName: TextView
    private lateinit var detailTitlePostText: TextView
    private lateinit var detailContentPostText: TextView
    private lateinit var detailTimePostText: TextView
    private lateinit var deletePostBtn: Button

    private lateinit var post: Post
    private var isLoginUser by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        postDetailToolbar = findViewById(R.id.postDetailToolbar)
        detailPosterImg = findViewById(R.id.detailPotsterImg)
        detailPosterName = findViewById(R.id.detailPosterName)
        detailTimePostText = findViewById(R.id.detailTimePostText)
        detailTitlePostText = findViewById(R.id.specificTitlePostText)
        detailContentPostText = findViewById(R.id.specificContentPostText)
        deletePostBtn = findViewById(R.id.deletePostBtn)

        setSupportActionBar(postDetailToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
            it.title = "详情"
        }

        post = intent.getParcelableExtra("post")!!
        isLoginUser = intent.getBooleanExtra("isLoginUser", false)
        if (isLoginUser) {
            deletePostBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    postViewModel.deletePostByKey(post.key)
                    userViewModel.removePublished(post.posterPN, post.key)
                    finish()
                }
            }
        }
        val poster = userViewModel.searchUserByPhoneNumber(post.posterPN)
        detailPosterImg.setImageBitmap(poster?.image)
        detailPosterName.text = poster?.username
        detailTimePostText.text = post.time
        detailTitlePostText.text = post.title
        detailContentPostText.text = post.content

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

}