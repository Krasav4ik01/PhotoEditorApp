package com.example.photoeditorapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.ImageButton
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream

class ResultActivity : AppCompatActivity() {
    lateinit var prevImage: ImageView
    lateinit var shareBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        prevImage = findViewById(R.id.image_result)
        prevImage.setImageURI(intent.data)
        shareBtn = findViewById(R.id.share_img)

        shareBtn.setOnClickListener {
            shareImage()

        }
    }
    private fun shareImage() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        prevImage = findViewById(R.id.image_result)
        prevImage.setImageURI(intent.data)
        val drawable = prevImage.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val file = File(getExternalCacheDir(), "$bitmap")
        val intent: Intent
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        startActivity(Intent.createChooser(intent, "Share image Via: "))
    }




}