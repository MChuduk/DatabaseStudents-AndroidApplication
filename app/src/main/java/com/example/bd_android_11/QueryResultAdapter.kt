package com.example.bd_android_11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QueryResultAdapter(private val items: List<String>) :
    RecyclerView.Adapter<QueryResultAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var queryTextView: TextView? = null

        init {
            queryTextView = itemView.findViewById(R.id.queryTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.query_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.queryTextView?.text = items[position]
    }

    override fun getItemCount() = items.size

}