package com.example.bd_android_11

import android.widget.EditText

fun getValue(editText: EditText?) : String? {
    if(editText?.text?.isNotEmpty() == true)
        return editText.text.toString()
    return null
}

fun convertDate(date : String) : String {
    val values = date.split('.', '/')

    if(values.size < 2 || date.contains('-')) return date

    return "${values[2]}-${values[1]}-${values[0]}"
}