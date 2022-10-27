package com.example.lesson24

import android.app.Application
import com.example.lesson24.databases.DatabaseHelper
import com.example.lesson24.repositories.DataRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val dbOpenHelper = DatabaseHelper(this)

        dataRepository = DataRepository(dbOpenHelper.writableDatabase)
    }

    companion object {
        private lateinit var dataRepository: DataRepository

        fun getDataRepository(): DataRepository {
            return dataRepository
        }
    }
}