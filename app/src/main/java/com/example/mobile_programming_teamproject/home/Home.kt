package com.example.mobile_programming_teamproject.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_programming_teamproject.DBKey
import com.example.mobile_programming_teamproject.DBKey.Companion.CHILD_CHAT
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_ARTICLES
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_USERS
import com.example.mobile_programming_teamproject.R
import com.example.mobile_programming_teamproject.chatList.ChatListItem
import com.example.mobile_programming_teamproject.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Home : Fragment(R.layout.fragment_home) {


    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference

    private val articleList = mutableListOf<ArticleModel>()

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return
            articleModel.articleKey = snapshot.key

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java) ?: return
            articleModel.articleKey = snapshot.key

            val position = articleList.indexOfFirst { it.articleKey == articleModel.articleKey }
            if (position > -1) {
                articleList[position] = articleModel
                articleAdapter.notifyItemChanged(position)
            }
        }
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }
    private var binding : FragmentHomeBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHoneBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHoneBinding

        articleList.clear()

        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        userDB = Firebase.database.reference.child(DB_USERS)
        articleAdapter = ArticleAdapter(onItemClicked = { articleModel->
            if(auth.currentUser != null) { // 로그인 상태
                if(auth.currentUser!!.uid != articleModel.sellerID) {
                    val intent = Intent(requireContext(), DetailItemActivity::class.java)
                    intent.putExtra("articleKey", articleModel.articleKey)
                    intent.putExtra("title", articleModel.title)
                    intent.putExtra("price", articleModel.price)
                    intent.putExtra("sellerID", articleModel.sellerID)
                    intent.putExtra("status", articleModel.status)
                    startActivity(intent)
                }
                else { //내가 올린 아이템일 때
                    val intent = Intent(requireContext(), MyDetailItemActivity::class.java)
                    intent.putExtra("articleKey", articleModel.articleKey)
                    intent.putExtra("title", articleModel.title)
                    intent.putExtra("price", articleModel.price)
                    intent.putExtra("sellerID", articleModel.sellerID)
                    intent.putExtra("status", articleModel.status)
                    startActivity(intent)
                }
            }
            else { //로그인하지 않은 상태
                Snackbar.make(view, "회원만 이용 가능합니다.", Snackbar.LENGTH_LONG).show()
            }
        })

        fragmentHoneBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHoneBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHoneBinding.addFloatingButton.setOnClickListener {
            if(auth.currentUser != null) {
                val intent = Intent(requireContext(), AddArticleActivity::class.java)
                startActivity(intent)
            }
            else{
                Snackbar.make(view, "회원만 이용 가능합니다.", Snackbar.LENGTH_LONG).show()
            }
        }
        articleDB.addChildEventListener(listener)

        fragmentHoneBinding.radioAll.setOnClickListener {
            loadAllArticles() // 모든 판매 여부 항목을 표시하는 함수 호출
        }

        fragmentHoneBinding.radioSale.setOnClickListener {
            loadSaleArticles() // 판매 여부가 false인 항목만 표시하는 함수 호출
        }

        //loadAllArticles()
    }

    private fun loadAllArticles() {
        articleAdapter.submitList(articleList) // 모든 판매 항목을 RecyclerView에 전달
    }

    private fun loadSaleArticles() {
        val saleArticleList = articleList.filter { !it.status } // 판매 여부가 false인 항목만 필터링
        articleAdapter.submitList(saleArticleList) // 필터링된 항목을 RecyclerView에 전달
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)

    }
}