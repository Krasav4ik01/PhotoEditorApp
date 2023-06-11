package com.example.photoeditorapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants

class MainActivity : AppCompatActivity() {
    lateinit var imageButtonUpload: ImageButton
    val IMAGE_REQUEST_CODE: Int = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageButtonUpload = findViewById(R.id.btn_upload)
        imageButtonUpload.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
//            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE){
            if (data?.data != null){
                var filePath: Uri? = data.data
                val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
                dsPhotoEditorIntent.data = filePath
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "folder")

                val toolsToHide = intArrayOf(DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP)
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide)

                startActivityForResult(dsPhotoEditorIntent, 200)

            }
        }
        if (requestCode == 200){
            val intent = Intent(this, ResultActivity::class.java)
            intent.setData(data?.data)
            startActivity(intent)
        }
    }

}