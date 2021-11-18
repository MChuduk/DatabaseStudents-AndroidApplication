package com.example.bd_android_11

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDialogFragment

class QueryDialog1 : AppCompatDialogFragment() {

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
        
        builder.setTitle("Список группы со средней оценкой для каждого студента по предмету").setView(view)
        builder.setPositiveButton("ОК") { _, _ ->
            run {
                val group = groupSpinner?.selectedItem.toString()
                val subject = subjectSpinner?.selectedItem.toString()

                val db = dbHelper?.readableDatabase
                val queryString = "SELECT STUDENTS.IDSTUDENT, " +
                        "GROUPS.IDGROUP, STUDENTS.STUDENTNAME, SUBJECTS.SUBJECTNAME, " +
                        "AVG(PROGRESSES.MARK) 'AVERAGE MARK' " +
                        "FROM GROUPS " +
                        "JOIN STUDENTS ON GROUPS.IDGROUP = STUDENTS.IDGROUP " +
                        "JOIN PROGRESSES ON PROGRESSES.IDSTUDENT = STUDENTS.IDSTUDENT " +
                        "JOIN SUBJECTS ON SUBJECTS.IDSUBJECT = PROGRESSES.IDSUBJECT " +
                        "WHERE GROUPS.GROUPNAME = ? AND " +
                        "SUBJECTS.SUBJECTNAME = ? " +
                        "GROUP BY STUDENTS.STUDENTNAME"

                val cursor = db?.rawQuery(queryString, arrayOf(group, subject))

                if(cursor?.moveToFirst() == true) {
                    val idStudentIndex = cursor.getColumnIndex("IDSTUDENT")
                    val idGroupIndex = cursor.getColumnIndex("IDGROUP")
                    val studentNameIndex = cursor.getColumnIndex("STUDENTNAME")
                    val subjectNameIndex = cursor.getColumnIndex("AVERAGE MARK")

                    do {

                    } while (cursor.moveToNext())
                }
                cursor?.close()
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
        groupSpinner = view.findViewById(R.id.groupSpinner)
        subjectSpinner = view.findViewById(R.id.subjectSpinner)
    }
}