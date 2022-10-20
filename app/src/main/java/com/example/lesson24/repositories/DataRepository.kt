package com.example.lesson24.repositories

import android.database.sqlite.SQLiteDatabase
import bolts.CancellationToken
import bolts.Task
import com.example.lesson24.models.CommentInfo
import com.example.lesson24.models.PostInfo
import com.example.lesson24.models.PostStatistic

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

    private val statementComment = db.compileStatement("UPDATE comment SET rate = ? WHERE _id = ?")

    private fun getAllPosts(): List<PostInfo> {
        val listPost = mutableListOf<PostInfo>()

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

    private fun getAllComments(idCurrentPost: Long): List<CommentInfo> {
        val listComment = mutableListOf<CommentInfo>()

        val cursor = db.rawQuery(
            "SELECT u.email as $EMAIL_COMMENTATOR," +
                    "c._id as $ID_COMMENT, c.text as $TEXT_COMMENT, c.rate as $COMMENT_RATE FROM comment c " +
                    "JOIN post p ON c.post_id = p._id " +
                    "JOIN user u ON u._id = c.user_id " +
                    "WHERE p._id = ?" +
                    "ORDER BY c._id ASC",
            arrayOf(
                idCurrentPost.toString()
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

    private fun getAllPostsStatistic(): List<PostStatistic> {
        val listPostStatistic = mutableListOf<PostStatistic>()

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

    private fun updateRateComment(id: Long, rate: Long) {
        statementComment.apply {
            bindLong(1, rate)
            bindLong(2, id)
        }.executeUpdateDelete()
    }
    
    fun getAllPostsTask(
        cancellationToken: CancellationToken,
    ): Task<List<PostInfo>> {
        return Task
            .callInBackground({
                getAllPosts()
            }, cancellationToken)
    }

    fun getAllCommentTask(
        cancellationToken: CancellationToken,
        idCurrentPost: Long
    ): Task<List<CommentInfo>> {
        return Task
            .callInBackground({
                getAllComments(idCurrentPost)
            }, cancellationToken)
    }

    fun getAllPostStatisticTask(
        cancellationToken: CancellationToken,
    ): Task<List<PostStatistic>> {
        return Task
            .callInBackground({
                getAllPostsStatistic()
            }, cancellationToken)
    }

    fun updateCurrentCommentTask(
        cancellationToken: CancellationToken,
        idComment: Long,
        commentRate: Long
    ): Task<Unit> {
        return Task.callInBackground({
            updateRateComment(idComment, commentRate)
        }, cancellationToken)
    }
}