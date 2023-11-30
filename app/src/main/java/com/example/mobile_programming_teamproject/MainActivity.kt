package com.example.mobile_programming_teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_programming_teamproject.home.Home
import com.example.mobile_programming_teamproject.chatList.ChatListFragment
import com.example.mobile_programming_teamproject.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAuth = FirebaseAuth.getInstance()

        val home = Home()
        val chatList = ChatListFragment()
        val myPage = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        addFragment(home)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> addFragment(home)
                R.id.chatList -> addFragment(chatList)
                R.id.myPage -> addFragment(myPage)
            }
            return@setOnItemSelectedListener true
        }


    }
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }
    }
}