package com.example.mobile_programming_teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        findViewById<TextView>(R.id.textUID)?.text = Firebase.auth.currentUser?.uid ?: "NoUser"

        findViewById<Button>(R.id.button_signout)?.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

    }
}