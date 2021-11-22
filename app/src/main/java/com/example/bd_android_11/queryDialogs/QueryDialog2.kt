package com.example.bd_android_11.queryDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.bd_android_11.DatabaseHelper
import com.example.bd_android_11.MainActivity
import com.example.bd_android_11.R
import com.example.bd_android_11.convertDate

class QueryDialog2(val activity: MainActivity) : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var studentSpinner : Spinner? = null
    private var subjectSpinner : Spinner? = null
    private var startDateEditText : EditText? = null
    private var endDateEditText : EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.query2_dialog, null)
        findViews(view)

        setupSpinner(studentSpinner, "SELECT STUDENTNAME FROM STUDENTS")
        setupSpinner(subjectSpinner, "SELECT SUBJECTNAME FROM SUBJECTS")

        builder.setTitle("Средняя оценка для студента").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val queryResult : MutableList<String> = mutableListOf()
                val student = studentSpinner?.selectedItem.toString()
                val subject = subjectSpinner?.selectedItem.toString()

                val startDate = convertDate(startDateEditText?.text.toString())
                val endDate = convertDate(endDateEditText?.text.toString())

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT STUDENTS.STUDENTNAME, SUBJECTS.SUBJECTNAME, AVG(PROGRESSES.MARK) 'AVERAGE MARK' " +
                        "FROM STUDENTS JOIN PROGRESSES ON STUDENTS.IDSTUDENT = PROGRESSES.IDSTUDENT " +
                        "JOIN SUBJECTS ON PROGRESSES.IDSUBJECT = SUBJECTS.IDSUBJECT " +
                        "WHERE STUDENTS.STUDENTNAME = ? AND SUBJECTS.SUBJECTNAME = ? " +
                        "AND PROGRESSES.EXAMDATE BETWEEN ? AND ? " +
                        "GROUP BY STUDENTS.STUDENTNAME"

                val cursor = db?.rawQuery(queryString, arrayOf(student, subject, startDate, endDate))

                if(cursor?.moveToFirst() == true) {
                    val studentNameIndex = cursor.getColumnIndex("STUDENTNAME")
                    val subjectNameIndex = cursor.getColumnIndex("SUBJECTNAME")
                    val averageMarkIndex = cursor.getColumnIndex("AVERAGE MARK")

                    do {
                        val row = "${cursor.getColumnName(studentNameIndex)}: ${cursor.getString(studentNameIndex)} \n" +
                                "${cursor.getColumnName(subjectNameIndex)}: ${cursor.getString(subjectNameIndex)} \n" +
                                "${cursor.getColumnName(averageMarkIndex)}: ${cursor.getString(averageMarkIndex)}"
                        queryResult.add(row)
                    } while (cursor.moveToNext())
                }
                cursor?.close()
                activity.setQueryResult(queryResult)
            }
        }
        return builder.create()
    }

    private fun setupSpinner(spinner : Spinner?, query : String) {
        val db = dbHelper?.readableDatabase
        val values = mutableListOf<String>()

        val cursor = db?.rawQuery(query, null);

        if(cursor?.moveToFirst() == true) {
            do {
                val value = cursor.getString(0)
                values.add(value)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        spinner?.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, values)
    }

    private fun findViews(view : View) {
        studentSpinner = view.findViewById(R.id.facultySpinner)
        subjectSpinner = view.findViewById(R.id.subjectSpinner)
        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
    }
}