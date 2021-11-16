package com.example.bd_android_11

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addFaculty -> {
                val addFacultyDialog = AddFacultyDialog()
                addFacultyDialog.show(supportFragmentManager, "add_faculty_dialog")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}