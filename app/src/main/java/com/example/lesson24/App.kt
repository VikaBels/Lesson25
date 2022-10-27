package com.example.lesson24

import android.app.Application
import com.example.lesson24.databases.DatabaseHelper
import com.example.lesson24.repositories.DataRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        self = this

        dbOpenHelper = DatabaseHelper(this)
        dataRepository = DataRepository(dbOpenHelper.writableDatabase)
    }

    companion object {
        private lateinit var self: App
        private lateinit var dbOpenHelper: DatabaseHelper
        private lateinit var dataRepository: DataRepository

        fun getInstance(): App {
            return self
        }

        fun getDataRepository(): DataRepository {
            return dataRepository
        }
    }
}