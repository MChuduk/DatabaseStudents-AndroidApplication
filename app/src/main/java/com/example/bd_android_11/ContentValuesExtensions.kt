package com.example.bd_android_11

import android.content.ContentValues

fun ContentValues.putIntIfExists(key : String, value: Int?) {
    if(value != null) this.put(key, value)
}

fun ContentValues.putStringIfExists(key : String, value: String?) {
    if(value != null) this.put(key, value)
}