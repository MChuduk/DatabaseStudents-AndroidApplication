package com.example.bd_android_11.queryDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.bd_android_11.DatabaseHelper
import com.example.bd_android_11.MainActivity
import com.example.bd_android_11.R
import com.example.bd_android_11.datePickers.DateListPicker
import com.example.bd_android_11.datePickers.DatePickerBase
import com.example.bd_android_11.datePickers.DatePickerRange

class QueryDialog5(val activity: MainActivity) : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var datePickerSpinner : Spinner? = null

    private var datePicker : DatePickerBase? = null;

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.query5_dialog, null)
        findViews(view)

        setupDatePickerSpinner(view);

        builder.setTitle("Сравнительный анализ по факультетам").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val queryResult : MutableList<String> = mutableListOf()
                val queryArguments : MutableList<String> = mutableListOf()

                if(datePickerSpinner?.selectedItemPosition  == 0){
                    queryArguments.add((datePicker as DatePickerRange).getStartDate())
                    queryArguments.add((datePicker as DatePickerRange).getEndDate())
                }else{
                    queryArguments.add((datePicker as DateListPicker).getOffset())
                }

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT FACULTIES.FACULTY, ROUND(AVG(PROGRESSES.MARK), 2) 'AVERAGE MARK' " +
                        "FROM FACULTIES JOIN GROUPS ON FACULTIES.IDFACULTY = GROUPS.FACULTY " +
                        "JOIN STUDENTS ON GROUPS.IDGROUP = STUDENTS.IDGROUP " +
                        "JOIN PROGRESSES ON STUDENTS.IDSTUDENT = PROGRESSES.IDSTUDENT " +
                        "JOIN SUBJECTS ON PROGRESSES.IDSUBJECT = SUBJECTS.IDSUBJECT " +
                        "WHERE ${datePicker?.dateQueryString} " +
                        "GROUP BY FACULTIES.FACULTY " +
                        "ORDER BY AVG(PROGRESSES.MARK)"

                val cursor = db?.rawQuery(queryString, queryArguments.toTypedArray())

                if(cursor?.moveToFirst() == true) {
                    val facultyNameIndex = cursor.getColumnIndex("FACULTY")
                    val averageMarkIndex = cursor.getColumnIndex("AVERAGE MARK")

                    do {
                        val row = "${cursor.getColumnName(facultyNameIndex)}: ${cursor.getString(facultyNameIndex)} \n" +
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

    private fun setupDatePickerSpinner(view : View){
        val dateRangePickerView : View = view.findViewById(R.id.dateRangePicker)
        val dateListPickerView : View = view.findViewById(R.id.dateListPicker)

        datePickerSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {
                if(index == 0){
                    datePicker = DatePickerRange(dateRangePickerView)

                    dateRangePickerView.visibility = View.VISIBLE
                    dateListPickerView.visibility = View.GONE
                }else{
                    datePicker = DateListPicker(dateListPickerView)

                    dateRangePickerView.visibility = View.GONE
                    dateListPickerView.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun findViews(view : View) {
        datePickerSpinner = view.findViewById(R.id.datePickerSelectionSpinner)
    }
}