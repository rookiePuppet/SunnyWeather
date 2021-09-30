package com.sunnyweather.android.ui.forum

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.logic.model.Post
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.login.UserViewModel

class ForumAdapter(private val postList: ArrayList<Post>, private val userViewModel: UserViewModel) :
    RecyclerView.Adapter<ForumAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterName: TextView = view.findViewById(R.id.posterName)
        val posterImg: ImageView = view.findViewById(R.id.posterImg)
        val titlePostText: TextView = view.findViewById(R.id.titlePostText)
        val contentPostText: TextView = view.findViewById(R.id.contentPostText)
        val timePostText: TextView = view.findViewById(R.id.timePostText)
        val starBtn: ImageButton = view.findViewById(R.id.starBtn)
        val likeBtn: ImageButton = view.findViewById(R.id.likeBtn)
        val commentBtn: ImageButton = view.findViewById(R.id.commentBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forum_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForumAdapter.ViewHolder, position: Int) {
        val post = postList[position]
        val poster = userViewModel.searchUserByPhoneNumber(post.posterPN)
        holder.posterName.text = poster?.username
        holder.posterImg.setImageBitmap(poster?.image)
        holder.titlePostText.text = post.title
        holder.contentPostText.text = post.content
        holder.timePostText.text = post.time
        val loginUser = userViewModel.loginUser
        val likedList = loginUser.likedList
        val staredList = loginUser.staredList
        var liked = likedList.contains(post.key.toString())
        var stared = staredList.contains(post.key.toString())
        if (liked) {
            holder.likeBtn.setImageResource(R.drawable.ic_thumb_up_24_activate)
        }
        staredList.contains(post.key.toString())
        if (stared) {
            holder.starBtn.setImageResource(R.drawable.ic_star_24_activate)
        }
        holder.likeBtn.setOnClickListener {
            liked = if (liked) {
                holder.likeBtn.setImageResource(R.drawable.ic_thumb_up_24_normal)
                userViewModel.removeLiked(loginUser.phoneNumber, post.key)
                false
            } else {
                holder.likeBtn.setImageResource(R.drawable.ic_thumb_up_24_activate)
                userViewModel.addLiked(loginUser.phoneNumber, post.key)
                true
            }
        }
        holder.starBtn.setOnClickListener {
            stared = if (stared) {
                holder.starBtn.setImageResource(R.drawable.ic_star_24_normal)
                userViewModel.removeStared(loginUser.phoneNumber, post.key)
                false
            } else {
                holder.starBtn.setImageResource(R.drawable.ic_star_24_activate)
                userViewModel.addStared(loginUser.phoneNumber, post.key)
                true
            }
        }
        holder.commentBtn.setOnClickListener {
            Toast.makeText(it.context, "评论暂未开放", Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, PostDetailActivity::class.java)
            intent.putExtra("post", post)
            if (userViewModel.loginUser.phoneNumber == poster?.phoneNumber) {
                intent.putExtra("isLoginUser", true)
            } else {
                intent.putExtra("isLoginUser", false)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = postList.size

    fun refreshPostList(newPostList: ArrayList<Post>) {
        postList.apply {
            clear()
            addAll(newPostList)
        }
        notifyDataSetChanged()
    }

}