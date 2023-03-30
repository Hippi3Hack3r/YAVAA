package com.bhis.thehackerbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView


class Search : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        val net = networkHelpers()

        val sv = findViewById<SearchView>(R.id.searchbox)
        sv.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                println("SUBMITTED TEXT ${query}")
                val ret = net.findUser(query)
                val fill = findViewById<TextView>(R.id.searchresults)
                fill.text = ret
                //on submit send entire query
                return false
            }

        })

    }

}