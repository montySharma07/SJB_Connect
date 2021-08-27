package com.example.madlabminiproject.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madlabminiproject.R
import com.example.madlabminiproject.adapter.UserAdapter
import com.example.madlabminiproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.activity_users.bottom_navbar
import kotlinx.android.synthetic.main.activity_users.imgBack

class UsersActivity : AppCompatActivity() {
    var userList = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        userRecyclerView.layoutManager = LinearLayoutManager(this)

        bottom_navbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.feeds->{
                    onBackPressed()
                }
                R.id.setting->{
                    startActivity(Intent(this,ProfileActivity::class.java))
                }
            }
            true
        }

        imgBack.setOnClickListener {
            onBackPressed()
        }

//        imgProfile.setOnClickListener {
//            val intent = Intent(
//                this@UsersActivity,
//                ProfileActivity::class.java
//            )
//            startActivity(intent)
//        }
        getUsersList()
    }

    fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
//                if (currentUser!!.profileImage == ""){
//                    imgProfile.setImageResource(R.drawable.profile_image)
//                }else{
//                    Glide.with(this@UsersActivity).load(currentUser.profileImage).into(imgProfile)
//                }

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {

                        userList.add(user)
                    }
                }

                val userAdapter = UserAdapter(this@UsersActivity, userList)

                userRecyclerView.adapter = userAdapter
            }

        })
    }
}