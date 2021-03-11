package com.example.projetphoto.takepictures

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projetphoto.R
import com.example.projetphoto.databinding.ActivityTakepicturesBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


private val cameraRequestId  = 1222
private const val TAG = "TakePicturesActivity"
private val PERMISSION_CODE = 1000;

class TakePicturesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakepicturesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakepicturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                cameraRequestId
            )
        binding.cameraBtn.setOnClickListener {
            textDialog()

           // val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           // startActivityForResult(cameraInt, cameraRequestId)
        }
    }
    private fun textDialog() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
           // val dialogLayout = inflater.inflate(binding.cameraBtn, null)
           // val editText = dialogLayout.findViewById<EditText>(editText)

            with(builder) {
                setTitle("Enter title plz")
                setPositiveButton("OK"){dialog, which ->
                    val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraInt, cameraRequestId)
                   // val text  = editText.text.toString()
                }
                setNegativeButton("Cancel"){ dialog, which ->


                }

                //setView(dialogLayout)
                show()
            }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId){
            val image = data?.extras?.get("data") as Bitmap
            binding.myImage.setImageBitmap(image)
            if (ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE)
                //Manifest.permission.WRITE_EXTERNAL_STORAGE + Manifest.permission.READ_EXTERNAL_STORAGE
            } else {
                val sdf = SimpleDateFormat("dd/M/yyyy_hh:mm:ss")
                val currentDate = sdf.format(Date())
                val name = saveImage(image, "$currentDate")
                //val name = saveImage(image, "test2")
                val returnIntent = Intent(this, TakePicturesActivity::class.java)
                returnIntent.putExtra("result", name)
                setResult(RESULT_OK, returnIntent)
                finish()

            }
        }
    }
    private fun saveImage(finalBitmap: Bitmap, image_name: String): String? {
        
        val root: String = getExternalFilesDir(image_name).toString()
        val myDir = File(root)
        myDir.mkdirs()
        val fname = "Image-$image_name.jpg"
        val file = File(myDir, fname)

        val fullpath = "${getExternalFilesDir(image_name)}/${fname}"

        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Log.i(TAG, "saveImage: $fullpath")

            return fullpath;

        } catch (e: Exception) {
            Log.e(TAG, "saveImage: ", e)
        }

        return null
    }

}