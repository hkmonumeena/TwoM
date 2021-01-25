package com.twom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smartlib.quickimage.QuickImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    val TIME_OUT = 8 * 1000 //timeout time

    val CHARSET = "utf-8" //encoding format

    val PREFIX = "--" //prefix

    val BOUNDARY = UUID.randomUUID().toString() //Boundary identifier Randomly generated

    val CONTENT_TYPE = "multipart/form-data" //content type

    val LINE_END = "\r\n" //newline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = listOf(
                "FirstName" to "Monu",
                "LastName" to "Meena",
                "email" to "monu@m.com",
                "phone" to "1234",
                "address" to "new address - iraq",
                "googleAddress" to "Google Address String....",
                "Latitude" to "44.222222",
                "Longitude" to "34.333333"
        )
        val snhn = """{
    "day": 20,
    "month": 6,
    "year": 1996,
    "hour": 17,
    "min": 9,
    "lat": 23.25,
    "lon": 77.75,
    "tzone": 5.5,
    "varshaphal_year": 2020
}"""
        val jsonObject = JSONObject(snhn)


        //  val list = listOf("category_id" to "1", "subcategory_id" to "1")
        TwoMonu.url("https://json.astrologyapi.com/v1/varshaphal_details")
        TwoMonu.setContentType(TwoMonu.application_json_utf_8)
        /*  TwoMonu.headerParameter(
              "Authorization",
              "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvc2FtYW0uY29tIiwidWlkIjoyNDQsInR5cGUiOiJjbGllbnQiLCJpYXQiOjE2MTEzODI2MzAsImV4cCI6MTY0MjQ4NjYzMH0.h5J2CMRo9MvGCW2ymxqHjxhz7na3AqDNu1HvHRjd570"
          )*/
        TwoMonu.authenticationHeader("615561", "b362869114090dee784ae025d01ff3d2")
        TwoMonu.jsonBody(jsonObject)
        TwoMonu.PostExecute.get { result, error ->
            val test = findViewById<TextView>(R.id.test)
            test?.text = "d" + result
            test?.setOnClickListener {
                QuickImagePicker.singleImageDialog(this, 133)
            }
            Log.e("dmsjgh", "$result")
            Log.e("dmsjgh", "error: $error")
        }

        /*  GlobalScope.launch (Dispatchers.IO) {
              val url = URL("https://api.eaglesouq.com/client/profile&FirstName=monu")
              val con: HttpURLConnection = url.openConnection() as HttpURLConnection
              con.requestMethod = "POST"
              con.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvc2FtYW0uY29tIiwidWlkIjoyNDQsInR5cGUiOiJjbGllbnQiLCJpYXQiOjE2MTEzODI2MzAsImV4cCI6MTY0MjQ4NjYzMH0.h5J2CMRo9MvGCW2ymxqHjxhz7na3AqDNu1HvHRjd570")
              con.setReadTimeout(15000)
              con.setConnectTimeout(15000)
              con.setDoOutput(false)
              val data = con?.inputStream?.bufferedReader()?.readText()

              Log.e("dnsjd", "onCreate: "+data )
          }*/


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 132 && resultCode == Activity.RESULT_OK) {
            val myImage = QuickImagePicker.getCompressImg(data?.data, data?.extras, "folderName", this)
            val myImageOriginalSize = QuickImagePicker.getWithoutCompressImage(this, data?.data!!)

        }
        if (requestCode == 133 && resultCode == Activity.RESULT_OK) {

             var params = HashMap<String, String>()
            params["title"] = "title"
            params["title_en"] = "title"
            params["details"] = "title"
            params["details_en"] = "title"
            params["price"] = "2000"
            params["oldprice"] = "2000"
            params["oldprice"] = "2000"
            params["color"] = "RED"
            params["size"] = "RED"
            params["qty"] = "20"



           // postRequest(params)
            val myImage = QuickImagePicker.getCompressImg(data?.data, data?.extras, "folderName", this)
            Log.e("Dsdsd", "onActivityResult: "+ myImage.absolutePath )
            var filemap = HashMap<String, File>()
            filemap["images[]"] = myImage



            GlobalScope.launch(Dispatchers.IO) {
          // val getme = TwoMonu.multipartRequest("https://api.eaglesouq.com/merchant/prod/", params, myImage.absolutePath, "images[]", myImage,"image/jpg")
                postRequest(params,filemap)

                //Log.e("ddsdds", "onActivityResult: " + getme)
            }

        }
    }

    fun postRequest(strParams: Map<String, String>, fileParams: Map<String, File?>) {
        Thread {
            var conn: HttpURLConnection? = null
            try {
                Log.e("fdsfdf", "postRequest: "+fileParams )
                val url = URL("https://api.eaglesouq.com/merchant/prod/")
                conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn!!.readTimeout = TIME_OUT
                conn.connectTimeout = TIME_OUT
                conn.doOutput = true
                conn.doInput = true
                conn.useCaches = false //Post requests cannot use cache
                // Set the request header parameters
                conn.setRequestProperty("Connection", "Keep-Alive")
                conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0")
                conn.setRequestProperty("Charset", "UTF-8")
                conn.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvc2FtYW0uY29tIiwidWlkIjoyNDQsInR5cGUiOiJjbGllbnQiLCJpYXQiOjE2MTEzODI2MzAsImV4cCI6MTY0MjQ4NjYzMH0.h5J2CMRo9MvGCW2ymxqHjxhz7na3AqDNu1HvHRjd570")
                conn.setRequestProperty("Content-Type", "$CONTENT_TYPE;boundary=$BOUNDARY")
                /**
                 * Request body
                 */
                /**
                 * Request body
                 */
                /**
                 * Request body
                 */
                /**
                 * Request body
                 */
                val dos = DataOutputStream(conn.outputStream)
                //getStrParams() is a
                dos.writeBytes(getStrParams(strParams).toString())
                dos.flush()

                //File Upload
                val fileSb = StringBuilder()
                for ((key, value) in fileParams) {
                    fileSb.append(PREFIX)
                            .append(BOUNDARY)
                            .append(LINE_END)
                            /**
                             * The key point here: the value inside the name is the key required by the server. Only the key can get the corresponding file.
                             * filename is the name of the file, including the suffix name. For example: abc.png
                             */
                            /**
                             * The key point here: the value inside the name is the key required by the server. Only the key can get the corresponding file.
                             * filename is the name of the file, including the suffix name. For example: abc.png
                             */
                            /**
                             * The key point here: the value inside the name is the key required by the server. Only the key can get the corresponding file.
                             * filename is the name of the file, including the suffix name. For example: abc.png
                             */
                            /**
                             * The key point here: the value inside the name is the key required by the server. Only the key can get the corresponding file.
                             * filename is the name of the file, including the suffix name. For example: abc.png
                             */
                            .append("Content-Disposition: form-data; name=\"file\"; filename=\""
                                    + key + "\"" + LINE_END)
                            .append("Content-Type: image/jpg$LINE_END") //The ContentType here is different from the Content-Type in the request header
                            .append("Content-Transfer-Encoding: 8bit$LINE_END")
                            .append(LINE_END) // After the parameter header is set, two line breaks are required, and then the parameter content is
                    dos.writeBytes(fileSb.toString())
                    dos.flush()
                    val `is`: InputStream = FileInputStream(value)
                    val buffer = ByteArray(1024)
                    var len = 0
                    while (`is`.read(buffer).also { len = it } != -1) {
                        dos.write(buffer, 0, len)
                    }
                    `is`.close()
                    dos.writeBytes(LINE_END)
                }
                dos.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END)
                dos.flush()
                dos.close()
                Log.e("fjdkfjdk", "postResponseCode() = " + conn.responseCode)
                if (conn.responseCode == 200) {
                    val `in` = conn.inputStream
                    val reader = BufferedReader(InputStreamReader(`in`))
                    var line: String? = null
                    val response = StringBuilder()
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    Log.e("fjdkfjdk", "run: $response")
                }
            } catch (e: Exception) {
                e.printStackTrace()



                Log.e("fjdkfjdk", "run: $e")
            } finally {
                conn?.disconnect()
            }
        }.start()
    }

    /**
     * Encode the post parameters
     */
    private fun getStrParams(strParams: Map<String, String>): java.lang.StringBuilder? {
        val strSb = java.lang.StringBuilder()
        for ((key, value) in strParams) {
            strSb.append(PREFIX)
                    .append(BOUNDARY)
                    .append(LINE_END)
                    .append("Content-Disposition: form-data; name=\"$key\"$LINE_END")
                    .append("Content-Type: text/plain; charset=$CHARSET$LINE_END")
                    .append("Content-Transfer-Encoding: 8bit$LINE_END")
                    .append(LINE_END) // After the parameter header is set, two line breaks are required, and then the parameter content is
                    .append(value)
                    .append(LINE_END)
        }
        return strSb
    }

}