package com.example.bd_android_11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.widget.Toast


class AddDataActivity : AppCompatActivity() {

    var idStudentEditText : EditText? = null;
    var idGroupEditText : EditText? = null;
    var studentNameEditText : EditText? = null;
    var birthdayEditText : EditText? = null;
    var addressEditText : EditText? = null;

    private var dbHelper : DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        dbHelper = DatabaseHelper(this)

        findViews();
    }

    fun onAddStudentButtonClick(view : View) {
        val idStudent = getValue(idStudentEditText);
        val idGroup = getValue(idGroupEditText);
        val studentName = getValue(studentNameEditText);
        val birthday = getValue(birthdayEditText);
        val address = getValue(addressEditText);

        val database = dbHelper?.writableDatabase;

        val values = ContentValues()

        values.put("IDSTUDENT", idStudent)
        values.put("IDGROUP", idGroup)
        values.put("STUDENTNAME", studentName)
        values.put("BIRTHDAY", birthday)
        values.put("ADDRESS", address)
        try {
            database?.insertOrThrow("STUDENTS", null, values);
        } catch (e : SQLiteException){
            showMessage(this, e.message.toString());
        }
    }

    fun onRemoveStudentButtonClick(view : View) {
        val idStudent = getValue(idStudentEditText);

        val database = dbHelper?.writableDatabase;
        try {
            database?.delete("STUDENTS", "IDSTUDENT = ?", arrayOf(idStudent));
        } catch (e : SQLiteException){
            showMessage(this, e.message.toString());
        }
    }

    fun onUpdateStudentButtonClick(view : View) {
        val idStudent = getValue(idStudentEditText);
        val idGroup = getValue(idGroupEditText);
        val studentName = getValue(studentNameEditText);
        val birthday = getValue(birthdayEditText);
        val address = getValue(addressEditText);

        val database = dbHelper?.writableDatabase;

        val values = ContentValues()

        values.put("IDSTUDENT", idStudent)
        values.put("IDGROUP", idGroup)
        values.put("STUDENTNAME", studentName)
        values.put("BIRTHDAY", birthday)
        values.put("ADDRESS", address)
        try {
            database?.update("STUDENTS", values, "IDSTUDENT = ?", arrayOf(idStudent));
        } catch (e : SQLiteException){
            showMessage(this, e.message.toString());
        }
    }

    private fun findViews() {
        idStudentEditText = findViewById(R.id.editTextIdStudent);
        idGroupEditText = findViewById(R.id.editTextIdGroup);
        studentNameEditText = findViewById(R.id.editTextStudentName);
        birthdayEditText = findViewById(R.id.editTextBirthday);
        addressEditText = findViewById(R.id.editTextAddress);
    }

    private fun showMessage(context : Context, message: String) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}