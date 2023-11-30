package com.example.mobile_programming_teamproject.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_programming_teamproject.R
import com.example.mobile_programming_teamproject.chatList.ChatListItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.mobile_programming_teamproject.chatdata.ChatRoomActivity
import com.google.firebase.database.DatabaseReference
import com.example.mobile_programming_teamproject.DBKey
import com.google.firebase.database.ktx.database

class DetailItemActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private lateinit var userDB: DatabaseReference
    private lateinit var articleDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailitem)

        // 받은 정보
        val receivedArticleKey = intent.getStringExtra("articleKey")
        val articleModel = ArticleModel()
        // 여기에서 articleKey를 사용하여 해당 모델을 가져와야 합니다.
        userDB = Firebase.database.reference.child("your_user_node")
        // 레이아웃 내의 각각의 뷰에 정보 설정
        val titleText = findViewById<TextView>(R.id.titleText)
        titleText.text = intent.getStringExtra("title")

        val priceText = findViewById<TextView>(R.id.priceText)
        priceText.text = intent.getStringExtra("price")

        val seller = findViewById<TextView>(R.id.seller)
        seller.text = intent.getStringExtra("sellerID")

        val receivedStatus = intent.getBooleanExtra("status", false)
        val radioStatus = findViewById<RadioButton>(R.id.radioStatus)
        radioStatus.isChecked = receivedStatus

            // 채팅 버튼 클릭 시, 해당 판매자와 채팅하는 로직 작성

            val chatButton = findViewById<Button>(R.id.chatButton)
            chatButton.setOnClickListener {
                userDB = Firebase.database.reference.child("your_user_node")
                val chatRoom = ChatListItem(
                    buyerId = auth.currentUser!!.uid,
                    sellerId = articleModel.sellerID,
                    itemTitle = articleModel.title,
                    key = System.currentTimeMillis(),
                )

                userDB.child(auth.currentUser!!.uid)
                    .child(DBKey.CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                userDB.child(articleModel.sellerID)
                    .child(DBKey.CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)
                //Snackbar.make(chatButton,"채팅방이 생성되었습니다.", Snackbar.LENGTH_LONG).show()
                val intent = Intent(this, ChatRoomActivity::class.java)
                // 필요한 정보가 있다면 여기에 추가할 수 있음
                startActivity(intent)
            }
        }
    }
