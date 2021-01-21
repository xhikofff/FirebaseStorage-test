package com.kaiodonadelli.firebasestorage

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference

    private val CODE_IMG = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        config()

        fbUpload.setOnClickListener {
            setIntent()
        }
    }

    fun config() {
        alertDialog = SpotsDialog.Builder().setContext(this).build()
        storageReference = FirebaseStorage.getInstance().getReference("prod_img")
    }


    fun setIntent() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "captura imagem"), CODE_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG) {
            val uploadTask = storageReference.putFile(data!!.data!!)

//          uploadTask.continueWithTask { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this,"Chegando",Toast.LENGTH_SHORT).show()
//                }

            uploadTask.continueWithTask { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Chegando", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    Log.i("URI",downloadUri.toString())

                    val url = downloadUri!!.toString()
                        .substring(0, downloadUri.toString().indexOf("&token"))

                    Log.i("URL referência ", url)

                    alertDialog.dismiss()
                    Picasso.get().load(url).into(ivRec)
                }
            }
        }
    }
}