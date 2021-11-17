package com.example.bd_android_11

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.opencsv.CSVReader
import java.io.InputStreamReader
import java.util.*

class DatabaseHelper(val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "STUDENTSDB"
        const val DATABASE_VERSION = 1

        const val TABLE_FACULTY = "FACULTIES"
        const val KEY_FACULTY = "FACULTY"
        const val KEY_FACULTY_ID = "IDFACULTY"
        const val KEY_DEAN = "DEAN"
        const val KEY_OFFICETIMETABLE = "OFFICETIMETABLE"

        const val TABLE_GROUP = "GROUPS"
        const val KEY_GROUP_ID = "IDGROUP"
        const val KEY_COURSE = "COURSE"
        const val KEY_GROUP_NAME = "GROUPNAME"
        const val KEY_HEAD = "HEAD"

        const val TABLE_STUDENT = "STUDENTS"
        const val KEY_STUDENT_ID = "IDSTUDENT"
        const val KEY_STUDENT_NAME = "STUDENTNAME"
        const val KEY_STUDENT_BIRTHDAY ="BIRTHDAY"
        const val KEY_STUDENT_ADDRESS = "ADDRESS"

        const val TABLE_SUBJECT = "SUBJECTS"
        const val KEY_SUBJECT_ID = "IDSUBJECT"
        const val KEY_SUBJECT_NAME = "SUBJECTNAME"

        const val TABLE_PROGRESS = "PROGRESSES"
        const val KEY_EXAMDATE = "EXAMDATE"
        const val KEY_MARK = "MARK"
        const val KEY_TEACHER = "TEACHER"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createTableFaculty(db)
        createTableGroup(db)
        createTableStudent(db)
        createTableSubject(db)
        createTableProgress(db)

        initDataFrom(db, TABLE_FACULTY, "data/faculties.csv")
        initDataFrom(db, TABLE_GROUP, "data/groups.csv")
        initDataFrom(db, TABLE_STUDENT, "data/students.csv")
        initDataFrom(db, TABLE_SUBJECT, "data/subject.csv")
        initDataFrom(db, TABLE_PROGRESS, "data/progress.csv")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_FACULTY")
        db?.execSQL("drop table if exists $TABLE_GROUP")
        db?.execSQL("drop table if exists $TABLE_STUDENT")
        db?.execSQL("drop table if exists $TABLE_SUBJECT")
        db?.execSQL("drop table if exists $TABLE_PROGRESS")

        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    private fun createTableFaculty(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_FACULTY (" +
                "$KEY_FACULTY_ID integer primary key," +
                "$KEY_FACULTY text, " +
                "$KEY_DEAN text, " +
                "$KEY_OFFICETIMETABLE text" +
                ")")
    }

    private fun createTableGroup(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_GROUP (" +
                "$KEY_GROUP_ID integer primary key, " +
                "$KEY_FACULTY integer, " +
                "$KEY_COURSE integer, " +
                "$KEY_GROUP_NAME text, " +
                "$KEY_HEAD integer, " +
                "foreign key($KEY_FACULTY) references $TABLE_FACULTY($KEY_FACULTY_ID)" +
                "on delete cascade on update cascade, " +
                "foreign key($KEY_HEAD) references $TABLE_STUDENT($KEY_STUDENT_ID)" +
                "on update cascade" +
                ")")
    }

    private fun createTableStudent(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_STUDENT (" +
                "$KEY_STUDENT_ID integer primary key," +
                "$KEY_GROUP_ID integer, " +
                "$KEY_STUDENT_NAME text, " +
                "$KEY_STUDENT_BIRTHDAY text, " +
                "$KEY_STUDENT_ADDRESS text, " +
                "foreign key ($KEY_GROUP_ID) references $TABLE_GROUP($KEY_GROUP_ID)" +
                ")")
    }

    private fun createTableSubject(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_SUBJECT (" +
                "$KEY_SUBJECT_ID integer primary key," +
                "$KEY_SUBJECT_NAME text" +
                ")")
    }

    private fun createTableProgress(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_PROGRESS (" +
                "$KEY_STUDENT_ID integer, " +
                "$KEY_SUBJECT_ID integer, " +
                "$KEY_EXAMDATE text, " +
                "$KEY_MARK integer, " +
                "$KEY_TEACHER text" +
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
    }
}