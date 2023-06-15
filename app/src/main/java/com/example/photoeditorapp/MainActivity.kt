package com.example.photoeditorapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants

class MainActivity : AppCompatActivity() {
    private lateinit var imageButtonUpload: ImageButton
    private val GALLERY_REQUEST_CODE: Int = 101
    private val CAMERA_REQUEST_CODE: Int = 102
    private val IMAGE_REQUEST_CODE: Int = 103
    private val PERMISSION_REQUEST_CODE: Int = 104
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageButtonUpload = findViewById(R.id.btn_upload)
        imageButtonUpload.setOnClickListener {
            showImageSelectionOptions()
        }
    }

    private fun showImageSelectionOptions() {
        val items = arrayOf<CharSequence>("Выбрать из галереи", "Открыть камеру")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Выберите источник изображения")
        builder.setItems(items) { dialog, item ->
            when (item) {
                0 -> {

                        openGallery()

                }
                1 -> {

                        openCamera()


                }
            }
        }
        builder.show()
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImageSelectionOptions()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null) {
                        selectedImageUri = data.data
                        openImageEditor(selectedImageUri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    if (data?.extras?.get("data") != null) {
                        val imageBitmap = data.extras?.get("data") as Bitmap
                        selectedImageUri = getImageUriFromBitmap(imageBitmap)
                        openImageEditor(selectedImageUri)
                    }
                }
                IMAGE_REQUEST_CODE -> {
                    val editedImageUri = data?.data
                    openResultActivity(editedImageUri)
                }
            }
        }
    }

    private fun openImageEditor(imageUri: Uri?) {
        val intent = Intent(this, DsPhotoEditorActivity::class.java)
        intent.data = imageUri
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "folder")

        val toolsToHide = intArrayOf(
            DsPhotoEditorActivity.TOOL_ORIENTATION,
            DsPhotoEditorActivity.TOOL_CROP
        )
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide)

        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    private fun openResultActivity(imageUri: Uri?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.data = imageUri
        startActivity(intent)
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val imagePath = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "title",
            null
        )
        return Uri.parse(imagePath)
    }
}

