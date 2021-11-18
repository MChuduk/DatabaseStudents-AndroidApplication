package com.example.bd_android_11

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var  recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.query1 -> {
                val queryDialog = QueryDialog1(this)
                queryDialog.show(supportFragmentManager, "query1")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setQueryResult(result : MutableList<String>) {
        println("log: $result")

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.adapter = QueryResultAdapter(result)
    }

    private fun findViews() {
        recyclerView = findViewById(R.id.recyclerView)
    }
}