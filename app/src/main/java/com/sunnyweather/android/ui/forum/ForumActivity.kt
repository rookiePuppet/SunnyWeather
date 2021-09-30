package com.sunnyweather.android.ui.forum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.sunnyweather.android.R
import android.widget.Button
import com.sunnyweather.android.ui.login.UserViewModel
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

class ForumActivity : AppCompatActivity() {

    private val postViewModel: PostViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var userDrawer: DrawerLayout
    private lateinit var forumToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var closeForumMBtn: MenuItem
    private lateinit var publishFAB: FloatingActionButton
    private lateinit var searchPostEdit: EditText
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var forumAdapter: ForumAdapter

    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)
        val userPhoneNumber = intent.getStringExtra("userPhoneNumber")
        if (userPhoneNumber != null) {
            userViewModel.loginUser = userViewModel.searchUserByPhoneNumber(userPhoneNumber)!!
            Log.d("SunnyWeather", userViewModel.loginUser.username)
        }

        userDrawer = findViewById(R.id.userDrawer)
        forumToolbar = findViewById(R.id.postDetailToolbar)
        publishFAB = findViewById(R.id.publishFAB)
        searchPostEdit = findViewById(R.id.searchPostEdit)
        swipeRefresh = findViewById(R.id.swipeRefreshForum)
        postRecyclerView = findViewById(R.id.postRecyclerView)
        navView = findViewById(R.id.navView)

        setSupportActionBar(forumToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu_24)
            it.title = "论坛"
        }

        publishFAB.setOnClickListener {
            val intent = Intent(this, PostEditActivity::class.java)
            intent.putExtra("upn", userViewModel.loginUser.phoneNumber)
            startActivity(intent)
        }

        searchPostEdit.addTextChangedListener {
            val content = searchPostEdit.text.toString()
            if (content != "") {
                val result = postViewModel.searchPostByTitle(content)
                forumAdapter.refreshPostList(result)
            } else {
                forumAdapter.refreshPostList(postViewModel.getSavedPost())
            }
        }

        navView.setNavigationItemSelectedListener {
            val intent = Intent(this, SpecificPostActivity::class.java)
            intent.putExtra("userPN", userViewModel.loginUser.phoneNumber)
            when (it.itemId) {
                R.id.navPublishedPost -> {
                    intent.putExtra("specific_type", 0)
                    startActivity(intent)
                }
                R.id.navLikedPost -> {
                    intent.putExtra("specific_type", 1)
                    startActivity(intent)
                }
                R.id.navStaredPost -> {
                    intent.putExtra("specific_type", 2)
                    startActivity(intent)
                }
                R.id.navSettings -> {

                }
                R.id.navExitLogin -> {
                    val sp = getSharedPreferences("remember_account", MODE_PRIVATE)
                    sp.edit().clear().apply()
                    finish()
                }
            }
            userDrawer.closeDrawers()
            true
        }

        val nv = navView.inflateHeaderView(R.layout.nav_header)
        val navUserImg: ImageView = nv.findViewById(R.id.navUserImg)
        val navUsername: TextView = nv.findViewById(R.id.navUsername)
        val navPhoneNumber: TextView = nv.findViewById(R.id.navPhoneNumber)
        navUserImg.setImageBitmap(userViewModel.loginUser.image)
        navUsername.text = userViewModel.loginUser.username
        navPhoneNumber.text = userViewModel.loginUser.phoneNumber
        swipeRefresh.setOnRefreshListener {
            forumAdapter.refreshPostList(postViewModel.getSavedPost())
            swipeRefresh.isRefreshing = false
        }

        forumAdapter = ForumAdapter(postViewModel.getSavedPost(), userViewModel)
        postRecyclerView.adapter = forumAdapter
        val layoutManager = LinearLayoutManager(this)
        postRecyclerView.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.forum_toolbar_menu, menu)
        closeForumMBtn = menu.findItem(R.id.closeForumBtn)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                userDrawer.openDrawer(GravityCompat.START)
            }
            R.id.closeForumBtn -> {
                finish()
            }
            else -> {

            }

        }
        return true
    }

    override fun onResume() {
        super.onResume()
        forumAdapter.refreshPostList(postViewModel.getSavedPost())
    }

}