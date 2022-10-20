package com.example.lesson24

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.example.lesson24.dataBases.DatabaseHelper
import com.example.lesson24.repositories.DataRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        self = this

        dbOpenHelper = DatabaseHelper(this)
        db = dbOpenHelper.writableDatabase
    }

    companion object {
        private lateinit var self: App
        private lateinit var dbOpenHelper: DatabaseHelper
        private lateinit var db: SQLiteDatabase

        fun getInstance(): App {
            return self
        }

        fun getDataRepository(): DataRepository {
            return DataRepository(db)
        }
    }
}