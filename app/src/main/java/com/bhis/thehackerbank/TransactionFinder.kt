package com.bhis.thehackerbank

import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import kotlin.math.pow

import android.content.DialogInterface
import android.content.Intent

import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.scottyab.rootbeer.RootBeer
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.CountDownLatch
import kotlin.system.exitProcess

class TransactionFinder : AppCompatActivity() {
    var mDB: SQLiteDatabase? = null
    var seed = ((Math.pow(2.0, 58.0) / 7.0 - (93.0 % 250
            / 531) - 3.0.pow(6.0) + Math.pow(
        813.0,
        21.0
    )).toInt() % (536870912 / 2097152)) - 238

    //println("$seed")
    private var smh: SupportingMethods = SupportingMethods()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flag1 = "and9cHYrSH5kMXBjdDFBZn90dTEgISEgbA=="
        val flag2 = "and9cHYrRnQxcGN0MXB9fTFBZn90dTEgISAhbA=="
        val flag3 = "and9cHYrVHBjZXkxeGIxeH93dHJldHUxICAhIGw="
        val flag4 = "and9cHYrS358c3h0YjFwY3QxZGF+fzFkYjEgICAhbA=="
        val sql1 = String.format(
            "INSERT INTO sqliuser VALUES ('aliendroid1', 'AlphaCenturi3031', '%s');",
            flag1
        )
        val sql2 = String.format(
            "INSERT INTO sqliuser VALUES ('aliendroid2', 'AlphaCenturi3032', '%s');",
            flag2
        )
        val sql3 = String.format(
            "INSERT INTO sqliuser VALUES ('aliendroid3', 'AlphaCenturi3033', '%s');",
            flag3
        )
        val sql4 = String.format(
            "INSERT INTO sqliuser VALUES ('aliendroid4', 'AlphaCenturi3034', '%s');",
            flag4
        )
        try {
            mDB = openOrCreateDatabase("sqli", MODE_PRIVATE, null)
            mDB!!.execSQL("DROP TABLE IF EXISTS sqliuser;")
            mDB!!.execSQL("CREATE TABLE IF NOT EXISTS sqliuser(user VARCHAR, password VARCHAR, credit_card VARCHAR);")
            mDB!!.execSQL(sql1)
            mDB!!.execSQL(sql2)
            mDB!!.execSQL(sql3)
            mDB!!.execSQL(sql4)
        } catch (e: Exception) {
            Log.d("diva-sql", "Error occurred while creating database for SQLI: " + e.message)
        }
        setContentView(R.layout.activity_transaction_finder)
    }

    fun search(view: View?) {
        val srchtxt = findViewById(R.id.ivi1search) as EditText
        var cr: Cursor? = null
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.hideSoftInputFromWindow(this.getCurrentFocus()?.getWindowToken() ?: , 0)
        try {
            cr = mDB!!.rawQuery(
                "SELECT * FROM sqliuser WHERE user = '" + srchtxt.text.toString() + "'",
                null
            )
            val strb = StringBuilder("")
            if (cr != null && cr.count > 0) {
                cr.moveToFirst()
                do {
                    val extra: String = smh.transcode(cr.getString(2), seed)
                    strb.append(
                        """
                            User: ${cr.getString(0)}, Password: ${cr.getString(1)}, Extra: $extra
                            
                            """.trimIndent()
                    )
                } while (cr.moveToNext())
            } else {
                strb.append("User: (" + srchtxt.text.toString() + ") not found")
            }
            Log.d("diva-sqli", strb.toString())
            Toast.makeText(this, strb.toString(), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.d("diva-sqli", "Error occurred while searching in database: " + e.message)
        }
    }
}
