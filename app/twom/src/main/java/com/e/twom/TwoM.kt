package com.e.twom

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object TwoM {

    private var urlmain: String? = null
    private var bodyparams = JSONObject()
    private  var headerMap = HashMap<String, String>()
    fun bodyParameter(key: String, value: String): JSONObject {
        bodyparams.put(key, value)
        return bodyparams!!
    }
    fun headerParameter(key: String, value: String) {
        headerMap[key] = value
    }
    fun post(url: String): String {
        urlmain = url
        return urlmain!!
    }
    fun get(url: String): String {
        urlmain = url
        return urlmain!!
    }

    class PostExecute() {
        val url: String = urlmain!!
        fun get(myCallback: (result: String?) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val httpURlConnection = URL(url).openConnection() as HttpURLConnection
                httpURlConnection.requestMethod = "POST"
                httpURlConnection.readTimeout = 60000
                httpURlConnection.connectTimeout = 60000
                httpURlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                if (headerMap != null) {
                    for ((key, value) in headerMap!!.entries) {
                        httpURlConnection.setRequestProperty(key, value)
                    }
                }
                httpURlConnection.doInput = true
                httpURlConnection.doOutput = true
                val os: OutputStream = httpURlConnection.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(encodeParams(bodyparams))
                writer.flush()
                writer.close()
                os.close()
                httpURlConnection.connect()
                val data = httpURlConnection.inputStream.bufferedReader().readText()
                httpURlConnection.disconnect()
                if (httpURlConnection.responseCode == 200) {
                    GlobalScope.launch (Dispatchers.Main){
                        myCallback.invoke(data)
                    }

                }
                else {

                }

            }

        }
    }
    class GetExecute() {
        val url: String = urlmain!!
        fun get(myCallback: (result: String?) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val httpURlConnection = URL(url).openConnection() as HttpURLConnection
                httpURlConnection.requestMethod = "GET"
                httpURlConnection.readTimeout = 60000
                httpURlConnection.connectTimeout = 60000
                httpURlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                if (headerMap != null) {
                    for ((key, value) in headerMap!!.entries) {
                        httpURlConnection.setRequestProperty(key, value)
                    }
                }
                httpURlConnection.doInput = true
                httpURlConnection.doOutput = true
                val os: OutputStream = httpURlConnection.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(encodeParams(bodyparams))
                writer.flush()
                writer.close()
                os.close()
                httpURlConnection.connect()
                val data = httpURlConnection.inputStream.bufferedReader().readText()
                httpURlConnection.disconnect()
                if (httpURlConnection.responseCode == 200) {
                    myCallback.invoke(data)
                }

            }

        }
    }

    private fun encodeParams(params: JSONObject): String? {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }
}