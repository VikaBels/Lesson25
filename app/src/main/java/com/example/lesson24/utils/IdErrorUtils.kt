package com.example.lesson24.utils

import android.database.sqlite.SQLiteException
import com.example.lesson24.R
import java.io.IOException
import java.lang.Exception

fun getIdError(exception: Exception): Int {
    val idError = when (exception) {
        is IOException -> {
            R.string.error_io
        }
        is SQLiteException -> {
            R.string.error_sqlite
        }
        else -> {
            R.string.error_unknown
        }
    }
    return idError
}