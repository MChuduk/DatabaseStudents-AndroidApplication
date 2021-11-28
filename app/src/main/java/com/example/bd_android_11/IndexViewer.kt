package com.example.bd_android_11

import android.content.Context
import android.widget.Toast




class IndexViewer {

    private var dbHelper : DatabaseHelper? = null

    fun showIndexesFor(context : Context, table : String) {
        dbHelper = DatabaseHelper(context)
        val db = dbHelper?.readableDatabase
        val queryString = "PRAGMA INDEX_LIST ($table)"

        val cursor = db?.rawQuery(queryString, null)

        if(cursor?.moveToFirst() == true) {
            val seqIndex = cursor.getColumnIndex("seq")
            val nameIndex = cursor.getColumnIndex("name")
            val uniqueIndex = cursor.getColumnIndex("unique")
            val originIndex = cursor.getColumnIndex("origin")
            val partialIndex = cursor.getColumnIndex("partial")

            do {
                val row = "${cursor.getColumnName(seqIndex)}: ${cursor.getString(seqIndex)} \n" +
                        "${cursor.getColumnName(nameIndex)}: ${cursor.getString(nameIndex)} \n" +
                        "${cursor.getColumnName(uniqueIndex)}: ${cursor.getString(uniqueIndex)} \n" +
                        "${cursor.getColumnName(originIndex)}: ${cursor.getString(originIndex)} \n" +
                        "${cursor.getColumnName(partialIndex)}: ${cursor.getString(partialIndex)}"
                showMessage(context, row)
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    private fun showMessage(context : Context, message: String) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}