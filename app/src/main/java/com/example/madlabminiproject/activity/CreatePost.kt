package com.example.madlabminiproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.madlabminiproject.R
import com.example.madlabminiproject.dao.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePost : AppCompatActivity() {
    private lateinit var postDao:PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        postDao= PostDao()
        createPost.setOnClickListener {
            val input=postInput.text.toString().trim()
            if(input.isNotEmpty()){
                postDao.addPost(input)
                finish()
            }
            else{
                Toast.makeText(this,"Please enter text to create post", Toast.LENGTH_LONG).show()
            }
        }
    }
}