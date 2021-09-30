package com.sunnyweather.android.ui.forum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Post
import com.sunnyweather.android.logic.model.User
import com.sunnyweather.android.ui.login.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

class PostEditActivity : AppCompatActivity() {

    private val postViewModel: PostViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var publishBtn: Button
    private lateinit var postEditToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var postTitleText: EditText
    private lateinit var postContentText: EditText

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_edit)
        val upn = intent.getStringExtra("upn")
        if (upn != null) {
            user = userViewModel.searchUserByPhoneNumber(upn)!!
        }
        publishBtn = findViewById(R.id.publishBtn)
        postEditToolbar = findViewById(R.id.postEditToolbar)
        postTitleText = findViewById(R.id.postTitleText)
        postContentText = findViewById(R.id.postContentText)

        setSupportActionBar(postEditToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
            it.title = "发布新帖"
        }

        publishBtn.setOnClickListener {
            val title = postTitleText.text.toString()
            val content = postContentText.text.toString()
            if (title != "" && content != "") {
                val simpleDateFormat = SimpleDateFormat("yyyy年M月d日 H:mm", Locale.getDefault())
                val time = simpleDateFormat.format(System.currentTimeMillis())
                publishNewPost(title, content, time)
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "请输入标题和内容！", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun publishNewPost(title: String, content: String, time: String) {
        var key: Int
        do {
            key = (0..9999).random()
        } while (postViewModel.isKeyExisted(key))
        val post = Post(key, user.phoneNumber, title, content, time)
        postViewModel.savePost(post)
        userViewModel.addPublished(user.phoneNumber, key)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            finish()
            true
        } else -> {
            false
        }
    }

}