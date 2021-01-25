package com.twom

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLEncoder


object TwoMonu {

    private var urlmain: String? = null
    private var encodeAuth: String? = null
    private var checkBodytype: String? = null
    private var setContentType: String? = "application/x-www-form-urlencoded"
    val application_x_www_form_urlencoded = "application/x-www-form-urlencoded"
    val Authorization ="Authorization"
    val application_json_utf_8 = "application/json"
    private var bodyparams = JSONObject()
    private var jsonBody = JSONObject()
    private var headerMap = HashMap<String, String>()

    fun bodyParameterList(hashMap: List<Pair<String, String>>): JSONObject {
        for ((key, value) in hashMap) {
            bodyparams.put(key, value)
        }

        return bodyparams!!
    }
    fun bodyParameter(key: String, value: String): JSONObject {
        checkBodytype="bodyParameter"
        bodyparams.put(key, value)
        return bodyparams!!
    }

    fun jsonBody(jsonObject: JSONObject): JSONObject {
        jsonBody = jsonObject
        checkBodytype="jsonBody"
        return jsonObject
    }
    fun headerParameter(key: String, value: String) {
        headerMap[key] = value
    }
    fun authenticationHeader(username: String, password: String){
        val auth: String = "$username:$password"
        val data = auth.toByteArray()
         encodeAuth = "Basic " + Base64.encodeToString(data, Base64.DEFAULT)
        Log.e("Dsdss", "authenticationHeader: " + encodeAuth)
        headerMap[Authorization] = encodeAuth!!
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
                    httpURlConnection?.setRequestProperty("Accept", "application/json");
                    for ((key, value) in headerMap.entries) {
                        httpURlConnection?.setRequestProperty(key, value)
                    }
                    httpURlConnection?.readTimeout = 60000
                    httpURlConnection?.connectTimeout = 60000
                    httpURlConnection?.doInput = true
                    httpURlConnection?.doOutput = true
                    os = httpURlConnection?.outputStream
                    if (checkBodytype=="bodyParameter"){
                        writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                        writer?.write(encodeParams(bodyparams))
                        writer?.flush()
                        writer?.close()
                        os?.close()
                    } else if (checkBodytype=="jsonBody"){
                        writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                        writer?.write(jsonBody.toString())
                        writer?.flush()
                        writer?.close()
                        os?.close()
                    }



                    httpURlConnection?.connect()
                    val data = httpURlConnection?.inputStream?.bufferedReader()?.readText()
                    GlobalScope.launch(Dispatchers.Main) {
                        if (httpURlConnection?.responseCode == 200) {
                            myCallback.invoke(data, null)
                            httpURlConnection?.disconnect()
                        }
                    }

                } catch (se: SocketTimeoutException) {
                    val `in` = InputStreamReader(httpURlConnection?.errorStream)
                    var stringBuilder = StringBuilder()
                    val bufferedReader = BufferedReader(`in`)
                    if (bufferedReader != null) {
                        var cp: Int
                        while (bufferedReader.read().also { cp = it } != -1) {
                            stringBuilder.append(cp.toChar())
                        }
                        bufferedReader.close()
                    }
                    `in`.close()
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(
                            null,
                            "${stringBuilder}\n${httpURlConnection?.responseCode} \n${httpURlConnection?.responseMessage}\n${se}"
                        )
                    }

                } catch (e: IOException) {
                    val `in` = InputStreamReader(httpURlConnection?.errorStream)
                    var stringBuilder = StringBuilder()
                    val bufferedReader = BufferedReader(`in`)
                    if (bufferedReader != null) {
                        var cp: Int
                        while (bufferedReader.read().also { cp = it } != -1) {
                            stringBuilder.append(cp.toChar())
                        }
                        bufferedReader.close()
                    }
                    `in`.close()
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(
                            null,
                            "${stringBuilder}\n${httpURlConnection?.responseCode} \n${httpURlConnection?.responseMessage}\n Error IOException -${e}"
                        )
                    }

                } catch (e: java.lang.Exception) {
                    val `in` = InputStreamReader(httpURlConnection?.errorStream)
                    var stringBuilder = StringBuilder()
                    val bufferedReader = BufferedReader(`in`)
                    if (bufferedReader != null) {
                        var cp: Int
                        while (bufferedReader.read().also { cp = it } != -1) {
                            stringBuilder.append(cp.toChar())
                        }
                        bufferedReader.close()
                    }
                    `in`.close()
                    GlobalScope.launch(Dispatchers.Main) {
                        myCallback.invoke(
                            null,
                            "$stringBuilder\n${httpURlConnection?.responseCode} \n${httpURlConnection?.responseMessage} \n-${e}"
                        )
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

   // @Throws(CustomException::class)
    fun multipartRequest(
        urlTo: String?,
        parmas: Map<String, String?>,
        filepath: String,
        filefield: String,
        fileMimeType: String
    ): String? {
        var connection: HttpURLConnection? = null
        var outputStream: DataOutputStream? = null
        var inputStream: InputStream? = null
        val twoHyphens = "--"
        val boundary = "*****" + java.lang.Long.toString(System.currentTimeMillis()) + "*****"
        val lineEnd = "\r\n"
        var result: String? = ""
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        val buffer: ByteArray
        val maxBufferSize = 1 * 1024 * 1024
        val q = filepath.split("/".toRegex()).toTypedArray()
        val idx = q.size - 1
        return try {
            val file = File(filepath)
            val fileInputStream = FileInputStream(file)
            val url = URL(urlTo)
            connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0")
            connection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvc2FtYW0uY29tIiwidWlkIjo0NSwidHlwZSI6Im1lcmNoYW50IiwiaWF0IjoxNjA5MjY3ODg0LCJleHAiOjE2NDAzNzE4ODR9.ndCh2dINiROKo0tcQQjSSLO-2V50LZhIH2J3Ac-FGhE")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(twoHyphens + boundary + lineEnd)
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd)
            outputStream.writeBytes("Content-Type: $fileMimeType$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)
            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = ByteArray(bufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }
            outputStream.writeBytes(lineEnd)

            // Upload POST Data
            val keys = parmas.keys.iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = parmas[key]
                outputStream.writeBytes(twoHyphens + boundary + lineEnd)
                outputStream.writeBytes("Content-Disposition: form-data; name=\"$key\"$lineEnd")
                outputStream.writeBytes("Content-Type: text/plain$lineEnd")
                outputStream.writeBytes(lineEnd)
                outputStream.writeBytes(value)
                outputStream.writeBytes(lineEnd)
            }
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            if (200 != connection.responseCode) {
               // throw CustomException("Failed to upload code:" + connection.responseCode + " " + connection.responseMessage)
            }
            inputStream = connection.inputStream
            result = convertStreamToString(inputStream)
            fileInputStream.close()
            inputStream.close()
            outputStream.flush()
            outputStream.close()
            result
        } catch ( e:Exception) {
 throw (e)

        }
    }

    private fun convertStreamToString(`is`: InputStream?): String? {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = java.lang.StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }


}