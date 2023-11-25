package com.example.mobile_programming_teamproject.chatdata

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_CHATS
import com.example.mobile_programming_teamproject.R
import com.example.mobile_programming_teamproject.databinding.ActivityChatroomBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatroomBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val chatKey = intent.getLongExtra("chatKey", -1) // chatListFragment에서 클릭된 채팅방

        chatDB = Firebase.database.reference.child(DB_CHATS).child("$chatKey")
        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?:return

                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        findViewById<RecyclerView>(R.id.chatRecyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.chatRecyclerView).layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val messageText = findViewById<EditText>(R.id.messageEditText).text.toString()

            val chatItem = ChatItem(
                senderId = auth.currentUser?.uid.orEmpty(),
                message = messageText
            )

            chatDB?.push()?.setValue(chatItem)
        }
    })


    }
}
