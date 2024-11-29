package com.example.fit_connect

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.databinding.ActivityMainBinding
import com.example.fit_connect.ui.UserActivity

class MainActivity : AppCompatActivity() {
    private val user_id = "USER_ID"
    private lateinit var binding: ActivityMainBinding

    //Buttons
    private lateinit var loginBtn : Button
    private lateinit var closeBtn : Button
    private lateinit var createAccountBtn : Button

    //Login Info
    private lateinit var usernameEditText : EditText
    private lateinit var passwordEditText : EditText

    //Login String values
    private var username : String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Buttons and EditViews
        usernameEditText = findViewById(R.id.login_username_edittxt)
        passwordEditText = findViewById(R.id.login_password_edittxt)

        closeBtn = findViewById(R.id.close_login_btn)
        loginBtn = findViewById(R.id.login_login_btn)
        createAccountBtn = findViewById(R.id.login_create_account_btn)

        setupCloseButton()
        setupLoginButton()
        setupCreateAccountButton()
    }

    private fun setupCreateAccountButton() {
        createAccountBtn.setOnClickListener(){
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLoginButton() {
        loginBtn.setOnClickListener(){
            //Find User Check
            var isUserFound = false

            //Get String
            username = usernameEditText.text.toString()
            password = passwordEditText.text.toString()

            //Get Database
            val database = FitConnectDatabase.getInstance(this)
            val userDatabaseDao = database.userDao()
            val repository = UserRepository(userDatabaseDao)

            val userLiveData = repository.getUserByUserName(username)


            userLiveData.observe(this, Observer{
                user ->
                userLiveData.removeObservers(this@MainActivity)
                if(user != null){
                    if(user.password == password){


                        val intent = Intent(this,  UserActivity::class.java)
                        intent.putExtra(user_id, user.userId)
                        startActivity(intent)

                        username = ""
                        password = ""
                        usernameEditText.setText("")
                        passwordEditText.setText("")

                    }
                    else{
                        Toast.makeText(this, "INCORRECT USERNAME OR PASSWORD", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"INCORRECT USERNAME OR PASSWORD", Toast.LENGTH_SHORT).show()
                }
            })


        }
    }

    private fun setupCloseButton() {
        closeBtn.setOnClickListener(){
            finish()
        }
    }





}