package com.bhis.thehackerbank

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import java.io.File


class AccountViewMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_view_main)

        makefile() // write some sensitive information to a file.

        supportActionBar!!.title = "Welcome"
        var cookie = intent.getStringExtra("USER_COOKIE")
        if (cookie != null) {
            saveData(cookie)
        } else {
            cookie = loadData()
        }

        Intent(this, UpdateLocalDatabase::class.java).putExtra("USER_COOKIE",cookie).also{
            startService(it)
        }

        val account1 = findViewById<CardView>(R.id.account1)
        account1.setOnClickListener {

            val intent = Intent(this, Account1View::class.java)
                .putExtra("USER_COOKIE", cookie)
            startActivity(intent)
        }
        val deets = findViewById<ImageButton>(R.id.viewdeets)
        deets.setOnClickListener {
            val intent = Intent(this, AccountDetails::class.java)
                .putExtra("USER_COOKIE", cookie)
            startActivity(intent)
        }

        val newtransfer = findViewById<ImageButton>(R.id.newTransfer)
        newtransfer.setOnClickListener {
            val intent = Intent(this, NewTransfer::class.java)
                .putExtra("USER_COOKIE", cookie)
            startActivity(intent)
        }

        val cj = findViewById<Button>(R.id.contact)
        cj.setOnClickListener {
            val intent = Intent (this, ContactJohn::class.java)
            startActivity(intent)
        }

        val check = findViewById<ImageButton>(R.id.depositcheck)
        check.setOnClickListener {
            intent = Intent(this, CheckDeposit::class.java)
            startActivity(intent)
        }

        val logout = findViewById<ImageButton>(R.id.logout)
            logout.setOnClickListener {
                saveData("")
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        val search = findViewById<ImageButton>(R.id.searchUsers)
        search.setOnClickListener {
            intent = Intent(this, Search::class.java)
            startActivity(intent)
        }

    }

    fun makefile() {
        val filename = "TheBigSecrets"
        val fileContents = "f l a g { d o n t s a v e s e n s i t i v e d a t a o n t h e f i l e s y s t e m }"
        this.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }

    private fun saveData(cookie: String) {

        val sharedPreferences = getSharedPreferences("key", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("CACHED_COOKIE", cookie)
        }
        editor.commit()
    }
    private fun loadData(): String? {
        Log.d("YAVAA-DEBUG", "Recovering Cookie")
        val sharedPreferences = getSharedPreferences("key", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CACHED_COOKIE", "")!!
    }
}