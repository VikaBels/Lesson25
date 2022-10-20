package com.example.lesson24.dataBases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import com.example.lesson24.utils.listComment
import com.example.lesson24.utils.listPost
import com.example.lesson24.utils.listUser

class DatabaseHelper(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Lesson_24.db"
        const val DB_VERSION = 4
    }

    override fun onCreate(db: SQLiteDatabase) {
        for (i in 1..DB_VERSION) {
            migrate(db, i)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        var oldVersionHelper = oldVersion

        while (oldVersionHelper < newVersion) {
            oldVersionHelper += 1
            migrate(db, oldVersionHelper);
        }
    }

    private fun migrate(db: SQLiteDatabase, dbVersion: Int) {
        when (dbVersion) {
            1 -> {
                createTableUser(db)
                createTablePost(db)
                createTableComment(db)
            }
            2 -> {
                addIndexesUserTable(db)
            }
            3 -> {
                changeUserTable(db)
            }
            4 -> {
                changeCommentTable(db)
            }
        }
    }

    private fun createTablePost(db: SQLiteDatabase) {
        executeMethod(
            db, "CREATE TABLE post " +
                    "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "body TEXT NOT NULL," +
                    "rate INTEGER NOT NULL );"
        )

        val statementPost = compileStatementCreator(
            db,
            "INSERT INTO post (user_id, title, body, rate) VALUES(?,?,?,?);"
        )

        listPost().forEach {
            statementPost.apply {
                bindLong(1, it.userId)
                bindString(2, it.title)
                bindString(3, it.body)
                bindLong(4, it.rate)
            }.executeInsert()
        }
    }

    private fun createTableUser(db: SQLiteDatabase) {
        executeMethod(
            db, "CREATE TABLE user" +
                    "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT NOT NULL, " +
                    "last_name TEXT NOT NULL," +
                    "email TEXT NOT NULL );"
        )

        val statementUser = compileStatementCreator(
            db,
            "INSERT INTO user (first_name, last_name, email) VALUES(?,?,?);"
        )

        listUser().forEach {
            statementUser.apply {
                bindString(1, it.firstName)
                bindString(2, it.lastName)
                bindString(3, it.email)
            }.executeInsert()
        }
    }

    private fun createTableComment(db: SQLiteDatabase) {
        executeMethod(
            db, "CREATE TABLE comment " +
                    "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " post_id INTEGER NOT NULL," +
                    " user_id INTEGER NOT NULL," +
                    " text TEXT NOT NULL );"
        )

        val statementComment = compileStatementCreator(
            db,
            "INSERT INTO comment (post_id, user_id, text) VALUES(?,?,?);"
        )

        listComment().forEach {
            statementComment.apply {
                bindLong(1, it.postId)
                bindLong(2, it.userId)
                bindString(3, it.text)
            }.executeInsert()
        }
    }

    private fun addIndexesUserTable(db: SQLiteDatabase) {
        db.compileStatement("CREATE INDEX user_name ON user (first_name, last_name)").execute()
    }

    private fun changeUserTable(db: SQLiteDatabase) {
        executeMethod(
            db,
            "CREATE TABLE _tmp_user" +
                    "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "full_name TEXT NOT NULL, " +
                    "email TEXT NOT NULL );"
        )

        compileStatementCreator(
            db,
            "INSERT INTO _tmp_user (_id, full_name, email) SELECT _id, first_name || ' ' || last_name, email FROM user;"
        ).executeInsert()

        executeUpdateDeleteMethod(db, "DROP TABLE user;")
        executeUpdateDeleteMethod(db, "ALTER TABLE _tmp_user RENAME TO user;")
    }

    private fun changeCommentTable(db: SQLiteDatabase) {
        db.compileStatement("ALTER TABLE comment ADD COLUMN rate INTEGER NOT NULL default 0")
            .executeUpdateDelete()
    }

    private fun executeUpdateDeleteMethod(db: SQLiteDatabase, sqlUpdate: String) {
        db.compileStatement(
            sqlUpdate
        ).executeUpdateDelete()
    }

    private fun executeMethod(db: SQLiteDatabase, sqlCreate: String) {
        db.compileStatement(
            sqlCreate
        ).execute()
    }

    private fun compileStatementCreator(db: SQLiteDatabase, sqlInsert: String): SQLiteStatement {
        return db.compileStatement(sqlInsert)
    }
}