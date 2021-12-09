package com.example.bd_android_11

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd_android_11.queryDialogs.*

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
            R.id.query5 -> {
                val queryDialog = QueryDialog5(this)
                queryDialog.show(supportFragmentManager, "query5")
            }
            R.id.query6 -> {
                val viewer = IndexViewer()
                viewer.showIndexesFor(this,"STUDENTS")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onAddDataButtonClick(view : View) {
        val intent = Intent(this, AddDataActivity::class.java);
        startActivity(intent)
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