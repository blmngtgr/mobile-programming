package com.example.mobile_programming_teamproject.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.mobile_programming_teamproject.DBKey.Companion.DB_ARTICLES
import com.example.mobile_programming_teamproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddArticleActivity : AppCompatActivity() {
    private var selectedUri : Uri? = null

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        findViewById<Button>(R.id.imageAddButton).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED -> {
                            startContentProvider()
                        }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }

                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
                }
            }
        }
         findViewById<Button>(R.id.submitButton).setOnClickListener {
             val title = findViewById<EditText>(R.id.titleEditText).text.toString().orEmpty()
             val price = findViewById<EditText>(R.id.priceEditText).text.toString().orEmpty()
             val sellerId = auth.currentUser?.uid.orEmpty()

             showProgress()

             if(selectedUri != null) {
                 val PhotoUri = selectedUri ?: return@setOnClickListener
                 uploadPhoto(PhotoUri, successHandler = { uri -> uploadArticle(sellerId, title, price, uri, false)},
                 errorHandler = { Toast.makeText(this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT)
                 hideProgress()})
             }
             else {
                 uploadArticle(sellerId, title, price, "", false)
             }
         }
    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("article/photo").child(fileName).putFile(uri).addOnCompleteListener {
            if(it.isSuccessful) {
                storage.reference.child("article/photo").child(fileName).downloadUrl.addOnSuccessListener {
                    uri -> successHandler(uri.toString())
                }.addOnFailureListener {
                    errorHandler()
                }
            }
            else {
                errorHandler()
            }
        }
    }
    private fun uploadArticle(sellerId: String, title: String, price: String, imageUrl: String, status: Boolean) {
        val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "$price 원", imageUrl, status)
        articleDB.push().setValue(model)

        hideProgress()

        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            1010 -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startContentProvider()
            }
            else {
                Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
////            val uri: Intent? = result.data
//
//            if(uri != null) {
//                findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
//                selectedUri = uri
//            }
//            else {
//                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun startContentProvider() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        getContentLauncher.launch(intent)
//    }

    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
            selectedUri = uri
        } else {
            Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startContentProvider() {
        getContentLauncher.launch("image/*")
    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = true
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = false
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this).setTitle("권한이 필요합니다").setMessage("사진을 가져오기 위해 동의가 필요합니다").setPositiveButton("동의") {
            _, _ -> requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
        }.create().show()
    }

}