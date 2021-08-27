package com.example.madlabminiproject.dao

import com.example.madlabminiproject.models.Post
import com.example.madlabminiproject.models.UserPost
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db=FirebaseFirestore.getInstance()
    val postCollection=db.collection("post")
    val auth= FirebaseAuth.getInstance()

    fun addPost(text:String){
        val currentUserId=auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao=UserDao()
            val user=userDao.getUserById(currentUserId).await().toObject(UserPost::class.java)!!
            val currentTime=System.currentTimeMillis()
            val post= Post(text,user,currentTime)
            postCollection.document().set(post)
        }

    }
    fun getPostById(postId:String):Task<DocumentSnapshot>{
        return postCollection.document(postId).get()
    }
    fun updateLikes(postId:String){
        GlobalScope.launch {
            val currUser=auth.currentUser!!.uid
            val post=getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked= post.likedBy.contains(currUser)
            if(isLiked){
                post.likedBy.remove(currUser)
            }else{
                post.likedBy.add(currUser)
            }
            postCollection.document(postId).set(post)
        }

    }
}