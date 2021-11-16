package com.example.bd_android_11

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "STUDENTSDB"
        const val DATABASE_VERSION = 1

        const val TABLE_FACULTY = "FACULTY"
        const val KEY_FACULTY = "_FACULTY"
        const val KEY_FACULTY_ID = "_IDFACULTY"
        const val KEY_DEAN = "_DEAN"
        const val KEY_OFFICETIMETABLE = "_OFFICETIMETABLE"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createTableFaculty(db)
        createTableGroup(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_FACULTY")

        onCreate(db)
    }

    private fun createTableFaculty(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_FACULTY (" +
                "$KEY_FACULTY_ID integer primary key," +
                "$KEY_FACULTY text, "+
                "$KEY_DEAN text, " +
                "$KEY_OFFICETIMETABLE text" +
                ")")
    }

    private fun createTableGroup(db: SQLiteDatabase?) {
        
    }
}