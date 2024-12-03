package com.example.fit_connect

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.Contacts.Photo
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.InvalidationTracker
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.databinding.ActivityCreateAccountBinding
import com.example.fit_connect.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCreateAccountBinding

    //Values to save
    private var username : String = ""
    private var firstname : String = ""
    private var lastname : String = ""
    private var email : String = ""
    private var password : String = ""
    private var passwordConfirmation :  String = ""

    //Retrieve values
    private lateinit var createprofileimg : ImageView
    private lateinit var editUsername : EditText
    private lateinit var editFirstName : EditText
    private lateinit var editLastName : EditText
    private lateinit var editEmail : EditText
    private lateinit var editPassword : EditText
    private lateinit var editPasswordConfirmation : EditText

    //Buttons
    private lateinit var btnTakePicture : Button
    private lateinit var btnChoosePicture : Button
    private lateinit var btnCancel : Button
    private lateinit var btnCreate : Button

    //Camera
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private lateinit var galleryResult : ActivityResultLauncher<Intent>
    private val tempImgFileName = "newimg.jpg"
    private lateinit var tempImgUri : Uri
    private var bitmap : Bitmap? = null
    private var imageData : ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get All Necessary EditText and Buttons + Image
        createprofileimg = findViewById(R.id.create_account_image)
        editUsername = findViewById(R.id.create_account_username_edit)
        editFirstName = findViewById(R.id.create_account_firstname_edit)
        editLastName = findViewById(R.id.create_account_lastname_edit)
        editEmail = findViewById(R.id.create_account_email_edit)
        editPassword = findViewById(R.id.create_account_password_edit)
        editPasswordConfirmation = findViewById(R.id.create_account_confirm_password_edit)

        btnTakePicture = findViewById(R.id.create_account_take_picture_btn)
        btnChoosePicture = findViewById(R.id.create_account_choose_picture_btn)
        btnCancel = findViewById(R.id.create_account_close_btn)
        btnCreate = findViewById(R.id.create_account_create_btn)

        setupUri()
        setupTakePictureButton()
        setupChoosePictureButton()
        setupCancelButton()
        setupCreateButton()
        setupCameraResults()
        PhotoUtil.checkPermissions(this)
    }

    private fun setupUri(){
        val tempImgFile = File(getExternalFilesDir(null), tempImgFileName)
        tempImgUri = FileProvider.getUriForFile(this, "com.example.fit_connect", tempImgFile)
    }

    private fun setupTakePictureButton() {
        btnTakePicture.setOnClickListener(){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
            cameraResult.launch(intent)
        }
    }

    private fun setupChoosePictureButton() {
        btnChoosePicture.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryResult.launch(intent)
        }
    }

    private fun setupCameraResults(){
        //Take Picture
        cameraResult = registerForActivityResult(StartActivityForResult()){
            result : ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){
                bitmap = PhotoUtil.getBitmap(this, tempImgUri)
                createprofileimg.setImageBitmap(bitmap)
                bitmap?.let{
                    saveImageToGallery(this)
                }
            }
        }
        galleryResult = registerForActivityResult(StartActivityForResult()){
            result : ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.data?.data)
                createprofileimg.setImageBitmap(bitmap)

            }
        }
    }
    private fun saveImageToGallery(context : Context){
        val contentResolver = context.contentResolver

        // Create content values to describe the image
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }
    }


    private fun setupCancelButton() {
        btnCancel.setOnClickListener(){
            finish()
        }
    }

    private fun setupCreateButton() {
        btnCreate.setOnClickListener(){
            var allcheckspassed = true

            //Get Values
            username = editUsername.text.toString()
            firstname = editFirstName.text.toString()
            lastname = editLastName.text.toString()
            email = editEmail.text.toString()
            password = editPassword.text.toString()
            passwordConfirmation = editPasswordConfirmation.text.toString()

            //Get Database
            val database = FitConnectDatabase.getInstance(this)
            val userDatabaseDao = database.userDao()
            val repository = UserRepository(userDatabaseDao)

            //Check if all information is not blank
            if (username.length == 0 ||
                firstname.length == 0 ||
                lastname.length == 0 ||
                email.length == 0 ||
                password.length == 0 ||
                passwordConfirmation.length == 0){
                Toast.makeText(this, "ALL INFORMATION MUST BE FILLED OUT", Toast.LENGTH_SHORT).show()
                allcheckspassed = false
            }


            //Check if password is same as confirmation
            if(!password.equals(passwordConfirmation)){
                Toast.makeText(this, "PASSWORD ENTERED INCORRECTLY", Toast.LENGTH_SHORT).show()
                println("PASSWORD ENTERED INCORRECTLY")
                allcheckspassed = false
            }

            if(allcheckspassed){
                if(bitmap != null){
                    imageData = PhotoUtil.bitmapToByteArray(bitmap!!)
                }

                val user = User(firstName = firstname,
                                lastName = lastname,
                                userName = username,
                                email =  email,
                                password = password,
                                imageData = imageData)

                val exceptionHandler = CoroutineExceptionHandler { _, exception ->
                    // Handle exception here
                    Toast.makeText(this, "USERNAME TAKEN", Toast.LENGTH_SHORT).show()
                }
                CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
                    try {
                        repository.insertUser(user)
                        println("coroutine finished")
                        finish()
                    } catch (e: Exception) {
                        // This will catch errors within the coroutine
                        e.printStackTrace()
                    }
                }

            }


        }
    }





}