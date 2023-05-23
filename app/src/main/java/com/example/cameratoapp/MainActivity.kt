package com.example.cameratoapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameratoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // These will be used to know if permissions have been accepted
    companion object {
        // Used an an ID for the 'request'
        private const val CAMERA_PERMISSION_CODE = 1
        // Used as an ID for the 'action'
        // Used in the intent and activityForResult
        private const val CAMERA_REQUEST_CODE = 2
    }

    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnCamera?.setOnClickListener {
            // Check if we have permission
            if(ContextCompat.checkSelfPermission(
                    this,
                    // This is the permission we are checking
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                // If true access the camera functionality using an intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // The 'action' of accessing the camera has been given the ID CAMERA_REQUEST_CODE
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } // Request for permission
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    // The 'request' to use the camera has been given the ID CAMERA_PERMISSION_CODE
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    // Function that is used to execute something once the result,which is the user's choice to either give permission or deny it, is returned
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check is the requestCode is equal to the camera request by comparing it with the request ID CAMERA_PERMISSION_CODE
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted access the action having an ID of CAMERA_REQUEST_CODE
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this,"Camera permission denied",Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This activity is automatically called once the startActivityForResult is executed.
    // It returns the result of the functionality that was used since permission was given
    // The result here is what is returned once the intended functionality is used. For example
    // we wanted to use the camera functionality. Once we use it what is returned,the result, in an image.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // We confirm our action
           if (requestCode == CAMERA_REQUEST_CODE) {
               // Retrieve the data
               // The data we need is called "data"
              val thumbNail : Bitmap = data!!.extras!!.get("data") as Bitmap
               binding?.ivImage?.setImageBitmap(thumbNail)
           }
        }
    }
}






