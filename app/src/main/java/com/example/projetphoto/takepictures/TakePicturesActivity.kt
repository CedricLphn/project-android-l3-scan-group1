package com.example.projetphoto.takepictures

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.annotation.RequiresApi
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
private val PERMISSION_CODE = 1000

class TakePicturesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakepicturesBinding


    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakepicturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED) {
            demandPermissions()

        }
        binding.cameraBtn.setOnClickListener {
            //textDialog()

            if(!binding.switchSimulate.isChecked) {
                val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraInt, cameraRequestId)
            }else {
                Log.i(TAG, "onCreate: simulaaaaaaaaate")
                var resId = R.drawable.simulate
                var resources = resources
                // On sait que ca crash ici mais i don't know why :(
                var uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + "-v24/" + resources.getResourceEntryName(resId)+ ".jpg" )
                sendAndSwitchActivity(uri.toString())
            }
        }

        binding.changeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.cameraBtn.isEnabled = s.isNotBlank()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.switchSimulate.setOnClickListener {
            toggleSimulation(binding.switchSimulate.isChecked)
            Log.i(TAG, "onTextChanged: ${binding.switchSimulate.isChecked}")

        }

    }

    fun toggleSimulation(btn : Boolean) {
        if(btn) {
            binding.myImage.setImageResource(R.drawable.simulate)
            binding.cameraBtn.text = "Validate"
        }else {
            binding.myImage.setImageURI(null)
            binding.cameraBtn.text = "Take a picture"

        }
    }
   

    @RequiresApi(Build.VERSION_CODES.M)
    private fun demandPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            cameraRequestId
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId){
            val image = data?.extras?.get("data") as Bitmap
            binding.myImage.setImageBitmap(image)

            val sdf = SimpleDateFormat("dd-M-yyyy_hh-mm-ss")
            val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            Log.i(TAG, "date **********: $date")
            val currentDate = sdf.format(Date())
            val name = saveImage(image, "$currentDate")
            //val name = saveImage(image, "test2")
            sendAndSwitchActivity(name)

        }
    }

    private fun sendAndSwitchActivity(name: String?) {
        val returnIntent = Intent(this, TakePicturesActivity::class.java)
        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        returnIntent.putExtra("rootFile", name)
        returnIntent.putExtra("title", binding.changeEditText.text.toString())
        returnIntent.putExtra("date", date.format(Date()))
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun saveImage(finalBitmap: Bitmap, image_name: String): String? {
        
        val root: String = getExternalFilesDir(image_name).toString()
        val myDir = File(root)
        myDir.mkdirs()
        val fname = "Image-$image_name.jpg"
        val file = File(myDir, fname)

        val fullpath = "${getExternalFilesDir(image_name)}/${fname}"


        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Log.i(TAG, "saveImage: $fullpath")

            return fullpath

        } catch (e: Exception) {
            Log.e(TAG, "saveImage: ", e)
        }

        return null
    }

}