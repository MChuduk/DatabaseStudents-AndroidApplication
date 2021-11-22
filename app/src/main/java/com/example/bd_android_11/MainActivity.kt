package com.example.bd_android_11

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd_android_11.queryDialogs.QueryDialog1
import com.example.bd_android_11.queryDialogs.QueryDialog2
import com.example.bd_android_11.queryDialogs.QueryDialog3
import com.example.bd_android_11.queryDialogs.QueryDialog4

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
            R.id.query2 -> {
                val queryDialog = QueryDialog2(this)
                queryDialog.show(supportFragmentManager, "query2")
            }
            R.id.query3 -> {
                val queryDialog = QueryDialog3(this)
                queryDialog.show(supportFragmentManager, "query3")
            }
            R.id.query4 -> {
                val queryDialog = QueryDialog4(this)
                queryDialog.show(supportFragmentManager, "query4")
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