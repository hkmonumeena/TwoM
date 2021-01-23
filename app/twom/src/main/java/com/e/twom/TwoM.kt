package com.twom

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.*
import javax.net.ssl.HttpsURLConnection


object TwoM {

    private var urlmain: String? = null
    private var bodyparams = JSONObject()
    private  var headerMap = HashMap<String, String>()
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
    fun post(url: String): String {
        urlmain = url
        return urlmain!!
    }
    fun get(url: String): String {
        urlmain = url
        return urlmain!!
    }

    class PostExecute(context: Context) {
        private val url: String = urlmain!!
        private val context = context
        private fun isNetworkAvailable(): Boolean? {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected
            } else false

        }
        @Throws(IOException::class, URISyntaxException::class)
        private fun connect(uri: String) {
            try {
                val url = URL(uri)
                val connection = url.openConnection() as HttpsURLConnection


            } catch (exception: ConnectException) {
                // Output expected ConnectException.
                //  Logging.log(exception)
            } catch (throwable: Throwable) {
                // Output unexpected Throwables.
                //  Logging.log(throwable, false)
            }
        }

        fun get(myCallback: (result: String?, error: String?) -> Unit) {

            try {

                var httpURlConnection = URL(url).openConnection() as HttpURLConnection
                if (isNetworkAvailable() == true) {
                    GlobalScope.launch(Dispatchers.IO) {
                        httpURlConnection.requestMethod = "POST"
                        httpURlConnection.setRequestProperty("Connection", "Keep-Alive");
                        httpURlConnection.setRequestProperty("Cache-Control", "no-cache");
                        httpURlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        httpURlConnection.readTimeout = 60000
                        httpURlConnection.connectTimeout = 100

                        try {
                            httpURlConnection.doInput = true
                            httpURlConnection.doOutput = true
                            val os: OutputStream = httpURlConnection.outputStream
                            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                            writer.write(encodeParams(bodyparams))
                            writer.flush()
                            writer.close()
                            os.close()

                        } catch (d :SocketTimeoutException) {
                            myCallback.invoke(null,d.toString())

                        } catch ( exception :IOException) {
                            myCallback.invoke(null,exception.toString())

                        }



                        for ((key, value) in headerMap!!.entries) {
                            httpURlConnection.setRequestProperty(key, value)
                        }

                        httpURlConnection.connect()
                        val data = httpURlConnection.inputStream.bufferedReader().readText()
                        httpURlConnection.disconnect()

                        GlobalScope.launch(Dispatchers.Main) {
                            if (httpURlConnection.responseCode == 200) {
                                myCallback.invoke(data, null)

                            } else {
                                //  throw IOException("Server returned non-OK status: ${httpURlConnection.responseCode}")
                                myCallback.invoke(null, "Error to connect with this url check all the fields -  ${httpURlConnection.responseCode}")

                            }

                        }
                    }

                } else {
                    try {
                        myCallback.invoke(null, "Server returned non-OK status ${httpURlConnection.responseCode}: Not Internet")
                    } catch (e: Exception) {
                        myCallback.invoke(null, "${e}: Not Internet")
                    }

                }


            } catch (exception: ConnectException) {
                // Output expected ConnectException.
                myCallback.invoke(null, exception.toString())
            } catch (throwable: Throwable) {
                // Output unexpected Throwables.
                //  Logging.log(throwable, false)
                myCallback.invoke(null, throwable.toString())
            }


        }


    }    class GetExecute() {
        val url: String = urlmain!!

        fun get(myCallback: (result: String?, error: String?) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val httpURlConnection = URL(url).openConnection() as HttpURLConnection
                httpURlConnection.requestMethod = "GET"
                httpURlConnection.readTimeout = 60000
                httpURlConnection.connectTimeout = 60000
                httpURlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                for ((key, value) in headerMap!!.entries) {
                    httpURlConnection.setRequestProperty(key, value)
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
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(data, null)
                    }
                }

                else {
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(null, "Error to connect with this url check all the fields")
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