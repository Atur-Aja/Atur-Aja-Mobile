package com.aturaja.aturaja.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.SetUpProfileResponse
import com.aturaja.aturaja.network.APIClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

import android.graphics.BitmapFactory
import com.aturaja.aturaja.model.GetProfileResponse
import com.aturaja.aturaja.session.SessionManager


class StupProfileActivity : AppCompatActivity() {
    lateinit var select_image_button : Button
    lateinit var make_prediction : Button
    lateinit var img_view : ImageView
    lateinit var text_view : TextView
    lateinit var bitmap: Bitmap
    lateinit var camerabtn : Button
    lateinit var uri1: Uri
    private var TAG = "setupprofil"
    private var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stup_profile)

        initComponent()
        checkandGetpermissions()

        select_image_button.setOnClickListener {
            openExternalStorage()
        }

        camerabtn.setOnClickListener {
            openCamera()
        }

        make_prediction.setOnClickListener {
            uploadPhoto()
        }
    }

    private fun initComponent() {
        select_image_button = findViewById(R.id.button)
        img_view = findViewById(R.id.imageView2)
        camerabtn = findViewById<Button>(R.id.camerabtn)
        make_prediction = findViewById(R.id.button2)
    }

    private fun checkandGetpermissions(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)
        }
        else{
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 250){
//            img_view.setImageURI(data?.data)
//
//            var uri : Uri?= data?.data
//            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            uri1 = data?.data!!
            Log.d(TAG, "${uri1.describeContents()}")
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri1)
        }
        else if(requestCode == 200 && resultCode == Activity.RESULT_OK){
//            bitmap = data?.extras?.get("data") as Bitmap
//            img_view.setImageBitmap(bitmap)
            uri1 = data?.data!!
        }
    }

    private fun openExternalStorage() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        startActivityForResult(intent, 250)
    }

    private fun openCamera() {
        var camera = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, 200)
    }

    private fun uploadPhoto() {
        val apiCLient = APIClient()
        val file = File(uri1.path)
        val name: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "iqbal")
        val phone: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "0445342533")
        val requestFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val photo: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestFile)


        apiCLient.getApiService(this).setUpProfile(name, photo, phone)
            .enqueue(object: Callback<SetUpProfileResponse> {
                override fun onResponse(
                    call: Call<SetUpProfileResponse>,
                    response: Response<SetUpProfileResponse>
                ) {
                    Log.d(TAG, "berhasil")
                }

                override fun onFailure(call: Call<SetUpProfileResponse>, t: Throwable) {
                    Log.d(TAG, "$t")
                }

            })
    }

    private fun getUserProfile() {
        val apiClient = APIClient()

        apiClient.getApiService(this).getProfile(SessionManager(this).fetchUsername().toString())
            .enqueue(object: Callback<GetProfileResponse> {
                override fun onResponse(
                    call: Call<GetProfileResponse>,
                    response: Response<GetProfileResponse>
                ) {
                    val responseImage = response
                    if(response.code() == 200) {
                    }
                }

                override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}