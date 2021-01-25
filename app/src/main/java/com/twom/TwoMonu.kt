package com.twom

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.*
import javax.net.ssl.HttpsURLConnection


object TwoMonu {

    private var urlmain: String? = null
    private var setContentType: String? = "application/x-www-form-urlencoded"
    val application_x_www_form_urlencoded = "application/x-www-form-urlencoded"
    val application_json_utf_8 = "application/json; utf-8"
    private var bodyparams = JSONObject()
    private var headerMap = HashMap<String, String>()

    fun bodyParameterList(hashMap: List<Pair<String, String>>): JSONObject {
        for ((key, value) in hashMap) {
            Log.e("gfdgfgffgr", "bodyParameterList: $key--$value")
            bodyparams.put(key, value)
        }

        return bodyparams!!
    }

    fun bodyParameter(key: String, value: String): JSONObject {
        bodyparams.put(key, value)
        return bodyparams!!
    }

    fun headerParameter(key: String, value: String) {
        headerMap[key] = value
    }

    fun setContentType(Content_Type: String = "application/x-www-form-urlencoded") {
        setContentType = Content_Type
    }

    fun url(url: String): String {
        urlmain = url
        return urlmain!!
    }

    object PostExecute {
        private val url: String = urlmain!!
        fun get(myCallback: (result: String?, error: String?) -> Unit) {
            var httpURlConnection: HttpURLConnection? = null
            var writer: BufferedWriter? = null
            var os: OutputStream? = null
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    httpURlConnection = URL(url).openConnection() as HttpURLConnection
                    httpURlConnection?.requestMethod = "POST"
                    httpURlConnection?.setRequestProperty("Connection", "Keep-Alive");
                    httpURlConnection?.setRequestProperty("Cache-Control", "no-cache");
                    httpURlConnection?.setRequestProperty("Content-Type", setContentType)
                    for ((key, value) in headerMap.entries) {
                        httpURlConnection?.setRequestProperty(key, value)
                    }
                    httpURlConnection?.readTimeout = 60000
                    httpURlConnection?.connectTimeout = 60000
                    httpURlConnection?.doInput = true
                    httpURlConnection?.doOutput = true
                    os = httpURlConnection?.outputStream
                    writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer?.write(encodeParams(bodyparams))
                    writer?.flush()
                    writer?.close()
                    os?.close()
                    httpURlConnection?.connect()
                    val data = httpURlConnection?.inputStream?.bufferedReader()?.readText()
                    GlobalScope.launch(Dispatchers.Main) {
                        if (httpURlConnection?.responseCode == 200) {
                            myCallback.invoke(data, null)
                            httpURlConnection?.disconnect()
                        }
                    }

                } catch (se: SocketTimeoutException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Twom Error SocketTimeoutException -${se}")
                    }

                } catch (e: IOException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Twom Error IOException -${e}")
                    }

                } catch (e: java.lang.Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Twom Error Exception -${e}")
                    }

                } finally {
                    if (httpURlConnection != null) {
                        httpURlConnection?.disconnect();
                    }

                    if (writer != null) {
                        try {
                            writer?.close()
                        } catch (ex: IOException) {
                        }
                    }

                }

            }

        }


    }

    object GetExecute {
        val url: String = urlmain!!

        fun get(myCallback: (result: String?, error: String?) -> Unit) {
            var httpURlConnection: HttpURLConnection? = null
            var writer: BufferedWriter? = null
            var os: OutputStream? = null
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    httpURlConnection = URL(url).openConnection() as HttpURLConnection
                    httpURlConnection?.requestMethod = "GET"
                    httpURlConnection?.setRequestProperty("Connection", "Keep-Alive");
                    httpURlConnection?.setRequestProperty("Cache-Control", "no-cache");
                    httpURlConnection?.setRequestProperty("Content-Type", setContentType)
                    // httpURlConnection?.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvc2FtYW0uY29tIiwidWlkIjoyNDQsInR5cGUiOiJjbGllbnQiLCJpYXQiOjE2MTEzODI2MzAsImV4cCI6MTY0MjQ4NjYzMH0.h5J2CMRo9MvGCW2ymxqHjxhz7na3AqDNu1HvHRjd570")
                    for ((key, value) in headerMap.entries) {
                        httpURlConnection?.setRequestProperty(key, value)
                    }
                    httpURlConnection?.readTimeout = 60000
                    httpURlConnection?.connectTimeout = 60000
                    //     httpURlConnection?.doInput = false
                    httpURlConnection?.doOutput = false
                    /*  os = httpURlConnection?.outputStream
                            writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                            writer?.write(encodeParams(bodyparams))
                            writer?.flush()
                            writer?.close()
                            os?.close()*/
                    httpURlConnection?.connect()
                    val data = httpURlConnection?.inputStream?.bufferedReader()?.readText()

                    GlobalScope.launch(Dispatchers.Main) {

                        if (httpURlConnection?.responseCode == 200) {
                            myCallback.invoke(data, null)
                            httpURlConnection?.disconnect()
                        }
                    }

                } catch (se: SocketTimeoutException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Twom Error SocketTimeoutException -${se}")
                    }

                } catch (e: IOException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Twom Error IOException -${e}")
                    }

                } catch (e: java.lang.Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Twom Error Exception -${e}")
                    }

                } finally {
                    if (httpURlConnection != null) {
                        httpURlConnection?.disconnect();
                    }

                    if (writer != null) {
                        try {
                            writer?.close()
                        } catch (ex: IOException) {
                        }
                    }

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