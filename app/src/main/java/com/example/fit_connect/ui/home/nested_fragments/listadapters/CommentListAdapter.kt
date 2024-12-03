package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.content.Context
import android.icu.util.Calendar
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fit_connect.PhotoUtil
import com.example.fit_connect.R
import com.example.fit_connect.data.workout.Comments

class CommentListAdapter (private val context: Context, private var arrayList:MutableList<Comments>) : BaseAdapter(){
    private val calendar: Calendar = Calendar.getInstance()
    override fun getCount(): Int{
        return arrayList.size
    }
    override fun getItem(position: Int): Comments{
        return arrayList[position]
    }
    override fun getItemId(position: Int) : Long{
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val customView: View = View.inflate(context, R.layout.comment_listadapter, null)

        val commentProfileImg : ImageView = customView.findViewById(R.id.comment_profile_img)
        val comment: TextView = customView.findViewById(R.id.comment_txt)

        if(arrayList[position].imageData.size == 0) {
            commentProfileImg.setImageResource(R.drawable.ic_launcher_background)
        }
        else{
            commentProfileImg.setImageBitmap(
                PhotoUtil.byteArrayToBitmap(arrayList[position].imageData!!)
            )
        }

        comment.text = arrayList[position].comment

        return customView
    }
    fun replace(comments : MutableList<Comments>){
        arrayList = comments
    }

}