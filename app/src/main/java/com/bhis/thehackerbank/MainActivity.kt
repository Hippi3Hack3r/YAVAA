package com.bhis.thehackerbank

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scottyab.rootbeer.RootBeer
import java.io.File
import kotlin.random.Random
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val net = networkHelpers()
        val userVal = checkdevID()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userView = findViewById<TextView>(R.id.tUser)
        userView.text = userVal

        net.registerAccount(userVal)  // lazy implementation. Server ignores if user exists.

        getActionBar()?.setDisplayHomeAsUpEnabled(true)

        Log.i("YAVAA-INFO", String(Base64.decode("ZmxhZ3t5b3VfcmVhZF90aGVfbG9ncyF9", 0)))
        val context = applicationContext
        val rootBeer = RootBeer(context)
        if (rootBeer.isRooted) {
            rootAlert()
        }

        val code = findViewById<EditText>(R.id.editTextNumberPassword)
        val loginButton = findViewById<Button>(R.id.button)
        loginButton.setOnClickListener {
            val pass = code.text.toString()
            val correct = net.validateLogin(pass, this)
            if (correct[0] != "Password Correct") {

                if (correct[0] != "internets broke") {
                    Toast.makeText(
                        this@MainActivity,
                        "Incorrect Passcode",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val cookie = correct[1]
                val user = correct[2]
                Log.d("YAVAA-AUTH", "Successful Login by user $cookie")
                val fstring = "$user : $cookie"
                mostRecentLogin(fstring)
                val intent = Intent(this, AccountViewMain::class.java)
                intent.putExtra("USER_COOKIE", cookie)
                startActivity(intent)
            }
            code.text.clear()
        }
    }

    fun rootAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Root Detected")
            .setMessage("This app cannot run on a rooted device.")
            .setNegativeButton("Okay") { _: DialogInterface, i: Int ->
                exitProcess(0)
            }
            //.setCancelable(false)
            .show()
    }

    fun mostRecentLogin(userstring: String) {
        this.openFileOutput("lastlogin.txt", Context.MODE_PRIVATE).use {
            it.write(userstring.toByteArray())
        }
    }

    fun checkdevID(): String {
        val filePath: String = "$filesDir/registered.txt"
        var file = File(filePath)
        var fileExists = file.exists()
        var uid = ""
        if (fileExists) {
            Log.d("YAVAA-DEBUG", "Device already registered. Continuing.")
            val yourFile = File(filePath)
            uid = yourFile.readText()

        } else {

            uid = Random.nextInt(10000, 99999).toString()
            this.openFileOutput("registered.txt", Context.MODE_PRIVATE).use {
                it.write(uid.toByteArray())
            }
        }
    return uid
    }
}