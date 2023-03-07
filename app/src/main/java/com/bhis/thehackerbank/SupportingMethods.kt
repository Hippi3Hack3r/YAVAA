/*
Joff Thyres things.
 */
package com.bhis.thehackerbank

import okhttp3.OkHttpClient
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.experimental.xor

class SupportingMethods {
    fun bisect(data: String, k: Int): String {
        val buffer = ByteArray(data.length)
        for (i in 0 until data.length) buffer[i] = (data[i].code xor k).toByte()
        return Base64.getEncoder().encodeToString(buffer)
    }

    @Throws(UnsupportedEncodingException::class)
    fun transcode(data: String?, k: Int): String {
        val decoded = Base64.getDecoder().decode(data)
        val result = ByteArray(decoded.size)
        for (i in decoded.indices) result[i] = (decoded[i] xor k.toByte()).toByte()
        return String(result, StandardCharsets.UTF_8)
    }

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
}
