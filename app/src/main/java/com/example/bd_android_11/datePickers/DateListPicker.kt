package com.example.bd_android_11.datePickers

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import com.example.bd_android_11.R
import com.example.bd_android_11.getValue

class DateListPicker(var view: View) : DatePickerBase() {

    override var dateQueryString: String = "PROGRESSES.EXAMDATE BETWEEN date('now', ?) AND date('now')"

    private var partsOfYearCountEditText : EditText? = null
    private var partsOfYearSpinner : Spinner? = null

    init {
        findViews();
    }

    fun getOffset() : String {
        val count = getValue(partsOfYearCountEditText)
        var keyWord = "";
        when(partsOfYearSpinner?.selectedItemPosition){
            0 -> keyWord = "days"
            1 -> keyWord = "months"
            2 -> keyWord = "years"
        }
        return "-$count $keyWord"
    }

    private fun findViews() {
        partsOfYearCountEditText = view.findViewById(R.id.listCountEditText)
        partsOfYearSpinner = view.findViewById(R.id.partOfYearSpinner)
    }
}