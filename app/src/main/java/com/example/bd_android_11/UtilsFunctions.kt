package com.example.bd_android_11

import android.widget.EditText

fun getValue(editText: EditText?) : String? {
    if(editText?.text?.isNotEmpty() == true)
        return editText.text.toString()
    return null
}