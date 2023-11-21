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

class RegisterActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mEtEmail: EditText
    private lateinit var mEtPwd: EditText
    private lateinit var mEtName: EditText
    private lateinit var mEtBirthDate: EditText
    private lateinit var mBtnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MyLogin1")

        mEtEmail = findViewById(R.id.et_email)
        mEtPwd = findViewById(R.id.et_pwd)
        mEtName = findViewById(R.id.et_name)
        mEtBirthDate = findViewById(R.id.et_birthdate)
        mBtnRegister = findViewById(R.id.btn_register)

        mBtnRegister.setOnClickListener {
            // 회원가입 처리 시작
            val strEmail = mEtEmail.text.toString()
            val strPwd = mEtPwd.text.toString()
            val strName = mEtName.text.toString()
            val strBirthdate = mEtBirthDate.text.toString()

            // Firebase auth 진행
            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this@RegisterActivity, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mFirebaseAuth.currentUser
                        val account = UserAccount(
                            firebaseUser!!.uid,
                            firebaseUser.email,
                            strPwd,
                            strName,
                            strBirthdate
                        )

                        mDatabaseRef.child("UserAccount").child(firebaseUser.uid).setValue(account)

                        Toast.makeText(
                            this@RegisterActivity,
                            "회원가입에 성공하셨습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        // 로그인 후 메인 화면으로 이동
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // 현재 액티비티 종료
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "회원가입에 실패하셨습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}