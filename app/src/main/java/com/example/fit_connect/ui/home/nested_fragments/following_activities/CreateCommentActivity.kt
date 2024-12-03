package com.example.fit_connect.ui.home.nested_fragments.following_activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.example.fit_connect.PhotoUtil
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.Comments
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutDao
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.ui.home.nested_fragments.listadapters.CommentListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class CreateCommentActivity: AppCompatActivity() {

    //UI Views
    private lateinit var backbtn : ImageButton
    private lateinit var postbtn : Button
    private lateinit var commentTxt : EditText
    private lateinit var commentListView : ListView
    private lateinit var followingCommentProfile : TextView
    private var isNewComment = false
    private lateinit var commentList : MutableList<Comments>

    //Intent
    private val workout_id_string = "WORKOUT_ID"
    private val user_id_string = "USER_ID"
    private var workoutId : Long = 0
    private var workoutData : Workout? = null
    private var userId : Long = 0
    private var username : String = ""
    private var imageData : ByteArray = ByteArray(0)

    //Repository
    private lateinit var database : FitConnectDatabase
    private lateinit var userDatabaseDao : UserDao
    private lateinit var workoutDatabaseDao : WorkoutDao
    private lateinit var userRepository: UserRepository
    private lateinit var workoutRepository: WorkoutRepository

    //Array
    private lateinit var arrayAdapter : CommentListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)

        backbtn = findViewById(R.id.comment_back_btn)
        postbtn = findViewById(R.id.comment_post_btn)
        commentTxt = findViewById(R.id.post_comment_edit)
        commentListView = findViewById(R.id.comment_listview)
        followingCommentProfile = findViewById(R.id.comment_following_name_txt)

        //Repository
        database = FitConnectDatabase.getInstance(this)
        userDatabaseDao = database.userDao()
        workoutDatabaseDao = database.workoutDao()
        userRepository = UserRepository(userDatabaseDao)
        workoutRepository = WorkoutRepository(workoutDatabaseDao)

        workoutId = intent.getLongExtra(workout_id_string, 0)
        userId = intent.getLongExtra(user_id_string, 0)

        commentList = mutableListOf()
        setBackBtn()
        setPostBtn()
        initalizeList()
        setCommentList()
        observeUser()

    }
    private fun observeUser(){

        val userLiveData = userRepository.getUser(userId)
        userLiveData.observe(this as LifecycleOwner){
            user ->
            if(user != null) {
                username = user!!.userName

                if(user.imageData != null){
                    imageData = user.imageData!!
                }
            }
        }
    }


    private fun setBackBtn(){
        backbtn.setOnClickListener(){
            val resultIntent = Intent()
            resultIntent.putExtra("CHANGES_MADE", isNewComment)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
    private fun setPostBtn(){
        postbtn.setOnClickListener(){

            val comment = Comments(username + ": " + commentTxt.text, imageData)
            commentList.add(comment)

            //WorkoutData Add
            if(workoutData != null) {
                workoutData!!.comments = commentList
                CoroutineScope(IO).launch{
                    workoutRepository.updateWorkout(workoutData!!)
                }

            }
            commentTxt.setText("")

        }

    }
    private fun initalizeList(){
        arrayAdapter = CommentListAdapter(this, commentList)
        commentListView.adapter = arrayAdapter
    }


    private fun setCommentList(){

        val workoutLiveData = workoutRepository.getWorkout(workoutId)

        workoutLiveData.observe(this as LifecycleOwner){
            workout ->
            if(workout != null){
                workoutData = workout
                commentList = workout.comments
                arrayAdapter.replace(commentList)
                arrayAdapter.notifyDataSetChanged()
            }
        }
    }
}