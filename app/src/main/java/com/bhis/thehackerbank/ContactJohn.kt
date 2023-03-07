package com.bhis.thehackerbank

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.bhis.thehackerbank.R.layout.contact_john
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ContactJohn : AppCompatActivity() {

    val secret = "fromtheapp"
    val net: com.bhis.thehackerbank.networkHelpers = networkHelpers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contact_john)

        supportActionBar!!.title = "PIN ALL THE CERTS"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var send = findViewById<Button>(R.id.beacon)
        send.setOnClickListener {
            val response = net.pinnedRequest(this, secret)

            if (response != "error") {
                val flag = findViewById<TextView>(R.id.flag)
                flag.text = response
            } else {
                showAlertDialogue()
            }
        }
    }
    fun showAlertDialogue() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Oof.")
            .setMessage("I know about your proxy...")
            .setNegativeButton("Okay") { dialogInterface: DialogInterface, i: Int ->
            }
            .setCancelable(false)
            .show()
    }
}