package com.example.mobile_programming_teamproject.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mobile_programming_teamproject.LoginActivity
import com.example.mobile_programming_teamproject.R
import com.google.firebase.auth.FirebaseAuth

class MyPageFragment : Fragment(R.layout.fragment_mypage) {

    private lateinit var btnLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout = view.findViewById(R.id.btn_logout)
        firebaseAuth = FirebaseAuth.getInstance()

        btnLogout.setOnClickListener {
            // 로그아웃하기
            firebaseAuth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}