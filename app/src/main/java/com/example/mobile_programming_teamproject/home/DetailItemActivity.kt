
package com.example.mobile_programming_teamproject.home
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_programming_teamproject.DBKey
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_ITEMS
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_USERS
import com.example.mobile_programming_teamproject.R
import com.example.mobile_programming_teamproject.chatList.ChatListItem
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailItemActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private lateinit var ItemDB: DatabaseReference
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailitem)

        val receivedItemKey = intent.getStringExtra("itemKey")
        userDB = Firebase.database.reference.child(DB_USERS)
        ItemDB = Firebase.database.reference.child(DB_ITEMS).child(receivedItemKey!!)

        ItemDB.get().addOnSuccessListener { snapshot ->
            val itemModel = snapshot.getValue(ItemModel::class.java) ?: return@addOnSuccessListener

            userDB = Firebase.database.reference.child(DB_USERS)
            // 레이아웃 내의 각각의 뷰에 정보 설정
            val titleText = findViewById<TextView>(R.id.titleText)
            titleText.text = intent.getStringExtra("title")

            val priceText = findViewById<TextView>(R.id.priceText)
            priceText.text = intent.getStringExtra("price")

            val contentText = findViewById<TextView>(R.id.content)
            contentText.text = itemModel.content

            val seller = findViewById<TextView>(R.id.seller)
            seller.text = intent.getStringExtra("sellerID")

            val receivedStatus = intent.getBooleanExtra("status", false)
            val radioStatus = findViewById<RadioButton>(R.id.radioStatus)
            radioStatus.isChecked = receivedStatus

            val chatButton = findViewById<Button>(R.id.chatButton)
            chatButton.setOnClickListener {
                val chatRoom = ChatListItem(
                    buyerId = auth.currentUser!!.uid,
                    sellerId = itemModel.sellerID,
                    itemTitle = itemModel.title,
                    key = System.currentTimeMillis(),
                )

                userDB.child(auth.currentUser!!.uid)
                    .child(DBKey.CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                userDB.child(itemModel.sellerID)
                    .child(DBKey.CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                Snackbar.make(chatButton,"채팅방이 생성되었습니다.", Snackbar.LENGTH_LONG).show()

            }
        }
    }
}
