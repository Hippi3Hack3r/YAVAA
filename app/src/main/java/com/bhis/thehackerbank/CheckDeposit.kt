package com.bhis.thehackerbank

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class CheckDeposit : AppCompatActivity() {

    private val cameraRequest = 1888
    lateinit var imageView: ImageView
    lateinit var photob : Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_deposit)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)


        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        imageView = findViewById(R.id.imageView)
        val photoButton: Button = findViewById(R.id.button)
        photoButton.setOnClickListener {

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
            successAlert()
            this.photob = photo
        }

        createImageFile(photob)

    }
    fun successAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Photo Accepted!")
            .setMessage("It may take up to 5 days to see the changes reflected in your account.")
            .setNegativeButton("Okay") { _: DialogInterface, i: Int ->
                startActivity(
                    Intent(
                        applicationContext,
                        AccountViewMain::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
            .setCancelable(false)
            .show()
    }
    fun createImageFile(bitmap: Bitmap){
        val filename = "last_deposit"
        val mimeType: String =  "image/png"
        val directory: String = Environment.DIRECTORY_PICTURES
        val mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageOutStream: OutputStream
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            }

            contentResolver.run {
                val uri =
                    contentResolver.insert(mediaContentUri, values)
                        ?: return
                imageOutStream = openOutputStream(uri) ?: return
            }
        } else {
            val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
            val image = File(imagePath, filename)
            imageOutStream = FileOutputStream(image)
        }

        imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}