package com.example.madlabminiproject.models

data class Post (
    val text:String ="",
    val createdBy:UserPost =UserPost(),
    val createdAt:Long=0L,
    val likedBy: ArrayList<String> = ArrayList()
        )