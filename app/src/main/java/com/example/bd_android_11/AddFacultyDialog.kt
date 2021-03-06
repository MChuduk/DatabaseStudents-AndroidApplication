package com.example.bd_android_11

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment

class AddFacultyDialog : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var facultyId : EditText? = null
    private var faculty : EditText? = null
    private var facultyDean : EditText? = null
    private var facultyOfficetimetable : EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)

        val view = layoutInflater.inflate(R.layout.add_faculty_dialog, null)
        findViews(view)

        builder.setTitle("Add Faculty").setView(view)
        builder.setPositiveButton("Add") { _, _ ->
            run {
                val id = getValue(facultyId)?.toInt()
                val name = getValue(faculty)
                val dean = getValue(facultyDean)
                val officetimetable = getValue(facultyOfficetimetable)

                addFaculty(id, name, dean, officetimetable)
            }
        }
        return builder.create()
    }

    private fun addFaculty(id : Int?, faculty : String?, dean : String?, officetable : String?) {
        val db = dbHelper?.writableDatabase

        val values = ContentValues()
        values.putIntIfExists("IDFACULTY", id);
        values.putStringIfExists("FACULTY", faculty)
        values.putStringIfExists("DEAN", dean)
        values.putStringIfExists("OFFICETIMETABLE", officetable)

        db?.insert("FACULTIES", null, values)
    }

    private fun findViews(view : View) {
        facultyId = view.findViewById(R.id.editTextGroupId)
        faculty = view.findViewById(R.id.editTextFaculty)
        facultyDean = view.findViewById(R.id.editTextCourse)
        facultyOfficetimetable = view.findViewById(R.id.editTextGroupName)
    }
}