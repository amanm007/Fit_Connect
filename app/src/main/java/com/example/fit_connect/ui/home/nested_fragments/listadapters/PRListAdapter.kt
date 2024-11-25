package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fit_connect.R

class PRListAdapter (private val context: Context, private var arrayList:ArrayList<Int>) : BaseAdapter(){
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
        val customView: View = View.inflate(context, R.layout.pr_listadapter, null)

        //Set Image
        val imgView : ImageView = customView.findViewById(R.id.exercise_imgview)
        imgView.setImageResource(R.drawable.ic_launcher_background)

        //Set Text
        val exercise_routine : TextView = customView.findViewById(R.id.exercise_routine_txt)
        exercise_routine.setText(context.getString(R.string.testing_text_routine))

        val exercise_part : TextView = customView.findViewById(R.id.exercise_routine_part_txt)
        exercise_part.setText(context.getString(R.string.testing_routine_part))


        return customView
    }

}