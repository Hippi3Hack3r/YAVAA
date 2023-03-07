package com.bhis.thehackerbank

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*

class UpdateLocalDatabase : IntentService("UpdateLocalDatabase") {

    override fun onHandleIntent(intent: Intent?) {

        Log.i("YAVAA-INFO", "Updating local database...")

        val net = networkHelpers()
        var cookie = intent?.getStringExtra("USER_COOKIE")
        if (cookie == null) {
            cookie = ""
        }
        // fetch all the transactions from the server
        val records: String = net.getRecords(cookie)
        if (records == "Oops") {
            return
        }

        val trans= Gson().fromJson(records, Array<Transaction>::class.java).asList()
        println(trans)

        // Update database
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        val all = db.transactionDao().getAll()
        for(a in all) {
            db.transactionDao().deleteAll()
        }
        for(t in trans) {
            dbpopulate(transaction = t)
        }

    }

    private fun dbpopulate(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
        }
    }
}