package com.example.photoeditorapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
            val imageUri = getImageUriFromImageView(prevImage)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(shareIntent, "Отправить изображение через"))
        }
    }

    private fun getImageUriFromImageView(imageView: ImageView): Uri {
        val drawable = imageView.drawable
        val bitmap: Bitmap = when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            else -> {
                val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
                val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }
        }

        val imageUri = getImageUriFromBitmap(bitmap)
        return imageUri
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val cachePath = File(applicationContext.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "image.png")

        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        return FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName + ".fileprovider",
            file
        )
    }
}
