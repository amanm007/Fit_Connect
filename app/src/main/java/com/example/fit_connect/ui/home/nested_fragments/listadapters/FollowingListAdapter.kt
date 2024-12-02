package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.anychart.core.annotations.Line
import com.example.fit_connect.PhotoUtil
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.Following
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.ui.home.nested_fragments.following_activities.CreateCommentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FollowingListAdapter(private val context: Context, private var followingList:List<Following>
                            , private var userId : Long) : BaseAdapter(){
    private val ACTIVITY_REQUEST_CODE = 1000000
    private val database = FitConnectDatabase.getInstance(context)
    private val userDatabaseDao = database.userDao()
    private val workutDatabaseDao = database.workoutDao()

    private val repository = UserRepository(userDatabaseDao)
    private val workoutRepository = WorkoutRepository(workutDatabaseDao)

    override fun getCount(): Int{
        return followingList.size
    }
    override fun getItem(position: Int): Following{
        return followingList[position]
    }
    override fun getItemId(position: Int) : Long{
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val customView: View = View.inflate(context, R.layout.following_listadapter, null)

        //Get Image and Text User Information
        val followProfileImg : ImageView = customView.findViewById(R.id.following_img)
        val followNameTxt : TextView = customView.findViewById(R.id.following_name_txt)

        val followinguser = repository.getUser(followingList[position].friendId)
        followinguser.observe(context as LifecycleOwner) { user ->
            followProfileImg.setImageResource(R.drawable.ic_launcher_background)
            if (user?.imageData != null) {
                val bitmap = PhotoUtil.byteArrayToBitmap(user?.imageData!!)
                followProfileImg.setImageBitmap(bitmap)
            }
            followNameTxt.text = user?.firstName + " " + user?.lastName

        }

        //Get Layout
        val workoutLayout = customView.findViewById<LinearLayout>(R.id.following_workout_data_layout)

        //Get Workout Data including likes and comments
        val followingWorkout = repository.getUserWithSimpleWorkouts(followingList[position].friendId)
        val followWorkoutDayTxt : TextView = customView.findViewById(R.id.workout_day_txt)
        val followTimeTxt : TextView = customView.findViewById(R.id.following_time_txt)
        val followVolumeTxt : TextView = customView.findViewById(R.id.following_volume_txt)
        val followRecordsTxt : TextView = customView.findViewById(R.id.following_records_txt)
        val followWorkoutList : ListView = customView.findViewById(R.id.following_workout_listview)

        val followNumberLikesTxt :TextView = customView.findViewById(R.id.number_likes_txt)
        val followNumberCommentTxt:TextView = customView.findViewById(R.id.number_comments_txt)

        //Setup Img button (For Later)
        val followLikebtn : ImageButton = customView.findViewById(R.id.like_imgbtn)
        val followCommentbtn : ImageButton = customView.findViewById(R.id.comment_imgbtn)

        //Last Comment Made
        val followLastCommentImg : ImageView = customView.findViewById(R.id.following_comment_img)
        val followCommentTxt : TextView = customView.findViewById(R.id.following_comment_txt)

        //Get Workout list from Dao
        followingWorkout.observe(context as LifecycleOwner) { simpleWorkouts ->
            val workoutList = simpleWorkouts.workouts

            //Only fill information if an entry exists in the workout
            if (workoutList.isNotEmpty()) {
                //Set Layout Visibility to True
                workoutLayout.visibility = View.VISIBLE

                //Get Most Recent Workout
                val workout = workoutList[workoutList.size - 1]
                followTimeTxt.text = workout.duration.toString()
                followVolumeTxt.text = "0 lbs"
                followRecordsTxt.text = "0"
                followNumberLikesTxt.text = workout.likes.toString()  + " likes"
                followNumberCommentTxt.text = workout.comments.size.toString() + " comments"

                //Setup btn
                //Like
                setupLikeBtn(followLikebtn, context, position, workout)
                setupCommentBtn(followCommentbtn, context, position, workout)

                followLastCommentImg.setImageResource(R.drawable.defaultpacman)
                followCommentTxt.text = "PACMAN2:    GIT GUD"
            }
            else{
                println("Bye")
            }

        }

        //Set Following Workout List (Use another list adapter)

        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        arrayList.add(1)
        arrayList.add(2)
        arrayList.add(3)

        val arrayAdapter = WorkoutListAdapter(context, arrayList)
        followWorkoutList.adapter = arrayAdapter
        followWorkoutList.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            (80 * arrayList.size * context.resources.displayMetrics.density).toInt()
        )

        return customView
        }

    private fun setupLikeBtn(btn : ImageButton, context : Context, position : Int, workout : Workout){
        var isLiked = false
        btn.setImageResource(R.drawable.ic_thumb_up_off)
        val userIdIndex = workout.likedList.indexOf(userId)

        if(userIdIndex != -1){
            btn.setImageResource(R.drawable.ic_thumb_up_on)
            isLiked = true
        }

        btn.setOnClickListener {
            if(isLiked) {
                btn.setImageResource(R.drawable.ic_thumb_up_off)

                //Remove user from list
                val index = workout.likedList.indexOf(userId)
                workout.likedList.removeAt(index)
                workout.likes--
                isLiked = false
            }
            else{
                //Add User to List
                btn.setImageResource(R.drawable.ic_thumb_up_on)
                workout.likedList.add(userId)
                workout.likes++
                isLiked = true
            }
            updateWorkout(workout)
        }
    }
    private fun setupCommentBtn(btn : ImageButton, context: Context, position: Int, workout: Workout){
        btn.setOnClickListener(){
            val intent = Intent(context, CreateCommentActivity::class.java)

            //Put Extra Vals into intent (maybe just the id?)

            if(context is Activity){
                context.startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
            }
        }
    }


    fun replace(newFollowing : List<Following>){
        followingList = newFollowing
    }

    fun updateWorkout(updateWorkout : Workout){
        CoroutineScope(IO).launch {
            workoutRepository.updateWorkout(updateWorkout)
        }
    }
}