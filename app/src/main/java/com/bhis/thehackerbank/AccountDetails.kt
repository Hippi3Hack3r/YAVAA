package com.bhis.thehackerbank

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.util.Base64
import android.util.Log
import java.io.File


class AccountDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "API Key"

        val apicview = findViewById<TextView>(R.id.apicTextView)
        val legit = intent.getStringExtra("USER_COOKIE")
        val c = findViewById<TextView>(R.id.accessmethod)

        Log.d("YAVAA_COOKIE", "$legit")
        // Get API credentials and other confidential details of the user
        val apikey = "Nothing to see here. But looking at the code is good, keep doing that."

        val f = File(filesDir,"lastlogin.txt")
        var self = ""
        var id = ""
        if (f.exists()) {
            self = f.readText(Charsets.UTF_8).split(" : ")[0]
            id = f.readText(Charsets.UTF_8).split(" : ")[1]
            println(self)
        }

        val username = self
        val passwd = "*****" // query from database instead
        var apidetails =
            String.format("API Key: %s\n\nUser name: %s\n\nPassword: %s\n\n", "ZmxhZ3tvYmZ1c2NhdGlvbi4gT2JmdXNjYXRpb24gRXZlcnl3aGVyZS59", username, passwd)

        if (legit == null) {
            val decodedString: String = String(Base64.decode("ZmxhZ3tpX2tub3dfaG93X3lvdV9nb3RfaGVyZX0=", 0))
            c.text = decodedString
            apidetails = ""
        } else if (legit != id) {
            val decodedSting: String = String(Base64.decode("ZmxhZ3t0aGVzZV9hcmVudF90aGVfZXh0cmFzX3lvdXJlX2xvb2tpbmdfZm9yfQ==", 0))
            c.text = decodedSting
        }

        // Display the details on the app
        apicview.text = apidetails
    }
}
