package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.content.Context
import android.icu.util.Calendar
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fit_connect.R

class CommentListAdapter (private val context: Context, private var arrayList:ArrayList<String>) : BaseAdapter(){
    private val calendar: Calendar = Calendar.getInstance()
    override fun getCount(): Int{
        return arrayList.size
    }
    override fun getItem(position: Int): String{
        return arrayList[position]
    }
    override fun getItemId(position: Int) : Long{
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val customView: View = View.inflate(context, R.layout.comment_listadapter, null)

        val commentProfileImg : ImageView = customView.findViewById(R.id.comment_profile_img)
        val commentName : TextView = customView.findViewById(R.id.comment_name_txt)
        val comment: TextView = customView.findViewById(R.id.comment_txt)
        val recentComment : TextView = customView.findViewById(R.id.comment_recency)


        commentProfileImg.setImageResource(R.drawable.defaultpacman)
        commentName.text = "Pacman"
        comment.text = arrayList[position]
        recentComment.text = "Just Now"

        return customView
    }


}