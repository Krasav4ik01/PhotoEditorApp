package com.example.photoeditorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ResultActivity : AppCompatActivity() {
    lateinit var prevImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        prevImage = findViewById(R.id.image_result)
        prevImage.setImageURI(intent.data)
    }
}