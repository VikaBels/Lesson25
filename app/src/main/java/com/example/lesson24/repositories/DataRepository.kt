package com.example.lesson24.repositories

import android.database.sqlite.SQLiteDatabase
import com.example.lesson24.models.CommentInfo
import com.example.lesson24.models.PostInfo
import com.example.lesson24.models.PostStatistic
import kotlin.collections.ArrayList

class DataRepository(private val db: SQLiteDatabase) {
    companion object {
        private const val POST_ID = "post_id"
        private const val TITLE = "title"
        private const val USER_EMAIL = "user_email"
        private const val BODY = "body"
        private const val FULL_NAME = "full_name"

        private const val EMAIL_COMMENTATOR = "email_commentator"
        private const val TEXT_COMMENT = "text_comment"
        private const val ID_COMMENT = "id_comment"

        private const val POST_TITLE = "post_title"
        private const val COMMENT_COUNT = "comment_count"
        private const val COMMENT_RATE = "comment_rate"
    }

    fun getAllPosts(): ArrayList<PostInfo> {
        val listPost = ArrayList<PostInfo>()

        val cursor = db.rawQuery(
            "SELECT p._id as $POST_ID, p.title as $TITLE, p.body as $BODY, " +
                    "u.email as $USER_EMAIL, u.full_name as $FULL_NAME FROM user u " +
                    "JOIN post p ON u._id = p.user_id " +
                    "ORDER BY $TITLE ASC",
            null
        )

        cursor?.use {
            if (cursor.moveToFirst()) {
                val idPostCursor = cursor.getColumnIndexOrThrow(POST_ID)
                val titleCursor = cursor.getColumnIndexOrThrow(TITLE)
                val userIdCursor = cursor.getColumnIndexOrThrow(USER_EMAIL)
                val bodyCursor = cursor.getColumnIndexOrThrow(BODY)
                val fullNameCursor = cursor.getColumnIndexOrThrow(FULL_NAME)

                do {
                    val post = PostInfo(
                        cursor.getLong(idPostCursor),
                        cursor.getString(titleCursor),
                        cursor.getString(userIdCursor),
                        cursor.getString(bodyCursor),
                        cursor.getString(fullNameCursor),
                    )

                    listPost.add(post)

                } while (cursor.moveToNext())
            }
        }
        return listPost
    }

    fun getAllComment(idCurrentPost: Long?): ArrayList<CommentInfo> {
        val listComment = ArrayList<CommentInfo>()

        val cursor = db.rawQuery(
            "SELECT u.email as $EMAIL_COMMENTATOR," +
                    "c._id as $ID_COMMENT, c.text as $TEXT_COMMENT, c.rate as $COMMENT_RATE FROM comment c " +
                    "JOIN post p ON c.post_id = p._id " +
                    "JOIN user u ON u._id = c.user_id " +
                    "WHERE p._id = ?" +
                    "ORDER BY c._id ASC",
            arrayOf(
                idCurrentPost?.toString()
            )
        )

        cursor?.use {
            if (cursor.moveToFirst()) {
                val idCommentCursor = cursor.getColumnIndexOrThrow(ID_COMMENT)
                val emailCommentatorCursor = cursor.getColumnIndexOrThrow(EMAIL_COMMENTATOR)
                val textCommentCursor = cursor.getColumnIndexOrThrow(TEXT_COMMENT)
                val rateCommentCursor = cursor.getColumnIndexOrThrow(COMMENT_RATE)

                do {
                    val comment = CommentInfo(
                        cursor.getLong(idCommentCursor),
                        cursor.getString(emailCommentatorCursor),
                        cursor.getString(textCommentCursor),
                        cursor.getLong(rateCommentCursor)
                    )

                    listComment.add(comment)

                } while (cursor.moveToNext())
            }
        }
        return listComment
    }

    fun getAllPostsStatistic(): ArrayList<PostStatistic> {
        val listPostStatistic = java.util.ArrayList<PostStatistic>()

        val cursor = db.rawQuery(
            "SELECT p.title AS $POST_TITLE, COUNT(c._id) AS $COMMENT_COUNT, AVG(c.rate) as $COMMENT_RATE " +
                    "FROM comment c " +
                    "JOIN post AS p " +
                    "ON c.post_id = p._id " +
                    "GROUP BY c.post_id " +
                    "ORDER BY $COMMENT_COUNT DESC",
            null
        )

        cursor?.use {
            if (cursor.moveToFirst()) {
                val titlePostCursor = cursor.getColumnIndexOrThrow(POST_TITLE)
                val commentCountCursor = cursor.getColumnIndexOrThrow(COMMENT_COUNT)
                val commentRateCursor = cursor.getColumnIndexOrThrow(COMMENT_RATE)

                do {
                    val post = PostStatistic(
                        cursor.getString(titlePostCursor),
                        cursor.getLong(commentCountCursor),
                        cursor.getLong(commentRateCursor)
                    )

                    listPostStatistic.add(post)

                } while (cursor.moveToNext())
            }
        }
        return listPostStatistic
    }

    fun updateRateComment(id: Long, rate: Long) {
        val statementComment = db.compileStatement("UPDATE comment SET rate = ? WHERE _id = ?")

        statementComment.apply {
            bindLong(1, rate)
            bindLong(2, id)
        }.executeUpdateDelete()
    }
}