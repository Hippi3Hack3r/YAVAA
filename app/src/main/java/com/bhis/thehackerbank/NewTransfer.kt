package com.bhis.thehackerbank

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.bhis.thehackerbank.databinding.AccountDetailsBinding
import com.bhis.thehackerbank.databinding.ActivityMainBinding
import com.bhis.thehackerbank.databinding.NewTransferBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.CountDownLatch

class NewTransfer : AppCompatActivity() {

    private lateinit var binding: NewTransferBinding
    private lateinit var transactions : List<Transaction>
    private lateinit var db : AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.title = "New Transfer"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var cookie = intent.getStringExtra("USER_COOKIE")
        if (cookie == null) {
            cookie = ""
        }

        val net = networkHelpers()
        val users = net.findAllUsers()
        val f = File(filesDir,"lastlogin.txt")
        var self = ""
        if (f.exists()) {
            self = f.readText(Charsets.UTF_8).split(":")[0]
            println(self)
        }

        // initialize the database
        transactions = arrayListOf()
        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions")
            .fallbackToDestructiveMigration()
            .build()

        var count: Double = 0.0
        val countDownLatch = CountDownLatch(1)
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()

            for (trans in transactions) {
                if (trans.to_acct != "YOURHACKERPOINTS")
                    count -= trans.amount
                else
                    count += trans.amount
            }
            countDownLatch.countDown()
        }

        countDownLatch.await()

        val selfu = net.findUser(self).split(",")
        println("SELF $selfu")
        val fromOpts = listOf(selfu[0])
        println("SELF $fromOpts")
        val toOpts = users // not sure why this is a nested list and don't particularly care.
        binding = NewTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromAdap = ArrayAdapter<String>(this, R.layout.accountlist, fromOpts)
        binding.fromactInput.setAdapter(fromAdap)

        val toAdap = ArrayAdapter<String>(this, R.layout.accountlist, toOpts)
        binding.toactInput.setAdapter(toAdap)

        val addTransactionBtn = findViewById<Button>(R.id.addTransactionBtn)

        addTransactionBtn.setOnClickListener {

            val etfromact = findViewById<AutoCompleteTextView>(R.id.fromactInput)
            val fromact = etfromact.text.toString()
            val ettoact = findViewById<AutoCompleteTextView>(R.id.toactInput)
            val toact = findViewById<AutoCompleteTextView>(R.id.toactInput).text.toString()
            val etamount = findViewById<TextInputEditText>(R.id.amountInput)

            val amount = etamount.text.toString()
            val etdesc = findViewById<TextInputEditText>(R.id.descInput)
            val desc = findViewById<TextInputEditText>(R.id.descInput).text.toString()

            if (fromact.trim().isEmpty()) {
                etfromact.error = "Choose a Valid Account"
                return@setOnClickListener
            }
            if (toact.trim().isEmpty()) {
                ettoact.error = "Choose a Valid Account"
                return@setOnClickListener
            }
            if (amount.trim().isEmpty()) {
                etamount.error = "Please enter a valid amount"
                return@setOnClickListener
            }
            if (amount.toDouble() < 0) {// potential attack here
                etamount.error = "Amount must be greater than 0"
                return@setOnClickListener
            }
            if (amount.toDouble() > count) {
                etamount.error = "You cannot send more points than you have...or can you"
                return@setOnClickListener
            }
            if (desc.trim().isEmpty()) {
                etdesc.error = "Enter a description"
                return@setOnClickListener
            }

            // Theres gotta be a more efficient way to do this
            val jo = JSONObject()
            jo.put("uid", 0)
            jo.put("fromact", fromact)
            jo.put("toact", toact)
            jo.put("amount", amount.toDouble())
            jo.put("description", desc)

            val requestBody = jo.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val URL = "https://android.pwncompany.com/update"
            val request = Request.Builder().addHeader("authcookie", cookie).url(URL).post(requestBody).build()

            val client = OkHttpClient.Builder()
                .build()
            client.newCall(request).enqueue(object: Callback
            {
                override fun onResponse(call: Call, response: Response) {
                    val okay = response.code
                    println("resp code: $okay")
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("failed to execute request...$e")
                }
            })
            val intent = Intent(this, AccountViewMain::class.java)
                .putExtra("USER_COOKIE", cookie)
            startActivity(intent)
        }
    }
}
