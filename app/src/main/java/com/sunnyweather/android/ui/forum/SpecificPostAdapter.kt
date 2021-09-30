package com.sunnyweather.android.ui.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.logic.model.Post
import com.sunnyweather.android.R
import android.widget.ImageView
import android.widget.TextView
import com.sunnyweather.android.ui.login.UserViewModel
import android.content.Intent

class SpecificPostAdapter(private val postList: ArrayList<Post>, private val userViewModel: UserViewModel) :
    RecyclerView.Adapter<SpecificPostAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val specificPosterImg: ImageView = view.findViewById(R.id.specificPosterImg)
        val specificPosterName: TextView = view.findViewById(R.id.specificPosterName)
        val specificTitlePostText: TextView = view.findViewById(R.id.specificTitlePostText)
        val specificContentPostText: TextView = view.findViewById(R.id.specificContentPostText)
        val specificTimePostText: TextView = view.findViewById(R.id.specificTimePostText)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecificPostAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.specific_post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecificPostAdapter.ViewHolder, position: Int) {
        val post = postList[position]
        val poster = userViewModel.searchUserByPhoneNumber(post.posterPN)
        holder.specificTitlePostText.text = post.title
        holder.specificContentPostText.text = post.content
        holder.specificTimePostText.text = post.time
        holder.specificPosterImg.setImageBitmap(poster?.image)
        holder.specificPosterName.text = poster?.username
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

}