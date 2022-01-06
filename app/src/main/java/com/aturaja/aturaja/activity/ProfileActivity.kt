package com.aturaja.aturaja.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aturaja.aturaja.R
import com.aturaja.aturaja.model.GetProfileResponse
import com.aturaja.aturaja.model.SetUpProfileResponse
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.session.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.io.IOException

import java.io.FileNotFoundException

import java.io.FileOutputStream

import java.io.File

import java.io.OutputStream

import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.content.DialogInterface
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


class ProfileActivity : AppCompatActivity() {
    private lateinit var imgView: ImageView
    private lateinit var camerabtn: ImageButton
    private lateinit var uri1: Uri
    private lateinit var editTextName: EditText
    private lateinit var editTextPN: EditText
    private var TAG = "profileactivity"
    private var imagePath = ""

    private lateinit var exitBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initComponent()

        camerabtn.setOnClickListener {
            checkAndGetCamerapermissions()
        }

        exitBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        getUserProfile()
    }

    private fun initComponent() {
        imgView = findViewById(R.id.ivProfile)
        camerabtn = findViewById(R.id.ibcapture)
        editTextName = findViewById(R.id.etName)
        editTextPN = findViewById(R.id.etPhone)
        exitBtn = findViewById(R.id.imageButton)
    }

    private fun checkAndGetCamerapermissions() {
        val arrayPermission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if(ContextCompat.checkSelfPermission(this,
                arrayPermission[0]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
                arrayPermission[1]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
                arrayPermission[2]) == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayPermission,
                100
            )
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayPermission,
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openExternalStorage()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun selectImage() {
//        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder.setTitle("Add Photo!")
//        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
//            if (options[item] == "Take Photo") {
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
//                startActivityForResult(intent, 200)
//            }
//            else if (options[item] == "Choose from Gallery") {
//                val intent =
//                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(intent, 250)
//            }
//            else if (options[item] == "Cancel") {
//                dialog.dismiss()
//            }
//        })
//        builder.show()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != RESULT_CANCELED && data != null) {
            if (requestCode == 250 && resultCode == Activity.RESULT_OK) {
                setImageFromExternal(data.data)
            }
        }
    }

//    private fun setImageFromCamera() {
//        var f = File(Environment.getExternalStorageDirectory().toString())
//        for (temp in f.listFiles()) {
//            if (temp.name == "temp.jpg") {
//                f = temp
//                break
//            }
//        }
//        try {
//            var bitmap: Bitmap
//            val bitmapOptions = BitmapFactory.Options()
//            bitmap = BitmapFactory.decodeFile(f.absolutePath, bitmapOptions)
//            bitmap = getResizedBitmap(bitmap, 400)
//            imgView.setImageBitmap(bitmap)
//            BitMapToString(bitmap)
//            val path = (Environment
//                .getExternalStorageDirectory()
//                .toString() + File.separator
//                    + "Phoenix" + File.separator + "default")
//            f.delete()
//            var outFile: OutputStream? = null
//            val file = File(path, System.currentTimeMillis().toString() + ".jpg")
//            try {
//                outFile = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile)
//                outFile.flush()
//                outFile.close()
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun BitMapToString(userImage1: Bitmap): String? {
//        val baos = ByteArrayOutputStream()
//        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos)
//        val b = baos.toByteArray()
//        imagePath = Base64.encodeToString(b, Base64.DEFAULT)
//        return imagePath
//    }
//
//    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
//        var width = image.width
//        var height = image.height
//        val bitmapRatio = width.toFloat() / height.toFloat()
//        if (bitmapRatio > 1) {
//            width = maxSize
//            height = (width / bitmapRatio).toInt()
//        }
//        else {
//            height = maxSize
//            width = (height * bitmapRatio).toInt()
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true)
//    }

    private fun setImageFromExternal(data: Uri?) {
        val selectedImage: Uri? = data
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val c = selectedImage?.let { contentResolver.query(it, filePath, null, null, null) }
        c!!.moveToFirst()
        val columnIndex = c.getColumnIndex(filePath[0])
        val picturePath = c.getString(columnIndex)
        c.close()

        imagePath = picturePath
        imgView.setImageURI(selectedImage)
    }

    private fun openExternalStorage() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, 250)
    }

    private fun openCamera() {
        var camera = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, 200)
    }

    private fun uploadPhoto() {
        val apiCLient = APIClient()
        val file = File(imagePath)
        val responseOut = "file size is too big or format file not PNG, JPG"
        val name: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextName.text.toString())
        val phone: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), editTextPN.text.toString())
        val requestFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val photo: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", file.name, requestFile)

            apiCLient.getApiService(this).setUpProfile(name, photo, phone)
                .enqueue(object : Callback<SetUpProfileResponse> {
                    override fun onResponse(
                        call: Call<SetUpProfileResponse>,
                        response: Response<SetUpProfileResponse>
                    ) {
                        if(response.code() == 200) {
                            Toast.makeText(applicationContext, "profil changed succesfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                        } else if(response.code() == 401){
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            SessionManager(applicationContext).clearTokenAndUsername()
                            finish()
                        } else {
                            Toast.makeText(applicationContext, responseOut, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SetUpProfileResponse>, t: Throwable) {
                        Log.d(TAG, "$t")
                    }

                })
    }

    private fun checkUpload() {
        val name = editTextName.text.trim().toString()
        val phone = editTextPN.text.trim().toString()
        var count = 0

        if(name.isNotEmpty() && imagePath.isNotEmpty() && phone.isNotEmpty()) {
            for(i in phone) {
                count++
            }
            if(count < 10) {
                Toast.makeText(this, "The phone number must be between 10 and 16 digits.", Toast.LENGTH_SHORT).show()
            } else {
                uploadPhoto()
            }
        } else {
            Toast.makeText(this, "please fill the data", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getUserProfile() {
        val apiClient = APIClient()

        apiClient.getApiService(this).getProfile(SessionManager(this).fetchUsername().toString())
            .enqueue(object : Callback<GetProfileResponse> {
                override fun onResponse(
                    call: Call<GetProfileResponse>,
                    response: Response<GetProfileResponse>
                ) {
                    if(response.body()?.photo == null) {
                        imgView.setImageResource(R.drawable.a)
                    } else {
                        response.body()?.photo?.let { getPhotoResponse(it) }
                        editTextName.setText(response.body()?.fullname)
                        editTextPN.setText(response.body()?.phoneNumber)
                    }
                }

                override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                    Log.d(TAG, "error $t")
                }

            })
    }

    private fun getPhotoResponse(s: String) {
        val apiClient = APIClient()

        apiClient.getApiService(this).getPhoto(s)
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())

                        imgView.setImageBitmap(bmp)
                        saveImageBitmap(s, bmp)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "Error : $t")
                }

            })
    }

    private fun saveImageBitmap(fileName: String, bitmap: Bitmap) {
        val f = File(this.cacheDir, fileName);
        f.createNewFile();

        imagePath = f.absolutePath

        val bitmap: Bitmap = bitmap
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        val bitmapData = bos.toByteArray()

        var fos: FileOutputStream?= null
        try {
            fos = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }
        try {
            fos?.write(bitmapData)
            fos?.flush();
            fos?.close();
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    fun saveOnClick(view: View) {
        val etName = editTextName.text.toString()
        val etPhone = editTextPN.text.toString()
        if(etName.trim().isEmpty() || etPhone.trim().isEmpty() || imagePath.isEmpty()) {
            Toast.makeText(this, "please fullfill data", Toast.LENGTH_SHORT).show()
        } else {
            checkUpload()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
