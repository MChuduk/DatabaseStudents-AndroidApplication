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

class QueryDialog3(val activity: MainActivity) : AppCompatDialogFragment()  {

    private var dbHelper : DatabaseHelper? = null

    private var groupSpinner : Spinner? = null
    private var startDateEditText : EditText? = null
    private var endDateEditText : EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.query3_dialog, null)
        findViews(view)


        setupSpinner(groupSpinner, "SELECT GROUPNAME FROM GROUPS")

        builder.setTitle("Средняя оценка для группы").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val queryResult : MutableList<String> = mutableListOf()
                val group = groupSpinner?.selectedItem.toString()

                val startDate = convertDate(startDateEditText?.text.toString())
                val endDate = convertDate(endDateEditText?.text.toString())

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT GROUPS.IDGROUP, AVG(PROGRESSES.MARK) 'AVERAGE MARK' " +
                        "FROM GROUPS JOIN STUDENTS ON GROUPS.IDGROUP = STUDENTS.IDGROUP " +
                        "JOIN PROGRESSES ON STUDENTS.IDSTUDENT = PROGRESSES.IDSTUDENT " +
                        "WHERE GROUPS.GROUPNAME = ? AND EXAMDATE BETWEEN ? AND ? " +
                        "GROUP BY GROUPS.IDGROUP"

                val cursor = db?.rawQuery(queryString, arrayOf(group, startDate, endDate))

                if(cursor?.moveToFirst() == true) {
                    val groupNameIndex = cursor.getColumnIndex("IDGROUP")
                    val averageMarkIndex = cursor.getColumnIndex("AVERAGE MARK")

                    do {
                        val row = "${cursor.getColumnName(groupNameIndex)}: ${cursor.getString(groupNameIndex)} \n" +
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
        groupSpinner = view.findViewById(R.id.facultySpinner)
        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
    }
}