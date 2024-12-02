package com.example.fit_connect.data.workout

import androidx.room.TypeConverter
import java.util.Base64


class WorkoutConverter {
    //Convert List to String
    @TypeConverter
    fun fromCommentList(commentList: MutableList<Comments>): String {
        return commentList.joinToString(";") {
            val base64Data = Base64.getEncoder().encodeToString(it.imageData)
            "$base64Data|${it.comment}"
        }
    }

    @TypeConverter
    fun toCommentList(commentListString: String): MutableList<Comments> {
        if (commentListString.isEmpty()) return mutableListOf()
        return commentListString.split(";").mapNotNull {
            val parts = it.split("|")
            if (parts.size == 2) {
                val data = Base64.getDecoder().decode(parts[0])
                val text = parts[1]
                Comments(text, data)
            } else null
        }.toMutableList()
    }

    @TypeConverter
    fun fromLongList(longList: MutableList<Long>): String {
        return longList.joinToString(";")
    }

    @TypeConverter
    fun toLongList(longListString: String): MutableList<Long> {
        if (longListString.isEmpty()) return mutableListOf()
        return longListString.split(";").map(String::toLong).toMutableList()
    }
}
