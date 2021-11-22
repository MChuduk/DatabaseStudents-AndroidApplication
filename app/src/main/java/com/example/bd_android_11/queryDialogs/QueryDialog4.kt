package com.example.bd_android_11.queryDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.bd_android_11.DatabaseHelper
import com.example.bd_android_11.MainActivity
import com.example.bd_android_11.R
import com.example.bd_android_11.convertDate
import java.util.*

class QueryDialog4(val activity: MainActivity) : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var facultySpinner : Spinner? = null
    private var averageMarkEditText : EditText? = null
    private var greaterRadioButton : RadioButton? = null
    private var startDateEditText : EditText? = null
    private var endDateEditText : EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.query4_dialog, null)
        findViews(view)

        setupSpinner(facultySpinner, "SELECT FACULTY FROM FACULTIES")

        builder.setTitle("Список лучших студентов").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val queryResult : MutableList<String> = mutableListOf()
                val faculty = facultySpinner?.selectedItem.toString()

                val averageMark = averageMarkEditText?.text.toString()
                val startDate = convertDate(startDateEditText?.text.toString())
                val endDate = convertDate(endDateEditText?.text.toString())

                val s = if(greaterRadioButton?.isChecked == true) ">" else "<"

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT STUDENTS.STUDENTNAME, GROUPS.IDGROUP, AVG(PROGRESSES.MARK) 'AVERAGE MARK' " +
                        "FROM FACULTIES JOIN GROUPS ON FACULTIES.IDFACULTY = GROUPS.FACULTY " +
                        "JOIN STUDENTS ON GROUPS.IDGROUP = STUDENTS.IDGROUP " +
                        "JOIN PROGRESSES ON PROGRESSES.IDSTUDENT = STUDENTS.IDSTUDENT " +
                        "WHERE FACULTIES.FACULTY = ? AND EXAMDATE BETWEEN ? AND ? " +
                        "GROUP BY STUDENTS.STUDENTNAME " +
                        "HAVING AVG(PROGRESSES.MARK) $s CAST(? AS REAL)"

                val cursor = db?.rawQuery(queryString, arrayOf(faculty, startDate, endDate, averageMark))

                if(cursor?.moveToFirst() == true) {
                    val studentNameIndex = cursor.getColumnIndex("STUDENTNAME")
                    val idGroupIndex = cursor.getColumnIndex("IDGROUP")
                    val averageMarkIndex = cursor.getColumnIndex("AVERAGE MARK")

                    do {
                        val row = "${cursor.getColumnName(studentNameIndex)}: ${cursor.getString(studentNameIndex)} \n" +
                                "${cursor.getColumnName(idGroupIndex)}: ${cursor.getString(idGroupIndex)} \n" +
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
        facultySpinner = view.findViewById(R.id.facultySpinner)
        averageMarkEditText = view.findViewById(R.id.averageMarkEditText)
        greaterRadioButton = view.findViewById(R.id.greaterThenRadioButton)
        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
    }
}