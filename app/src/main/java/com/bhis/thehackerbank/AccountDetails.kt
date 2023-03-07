package com.bhis.thehackerbank

import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.bhis.thehackerbank.databinding.AccountDetailsBinding
import kotlin.math.pow
import android.util.Base64
import android.util.Log
import java.io.File


class AccountDetails : AppCompatActivity() {
    private var smh = SupportingMethods()
    private var seed = (3.0.pow(7.0) % (21 + 199)).toInt() - 100
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
        val apikey = "not-the-real-key...sorry"
        val hiddenApikey = smh.bisect(apikey, seed)
        val f = File(filesDir,"lastlogin.txt")
        var self = ""
        if (f.exists()) {
            self = f.readText(Charsets.UTF_8).split(":")[0]
            println(self)
        }

        val username = self
        val passwd = "******" // query from database instead
        var apidetails =
            String.format("API Key: %s\n\nUser name: %s\n\nPassword: %s\n\n", hiddenApikey, username, passwd)


        if (legit == null) {
            val decodedString: String = String(Base64.decode("ZmxhZ3tpX2tub3dfaG93X3lvdV9nb3RfaGVyZX0=", 0))
            c.text = decodedString
            apidetails = ""
        } else if (legit.length != 64) {
            val decodedSting: String = String(Base64.decode("ZmxhZ3t0aGVzZV9hcmVudF90aGVfZXh0cmFzX3lvdXJlX2xvb2tpbmdfZm9yfQ==", 0))
            c.text = decodedSting
        }

        // Display the details on the app
        apicview.text = apidetails
    }
}
