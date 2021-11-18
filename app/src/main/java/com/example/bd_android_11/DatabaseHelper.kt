package com.example.bd_android_11

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.opencsv.CSVReader
import java.io.InputStreamReader

class DatabaseHelper(val context: Context?) :
    SQLiteOpenHelper(context, "STUDENTSDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        createTableFaculty(db)
        createTableGroup(db)
        createTableStudent(db)
        createTableSubject(db)
        createTableProgress(db)

        initDataFrom(db, "FACULTIES", "data/faculties.csv")
        initDataFrom(db, "GROUPS", "data/groups.csv")
        initDataFrom(db, "STUDENTS", "data/students.csv")
        initDataFrom(db, "SUBJECTS", "data/subject.csv")
        initDataFrom(db, "PROGRESSES", "data/progress.csv")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists FACULTIES")
        db?.execSQL("drop table if exists GROUPS")
        db?.execSQL("drop table if exists STUDENTS")
        db?.execSQL("drop table if exists SUBJECTS")
        db?.execSQL("drop table if exists PROGRESSES")

        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    private fun createTableFaculty(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE FACULTIES (" +
                "IDFACULTY INTEGER PRIMARY KEY," +
                "FACULTY TEXT, " +
                "DEAN TEXT, " +
                "OFFICETIMETABLE TEXT" +
                ")")
    }

    private fun createTableGroup(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE GROUPS (" +
                "IDGROUP INTEGER PRIMARY KEY, " +
                "FACULTY INTEGER, " +
                "COURSE INTEGER, " +
                "GROUPNAME TEXT, " +
                "HEAD INTEGER, " +
                "FOREIGN KEY(FACULTY) REFERENCES FACULTIES(IDFACULTY)" +
                "ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY(HEAD) REFERENCES STUDENTS(IDSTUDENT)" +
                "ON UPDATE CASCADE" +
                ")")
    }

    private fun createTableStudent(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE STUDENTS (" +
                "IDSTUDENT INTEGER PRIMARY KEY," +
                "IDGROUP INTEGER, " +
                "STUDENTNAME TEXT, " +
                "BIRTHDAY TEXT, " +
                "ADDRESS TEXT, " +
                "FOREIGN KEY (IDGROUP) REFERENCES GROUPS(IDGROUP)" +
                ")")
    }

    private fun createTableSubject(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE SUBJECTS (" +
                "IDSUBJECT INTEGER PRIMARY KEY," +
                "SUBJECTNAME TEXT" +
                ")")
    }

    private fun createTableProgress(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE PROGRESSES (" +
                "IDSTUDENT INTEGER, " +
                "IDSUBJECT INTEGER, " +
                "EXAMDATE TEXT, " +
                "MARK INTEGER, " +
                "TEACHER TEXT" +
                ")")
    }

    private fun initDataFrom(db: SQLiteDatabase?, table : String, file : String) {
        val assetManager = context?.assets
        val streamReader = InputStreamReader(assetManager?.open(file), "UTF-8")
        val cursor = db?.query(table, null, null, null, null, null, null)

        val reader = CSVReader(streamReader)
        var line = reader.readNext();
        while (line != null) {
            val values = ContentValues()

            for((index, column) in cursor?.columnNames?.withIndex()!!){
                if(index < line.size)
                values.put(column, line[index])
            }
            db.insert(table, null, values)
            line = reader.readNext()
        }
        cursor?.close()
    }
}