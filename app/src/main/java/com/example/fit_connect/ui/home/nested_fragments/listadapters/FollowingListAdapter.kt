package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.fit_connect.R
import com.example.fit_connect.ui.home.nested_fragments.CreateCommentActivity

class FollowingListAdapter(private val context: Context, private var arrayList:ArrayList<Int>) : BaseAdapter(){
    private val ACTIVITY_REQUEST_CODE = 1000000
    override fun getCount(): Int{
        return arrayList.size
    }
    override fun getItem(position: Int): Int{
        return arrayList[position]
    }
    override fun getItemId(position: Int) : Long{
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val customView: View = View.inflate(context, R.layout.following_listadapter, null)

        //Set Image
        val followProfileImg : ImageView = customView.findViewById(R.id.following_img)
        followProfileImg.setImageResource(R.drawable.defaultpacman)

        //Set Texts
        val followNameTxt : TextView = customView.findViewById(R.id.following_name_txt)
        followNameTxt.text = "PACMAN"

        val followWorkoutDayTxt : TextView = customView.findViewById(R.id.workout_day_txt)

        val followTimeTxt : TextView = customView.findViewById(R.id.following_time_txt)
        followTimeTxt.text = "0h"

        val followVolumeTxt : TextView = customView.findViewById(R.id.following_volume_txt)
        followVolumeTxt.text = "0 lbs"

        val followRecordsTxt : TextView = customView.findViewById(R.id.following_records_txt)
        followRecordsTxt.text = "0"

        //Set Following Workout List (Use another list adapter)
        val followWorkoutList : ListView = customView.findViewById(R.id.following_workout_listview)

        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        arrayList.add(1)
        arrayList.add(2)
        arrayList.add(3)

        val arrayAdapter = WorkoutListAdapter(context, arrayList)
        followWorkoutList.adapter = arrayAdapter



        //Likes and Comment
        val followNumberLikesTxt :TextView = customView.findViewById(R.id.number_likes_txt)
        val followNumberCommentTxt:TextView = customView.findViewById(R.id.number_comments_txt)


        //Setup Img button (For Later)
        val followLikebtn : ImageButton = customView.findViewById(R.id.like_imgbtn)
        val followCommentbtn : ImageButton = customView.findViewById(R.id.comment_imgbtn)

        //Last Comment Made
        val followLastCommentImg : ImageView = customView.findViewById(R.id.following_comment_img)
        val followCommentTxt : TextView = customView.findViewById(R.id.following_comment_txt)

        followLastCommentImg.setImageResource(R.drawable.defaultpacman)
        followCommentTxt.text = "PACMAN2:    GIT GUD"

        //Like
        var isLiked : Boolean = false
        followLikebtn.setOnClickListener {
            if(isLiked) {
                followLikebtn.setImageResource(R.drawable.ic_thumb_up_off)
                isLiked = false
            }
            else{
                followLikebtn.setImageResource(R.drawable.ic_thumb_up_on)
                isLiked = true
            }
        }
        followCommentbtn.setOnClickListener {
            startCommentActivity()
        }


        return customView
        }
    private fun startCommentActivity(){
        val intent = Intent(context, CreateCommentActivity::class.java)

        //Put Extra Vals into intent (maybe just the id?)
        if(context is Activity){
            context.startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
        }

    }
}