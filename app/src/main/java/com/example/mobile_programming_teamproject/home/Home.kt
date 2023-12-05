package com.example.mobile_programming_teamproject.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_ITEMS
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_USERS
import com.example.mobile_programming_teamproject.R
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


    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemDB: DatabaseReference
    private lateinit var userDB: DatabaseReference

    private val itemList = mutableListOf<ItemModel>()

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val itemModel = snapshot.getValue(ItemModel::class.java)
            itemModel ?: return
            itemModel.itemKey = snapshot.key

            itemList.add(itemModel)
            itemAdapter.submitList(itemList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val itemModel = snapshot.getValue(ItemModel::class.java) ?: return
            itemModel.itemKey = snapshot.key

            val position = itemList.indexOfFirst { it.itemKey == itemModel.itemKey }
            if (position > -1) {
                itemList[position] = itemModel
                itemAdapter.notifyItemChanged(position)
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

        itemList.clear()

        itemDB = Firebase.database.reference.child(DB_ITEMS)
        userDB = Firebase.database.reference.child(DB_USERS)
        itemAdapter = ItemAdapter(onItemClicked = { itemModel->
            if(auth.currentUser != null) {
                if(auth.currentUser!!.uid != itemModel.sellerID) {
                    val intent = Intent(requireContext(), DetailItemActivity::class.java)
                    intent.putExtra("itemKey", itemModel.itemKey)
                    intent.putExtra("title", itemModel.title)
                    intent.putExtra("price", itemModel.price)
                    intent.putExtra("sellerID", itemModel.sellerID)
                    intent.putExtra("status", itemModel.status)
                    startActivity(intent)
                }
                else {
                    val intent = Intent(requireContext(), EditItemActivity::class.java)
                    intent.putExtra("itemKey", itemModel.itemKey)
                    startActivity(intent)
                }
            }
            else {
                Snackbar.make(view, "회원가입 후 이용해주세요.", Snackbar.LENGTH_LONG).show()
            }
        })

        fragmentHoneBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHoneBinding.articleRecyclerView.adapter = itemAdapter

        fragmentHoneBinding.addFloatingButton.setOnClickListener {
            if(auth.currentUser != null) {
                val intent = Intent(requireContext(), AddItemActivity::class.java)
                startActivity(intent)
            }
            else{
                Snackbar.make(view, "회원가입 후 이용해주세요", Snackbar.LENGTH_LONG).show()
            }
        }
        itemDB.addChildEventListener(listener)

        fragmentHoneBinding.radioAll.setOnClickListener {
            loadAllItems()
        }

        fragmentHoneBinding.radioSale.setOnClickListener {
            loadSaleItems()
        }


    }

    private fun loadSaleItems() {
        val saleItemList = itemList.filter { !it.status }
        itemAdapter.submitList(saleItemList)
    }

    private fun loadAllItems() {
        itemAdapter.submitList(itemList)
    }


    override fun onResume() {
        super.onResume()
        itemAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemDB.removeEventListener(listener)

    }
}