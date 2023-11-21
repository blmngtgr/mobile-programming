package com.example.mobile_programming_teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mEtEmail: EditText
    private lateinit var mEtPwd: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyLogin1")

        mEtEmail = findViewById(R.id.et_email)
        mEtPwd = findViewById(R.id.et_pwd)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            // 로그인 요청
            val strEmail = mEtEmail.text.toString()
            val strPwd = mEtPwd.text.toString()

            mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this@LoginActivity, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // 현재 액티비티 파괴
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            // 회원가입 화면으로 이동
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
