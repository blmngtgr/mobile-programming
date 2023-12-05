package com.example.mobile_programming_teamproject.home

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mobile_programming_teamproject.DBKey
import com.example.mobile_programming_teamproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class EditItemActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val ItemDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_ITEMS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_item)

        val itemKey = intent.getStringExtra("itemKey") ?: return



        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString().orEmpty()
            val price = findViewById<EditText>(R.id.priceEditText).text.toString().orEmpty()
            val content = findViewById<EditText>(R.id.contentEditText).text.toString().orEmpty()
            val sellerId = auth.currentUser?.uid.orEmpty()

            showProgress()

                if(findViewById<RadioButton>(R.id.radioStatus).isChecked) {
                    updateItem(itemKey, sellerId, title, price, content, "", false)
                }
                else {
                    updateItem(itemKey, sellerId, title, price, content, "", true)
                }
        }
    }



    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = true
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = false
    }

    private fun updateItem(itemKey: String, sellerId: String, title: String, price: String, content: String, imageUrl: String, status: Boolean) {
        val updatedValues = HashMap<String, Any>()
        updatedValues["title"] = title
        updatedValues["price"] = "$price 원"
        updatedValues["content"] = content
        updatedValues["imageUrl"] = imageUrl
        updatedValues["createdAt"] = System.currentTimeMillis()
        if(findViewById<RadioButton>(R.id.radioStatus).isChecked) {
            updatedValues["status"] = true
        } else { updatedValues["status"] = false
        }
        ItemDB.child(itemKey).updateChildren(updatedValues).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                hideProgress()
                finish()
            }
        }
    }

}
