package com.example.mobile_programming_teamproject.chatList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_programming_teamproject.DBKey.Companion.CHILD_CHAT
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_USERS
import com.example.mobile_programming_teamproject.chatdata.ChatRoomActivity
import com.example.mobile_programming_teamproject.R
import com.example.mobile_programming_teamproject.databinding.FragmentChatlistBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChatListFragment : Fragment(R.layout.fragment_chatlist) {
    private var mBinding : FragmentChatlistBinding? = null
    private val binding get() = mBinding!! // null 값 허용 x
    private lateinit var chatListAdapter : ChatListAdapter
    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    private val  chatRoomList = mutableListOf<ChatListItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        mBinding = fragmentChatlistBinding

        chatListAdapter = ChatListAdapter(onItemClicked = { chatListItem ->
            context?.let {
            val intent = Intent(it, ChatRoomActivity::class.java)
            intent.putExtra("chatKey", chatListItem.key)
            startActivity(intent)
            }
        })
        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if (auth.currentUser == null) {
            return
        }

        val chatDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser!!.uid).child(CHILD_CHAT)
        chatDB.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {  }
        })

    }
        override fun onResume() {
            super.onResume()
            chatListAdapter.notifyDataSetChanged()
        }
    }
