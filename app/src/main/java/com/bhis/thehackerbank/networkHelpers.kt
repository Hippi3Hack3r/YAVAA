package com.bhis.thehackerbank

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URI
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.system.exitProcess


class networkHelpers {

    // This displays the popup window informing the user of a network error
    fun ConnectError(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Cannot connect to server.")
            .setMessage("An unknown network error has occured. Please try again later.")
            .setNegativeButton("Exit") { dialogInterface: DialogInterface, i: Int ->
                exitProcess(0)
            }
            .setCancelable(false)
            .show()
    }

    // The universe is interfering with non-pinned https requests and throwing ssl errors.
    // This function has no right to be necessary but it is. Ignore its existence and move on please.
    fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
        return this
    }

    // Get a login cookie using the remote endpoint.
    fun validateLogin(URL: String, password : String, context: Context): Array<String> {

        var resp = "Nope."

        val fb = FormBody.Builder().add("username", "notahacker").add("password", password).build()
        val request = Request.Builder().url(URL).post(fb).build()

        val client = OkHttpClient.Builder().apply { ignoreAllSSLErrors() }
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object: Callback
        {
            override fun onResponse(call: Call, response: Response) {
                Log.d("YAVAA-DEBUG", "Success contacting server.")
                val okay = response.code
                if (okay == 200) {
                    resp = response.body?.string().toString()
                } else {
                    resp = "error,error"
                }

                countDownLatch.countDown()
            }
            // TODO: Figure out why this is crashing the app
            override fun onFailure(call: Call, e: IOException) {
                Log.e("YAVAA-ERROR", "$e")
                countDownLatch.countDown()
                ConnectError(context)
            }
        })
        countDownLatch.await()
        val split = resp.split(',')
        if(split[0] == "Password Correct") {
            return arrayOf<String>(split[0], split[1], split[2])
        } else if (split[0] == "Not Today Satan"){
            return arrayOf<String>(split[0], split[1])
        }
        else {
            ConnectError(context)
            return arrayOf<String>("internets broke", split[1])
        }
    }

    fun getRecords(cookie: String): String {

        lateinit var resp: String
        val URL = "https://android.pwncompany.com/gettransactions"
        val request = Request.Builder().addHeader("authcookie", cookie).url(URL).get().build()

        val client = OkHttpClient.Builder().apply { ignoreAllSSLErrors() }
            .build()

        val countDownLatch = CountDownLatch(1) // working. dont touch
        client.newCall(request).enqueue(object: Callback
        {
            override fun onResponse(call: Call, response: Response) {
                Log.d("YAVAA-DEBUG", "Pulled records from server")
                resp = response.body?.string().toString()
                countDownLatch.countDown()
            }
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                Log.e("YAVAA-ERROR", "$e")
                resp = "Oops"

            }
        })
        countDownLatch.await()
        return resp

    }
    fun findUser(query : String ): String {

        lateinit var resp: String
        val URL = "https://android.pwncompany.com/finduser?s=${query}"
        val request = Request.Builder().url(URL).get().build()


        val client = OkHttpClient.Builder().apply { ignoreAllSSLErrors() }
            .build()

        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object: Callback
        {
            override fun onResponse(call: Call, response: Response) {
                Log.d("YAVAA-DEBUG", "User Found")
                resp = response.body?.string().toString()
                countDownLatch.countDown()
            }
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                Log.e("YAVAA-ERROR",  "$e")
                resp = "Oops"
            }
        })
        countDownLatch.await()
        return resp

    }
    fun findAllUsers(): List<String> {

        lateinit var resp: String
        val URL = "https://android.pwncompany.com/getusers"
        val request = Request.Builder().url(URL).get().build()


        val client = OkHttpClient.Builder().apply { ignoreAllSSLErrors() }
            .build()

        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object: Callback
        {
            override fun onResponse(call: Call, response: Response) {
                resp = response.body?.string().toString()
                countDownLatch.countDown()
            }
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
                Log.e("YAVAA-ERROR",  "$e")
                resp = "Oops"
            }
        })
        countDownLatch.await()
        return resp.split(",")

    }


    fun pinnedRequest(context: Context, secret: String): String {
        println("did in fact make it to this function")
        val URL = "https://android.pwncompany.com/contactjohn"
        var resp = "error"
        val request = Request.Builder().addHeader("secretkey", secret).url(URL).build()

        val certificatePinner = CertificatePinner.Builder()
            .add(
                "android.pwncompany.com",
                "sha256/lthr/1UV+iqttgRBR+hKnRH2jzOcFxwLqqVdr/Qti5Q="
            )
            .build()
        val client = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .connectTimeout(60, TimeUnit.SECONDS) // This exists to give the user time to edit the request in burp
            .build()

        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("YAVAA-DEBUG", "Success contacting server.")
                val status = response.code
                if (status == 200) {
                    resp = response.body?.string().toString()
                } else {
                    resp = "error"
                }

                countDownLatch.countDown()
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("YAVAA-ERROR", "Network Error: $e")
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        println(resp)
        return resp
    }
}
