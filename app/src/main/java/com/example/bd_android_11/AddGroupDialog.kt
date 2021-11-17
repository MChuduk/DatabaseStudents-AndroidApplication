package com.example.bd_android_11

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment

class AddGroupDialog : AppCompatDialogFragment() {

    private var dbHelper : DatabaseHelper? = null

    private var groupId : EditText? = null
    private var faculty : EditText? = null
    private var course : EditText? = null
    private var name : EditText? = null
    private var head : EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dbHelper = DatabaseHelper(context)

        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.add_faculty_dialog, null)
        findViews(view)

        builder.setTitle("Add Faculty").setView(view)
        builder.setPositiveButton("Add") { _, _ ->
            run {
                val id = getValue(groupId)?.toInt()
                val idFaculty = getValue(faculty)?.toInt()
                val groupCourse = getValue(course)
                val groupName = getValue(name)
                val groupHead = getValue(head)?.toInt()

                addGroup(id, idFaculty, groupCourse, groupName, groupHead)
            }
        }
        return builder.create()
    }

    private fun addGroup(id : Int?, faculty : Int?, course : String?, name : String?, head : Int?) {

    }

    private fun findViews(view : View) {
        groupId = view.findViewById(R.id.editTextGroupId)
        faculty = view.findViewById(R.id.editTextFaculty)
        course = view.findViewById(R.id.editTextCourse)
        name = view.findViewById(R.id.editTextGroupName)
        head = view.findViewById(R.id.editTextHead)
    }
}