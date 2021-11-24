package com.example.bd_android_11.datePickers

import android.view.View
import android.widget.EditText
import com.example.bd_android_11.R
import com.example.bd_android_11.convertDate

class DatePickerRange(var view: View) : DatePickerBase() {

    override var dateQueryString: String = "PROGRESSES.EXAMDATE BETWEEN ? AND ?"

    private var startDateEditText : EditText? = null
    private var endDateEditText : EditText? = null

    init {
        findViews();
    }

    fun getStartDate() : String = convertDate(startDateEditText?.text.toString())

    fun getEndDate() : String = convertDate(endDateEditText?.text.toString())

    private fun findViews() {
        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
    }
}