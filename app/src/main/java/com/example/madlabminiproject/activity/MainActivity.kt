package com.example.madlabminiproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.madlabminiproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser

        Handler().postDelayed({

        if (user == null) {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this,DashBoardActivity::class.java))
            finish()
        }
        },3000)
    }
}