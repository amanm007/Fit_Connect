package com.example.fit_connect.ui.home.nested_fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.fit_connect.R
import com.example.fit_connect.databinding.ActivityCreateCommentBinding
import com.example.fit_connect.databinding.ActivityMainBinding
import com.example.fit_connect.ui.home.nested_fragments.listadapters.CommentListAdapter

class CreateCommentActivity: AppCompatActivity() {

    private lateinit var backbtn : ImageButton
    private lateinit var postbtn : Button
    private lateinit var commentTxt : EditText
    private lateinit var commentListView : ListView
    private var isNewComment = false
    private lateinit var commentList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)

        backbtn = findViewById(R.id.comment_back_btn)
        postbtn = findViewById(R.id.comment_post_btn)
        commentTxt = findViewById(R.id.post_comment_edit)
        commentListView = findViewById(R.id.comment_listview)


        commentList = ArrayList<String>()
        setBackBtn()
        setPostBtn()
        setCommentList()

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
            isNewComment = true
            commentList.add(commentTxt.text.toString())
            setCommentList()
            commentTxt.setText("")

        }

    }

    private fun setCommentList(){
        val arrayAdapter = CommentListAdapter(this, commentList)
        commentListView.adapter = arrayAdapter
    }

}