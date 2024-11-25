package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R

class WorkoutListAdapter(private val context: Context, private var arrayList:ArrayList<Int>) : BaseAdapter(){
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
        val customView: View = View.inflate(context, R.layout.following_workout_listadapter, null)

        val workoutImg : ImageView = customView.findViewById(R.id.following_workout_img)
        workoutImg.setImageResource(R.drawable.ic_launcher_background)

        val workoutDescriptionTxt : TextView = customView.findViewById(R.id.following_workout_description_txt)
        workoutDescriptionTxt.text = "3 sets Pacman workout"

        return customView
    }
}