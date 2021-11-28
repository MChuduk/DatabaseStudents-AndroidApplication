package com.example.bd_android_11.queryDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.bd_android_11.DatabaseHelper
import com.example.bd_android_11.MainActivity
import com.example.bd_android_11.R
import com.example.bd_android_11.datePickers.*

class QueryDialog1(val activity: MainActivity) : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var groupSpinner : Spinner? = null
    private var subjectSpinner : Spinner? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)
        
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.query1_dialog, null)
        findViews(view)

        setupSpinner(groupSpinner, "SELECT GROUPNAME FROM GROUPS")
        setupSpinner(subjectSpinner, "SELECT SUBJECTNAME FROM SUBJECTS")

        builder.setTitle("Средняя оценка для студентов из группы").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val queryResult : MutableList<String> = mutableListOf()
                val group = groupSpinner?.selectedItem.toString()
                val subject = subjectSpinner?.selectedItem.toString()

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT * FROM GROUP_AVERAGE_MARK_VIEW " +
                        "WHERE GROUPNAME = ? AND SUBJECTNAME = ?"

                val cursor = db?.rawQuery(queryString, arrayOf(group, subject))

                if(cursor?.moveToFirst() == true) {
                    val idStudentIndex = cursor.getColumnIndex("IDSTUDENT")
                    val idGroupIndex = cursor.getColumnIndex("IDGROUP")
                    val studentNameIndex = cursor.getColumnIndex("STUDENTNAME")
                    val subjectNameIndex = cursor.getColumnIndex("AVERAGE MARK")

                    do {
                        val row = "${cursor.getColumnName(idStudentIndex)}: ${cursor.getString(idStudentIndex)} \n" +
                                "${cursor.getColumnName(idGroupIndex)}: ${cursor.getString(idGroupIndex)} \n" +
                                "${cursor.getColumnName(studentNameIndex)}: ${cursor.getString(studentNameIndex)} \n" +
                                "${cursor.getColumnName(subjectNameIndex)}: ${cursor.getString(subjectNameIndex)}"
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
        groupSpinner = view.findViewById(R.id.facultySpinner)
        subjectSpinner = view.findViewById(R.id.subjectSpinner)
    }
}