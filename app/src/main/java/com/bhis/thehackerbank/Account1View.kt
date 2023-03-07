package com.bhis.thehackerbank

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bhis.thehackerbank.R.layout.account1_view
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Account1View : AppCompatActivity() {

    private lateinit var transactions : List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var llayoutManager: LinearLayoutManager
    private lateinit var db : AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(account1_view)

        val cookie = intent.getStringExtra("USER_COOKIE")!!

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // initialize the database
        transactions = arrayListOf()
        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions")
            .fallbackToDestructiveMigration()
            .build()

        var count: Double = 0.0
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()
            runOnUiThread {
                transactionAdapter.setData(transactions)
                println("aaaaaaaaaaaaaaaaaaaa")
                for (trans in transactions) {
                    if (trans.to_acct != "YOURHACKERPOINTS")
                        count -= trans.amount
                    else
                        count += trans.amount
                }
                val total = findViewById<TextView>(R.id.Balance)
                total.text = "Balance: " + count.toString()
            }
        }

        // cache in local database for searching
        transactionAdapter = TransactionAdapter(transactions)
        llayoutManager = LinearLayoutManager(this)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.apply {
            adapter = transactionAdapter
            layoutManager = llayoutManager
        }

    }
}

