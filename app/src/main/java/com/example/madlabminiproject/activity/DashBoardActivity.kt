package com.example.madlabminiproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madlabminiproject.R
import com.example.madlabminiproject.adapter.IPostAdapter
import com.example.madlabminiproject.adapter.PostAdapter
import com.example.madlabminiproject.dao.PostDao
import com.example.madlabminiproject.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : AppCompatActivity(), IPostAdapter {
    private var backPressedTime =0L
    private lateinit var postDao:PostDao
    private lateinit var adapter:PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        bottom_navbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.chat->{
                    startActivity(Intent(this,UsersActivity::class.java))}
                R.id.setting->{
                    startActivity(Intent(this,ProfileActivity::class.java))
                }
            }
            true
        }

        addPost.setOnClickListener {
            startActivity(Intent(this, CreatePost::class.java))
        }
        postDao= PostDao()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val postCollection=postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val options=FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()
        adapter= PostAdapter(options,this)
        postRecyclerView.adapter=adapter
        postRecyclerView.layoutManager= LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onBackPressed() {
        if(backPressedTime+5000 > System.currentTimeMillis()){
            super.onBackPressed()
        }else{
            Toast.makeText(this,"Press back again to exit app",Toast.LENGTH_SHORT).show()
        }
        backPressedTime=System.currentTimeMillis()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }
}