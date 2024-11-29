package com.example.fit_connect.ui.home.nested_fragments.following_activities

import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fit_connect.PhotoUtil
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.Following
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FindFollowerActivity : AppCompatActivity() {

    private val user_id = "USER_ID"
    //Buttons
    private lateinit var backbtn : ImageButton
    private lateinit var searchbtn : Button
    private lateinit var followbtn : Button

    //Edit Text
    private lateinit var followingsearchedit : EditText

    //User Profile
    private lateinit var followingImage : ImageView
    private lateinit var followinguserNameText : TextView
    private lateinit var followingNameText : TextView

    private var userId : Long = 0
    private var friendId : Long = -1
    private var followingId : Long = -1
    private var following : Following? = null
    private var username_search : String = ""

    //check if user isFollowing
    private var isFollowing = false

    //Repository
    private lateinit var database : FitConnectDatabase
    private lateinit var userDatabaseDao : UserDao
    private lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_follower)

        //Retrieve UserID
        userId = intent.getLongExtra(user_id, 0)

        database = FitConnectDatabase.getInstance(this)
        userDatabaseDao = database.userDao()
        repository = UserRepository(userDatabaseDao)

        //Initalize all view items
        backbtn = findViewById(R.id.find_follower_back_btn)
        searchbtn = findViewById(R.id.find_follower_search_btn)
        followbtn = findViewById(R.id.find_follower_follow_btn)

        followingsearchedit = findViewById(R.id.find_follower_search_edit)

        followingImage = findViewById(R.id.find_follower_image)
        followinguserNameText = findViewById(R.id.find_follower_username_txt)
        followingNameText = findViewById(R.id.find_Follower_name_txt)

        setBackButton()
        setSearchBtn()
        setFollowBtn()

    }
    private fun setBackButton(){
        backbtn.setOnClickListener(){
            finish()
        }
    }

    private fun setSearchBtn(){
        searchbtn.setOnClickListener(){

            username_search = followingsearchedit.text.toString()


            //search_user based on username
            val liveuser = repository.getUserByUserName(username_search)
            liveuser.observe(this, Observer{ user->

                liveuser.removeObservers(this)

                //User Found and is not the current user
                if(user != null){


                    if(user.userId == userId){
                        Toast.makeText(this, "SAME USER", Toast.LENGTH_SHORT).show()
                    }

                    else{
                        //Set following user profile
                        if(user.imageData == null){
                            followingImage.setImageResource(R.drawable.ic_launcher_background)
                        }
                        else{
                            followingImage.setImageBitmap(
                                PhotoUtil.byteArrayToBitmap(user.imageData!!)
                            )
                        }

                        followinguserNameText.text = "UserName: " + user.userName
                        followingNameText.text = "Name: " + user.firstName + " " + user.lastName

                        friendId = user.userId!!
                        //Check if the user is already following
                        checkIfFollowing()

                    }

                }

                //User Not found
                else{
                    followinguserNameText.text = ""
                    followingNameText.text = ""
                    followingImage.setImageDrawable(null)
                    followbtn.text = getString(R.string.follow)
                    followbtn.visibility = View.GONE
                }

            })
        }
    }

    private fun setFollowBtn(){
        followbtn.setOnClickListener(){
            if(isFollowing){
                isFollowing = false
                CoroutineScope(IO).launch{
                    if(following != null) {
                        repository.deleteFollowing(following!!)
                        println("DELETED")
                    }
                    following = null
                }

                followbtn.text = getString(R.string.follow)
            }
            else{
                isFollowing = true

                //Add Follower to list
                CoroutineScope(IO).launch{
                    val insertfollowing = Following(userId = userId, friendId = friendId)
                    followingId = repository.insertFollowing(insertfollowing)
                    following = Following(followingId = followingId, userId = userId, friendId = friendId)
                    println("ADDED")
                }
                followbtn.text = getString(R.string.unfollow)

            }
        }
    }

    private fun checkIfFollowing(){
        val livefollowers = repository.getUserWithFollowing(userId)
        livefollowers.observe(this){ userAndFollowers->
            livefollowers.removeObservers(this)

            //Find if you are already following this person

            var isFollowerFound = false

            for(followers in userAndFollowers.followings){
                if(followers.friendId == friendId){
                    isFollowerFound = true
                    following = followers
                    followingId = followers.followingId!!
                    break
                }

            }
            if(isFollowerFound){
                followbtn.text = getString(R.string.unfollow)
                isFollowing = true
            }
            else{
                followbtn.text = getString(R.string.follow)
                isFollowing = false
            }
            followbtn.visibility = View.VISIBLE
        }
    }
}