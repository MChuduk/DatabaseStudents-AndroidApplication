package com.example.bd_android_11.queryDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.bd_android_11.DatabaseHelper
import com.example.bd_android_11.MainActivity
import com.example.bd_android_11.R
import com.example.bd_android_11.convertDate
import com.example.bd_android_11.datePickers.DateListPicker
import com.example.bd_android_11.datePickers.DatePickerBase
import com.example.bd_android_11.datePickers.DatePickerRange
import java.sql.Date

class QueryDialog2(val activity: MainActivity) : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var studentSpinner : Spinner? = null
    private var subjectSpinner : Spinner? = null
    private var datePickerSpinner : Spinner? = null

    private var datePicker : DatePickerBase? = null;

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.query2_dialog, null)
        findViews(view)

        setupSpinner(studentSpinner, "SELECT STUDENTNAME FROM STUDENTS")
        setupSpinner(subjectSpinner, "SELECT SUBJECTNAME FROM SUBJECTS")
        setupDatePickerSpinner(view);

        builder.setTitle("Средняя оценка для студента").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val queryResult : MutableList<String> = mutableListOf()
                val queryArguments : MutableList<String> = mutableListOf()
                val student = studentSpinner?.selectedItem.toString()
                val subject = subjectSpinner?.selectedItem.toString()

                queryArguments.add(student)
                queryArguments.add(subject)

                if(datePickerSpinner?.selectedItemPosition  == 0){
                    queryArguments.add((datePicker as DatePickerRange).getStartDate())
                    queryArguments.add((datePicker as DatePickerRange).getEndDate())
                }else{
                    queryArguments.add((datePicker as DateListPicker).getOffset())
                }

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT STUDENTS.STUDENTNAME, SUBJECTS.SUBJECTNAME, AVG(PROGRESSES.MARK) 'AVERAGE MARK' " +
                        "FROM STUDENTS JOIN PROGRESSES ON STUDENTS.IDSTUDENT = PROGRESSES.IDSTUDENT " +
                        "JOIN SUBJECTS ON PROGRESSES.IDSUBJECT = SUBJECTS.IDSUBJECT " +
                        "WHERE STUDENTS.STUDENTNAME = ? AND SUBJECTS.SUBJECTNAME = ? " +
                        "AND ${datePicker?.dateQueryString} " +
                        "GROUP BY STUDENTS.STUDENTNAME"

                val cursor = db?.rawQuery(queryString, queryArguments.toTypedArray())

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
        studentSpinner = view.findViewById(R.id.facultySpinner)
        subjectSpinner = view.findViewById(R.id.subjectSpinner)
        datePickerSpinner = view.findViewById(R.id.datePickerSelectionSpinner)
    }
}