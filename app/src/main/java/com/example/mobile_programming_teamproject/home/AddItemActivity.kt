package com.example.mobile_programming_teamproject.home

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_ITEMS
import com.example.mobile_programming_teamproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddItemActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val ItemDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ITEMS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)


         findViewById<Button>(R.id.submitButton).setOnClickListener {
             val title = findViewById<EditText>(R.id.titleEditText).text.toString().orEmpty()
             val price = findViewById<EditText>(R.id.priceEditText).text.toString().orEmpty()
             val content = findViewById<EditText>(R.id.contentEditText).text.toString().orEmpty()
             val sellerId = auth.currentUser?.uid.orEmpty()

             showProgress()

                 uploadItem(sellerId, title, content, price, "", false)

         }
    }


    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = true
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = false
    }

    private fun uploadItem(sellerId: String, title: String, content: String, price: String, imageUrl: String, status: Boolean) {
        val model = ItemModel(sellerId, title, content, System.currentTimeMillis(), "$price 원", imageUrl, status)
        ItemDB.push().setValue(model)

        hideProgress()

        finish()
    }
}
