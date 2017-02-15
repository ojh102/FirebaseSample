package com.github.ojh.firebaseauthsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val mainAdapter by lazy { MainAdapter() }
    val mDatabase: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    val chickenListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot?) {
            val chickenRepo = dataSnapshot?.getValue(ChickenRepo::class.java)
            mainAdapter.replace(chickenRepo!!.chicken)
        }

        override fun onCancelled(databaseError: DatabaseError?) {
            Toast.makeText(this@MainActivity, databaseError?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDatabase.addValueEventListener(chickenListener)

        val user = FirebaseAuth.getInstance().currentUser
        Toast.makeText(this@MainActivity, user?.email, Toast.LENGTH_SHORT).show()

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = mainAdapter
    }

    override fun onStart() {
        super.onStart()
        mDatabase.addValueEventListener(chickenListener)
    }

    override fun onStop() {
        mDatabase.removeEventListener(chickenListener)
        super.onStop()
    }
}
