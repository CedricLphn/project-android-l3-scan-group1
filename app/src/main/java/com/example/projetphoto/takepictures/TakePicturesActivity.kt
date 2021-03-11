package com.example.projetphoto.takepictures

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.projetphoto.R
import com.example.projetphoto.db.AppDatabase
import com.example.projetphoto.db.objects.Objects
import com.example.projetphoto.db.pictures.Pictures
import java.io.File
import java.io.FileOutputStream


private lateinit var cameraBtn:Button
private lateinit var myImage:ImageView
private val cameraRequestId  = 1222
private const val TAG = "MyActivity"
private val PERMISSION_CODE = 1000;

class TakePicturesViewModel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate: debut OnCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_takepictures)
        cameraBtn = findViewById(R.id.cameraBtn)
        myImage = findViewById(R.id.myImage)

        if (ContextCompat.checkSelfPermission(
                        applicationContext, Manifest.permission.CAMERA
                )== PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    cameraRequestId
            )
        cameraBtn.setOnClickListener {
            val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraInt, cameraRequestId)
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId){
            val images = data?.extras?.get("data") as Bitmap
            myImage.setImageBitmap(images)
            Log.i(TAG, "onActivityResult: $images")
            Log.i(TAG, "onActivityResult: $data")

            if (ContextCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE)
                //Manifest.permission.WRITE_EXTERNAL_STORAGE + Manifest.permission.READ_EXTERNAL_STORAGE
            } else {
                saveImage(images, "test2")
            }
        }
    }
    private fun saveImage(finalBitmap: Bitmap, image_name: String) {
        
        val root: String = getExternalFilesDir(image_name).toString()
        val myDir = File(root)
        myDir.mkdirs()
        val fname = "Image-$image_name.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        Log.i("LOAD", root + fname)
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Log.i(TAG, "saveImage: DONE")

        } catch (e: Exception) {
            Log.e(TAG, "saveImage: ", e)
        }
    }

}