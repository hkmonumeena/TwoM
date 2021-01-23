package com.twom

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*val list = listOf("FirstName" to "monu",
        "LastName" to "meena",
        "email" to "monu@m.com",
        "phone" to "1234567890"
        )*/

        val list = listOf("category_id" to "1", "subcategory_id" to "1")
        TwoMonu.url("https://maestrosinfotech.com/rushabhbusinesscentre/api/process.php?action=show_product")
        TwoMonu.setContentType(TwoMonu.application_x_www_form_urlencoded)
        TwoMonu.bodyParameterList(list)
        TwoMonu.PostExecute.get { result, error ->
            val test = findViewById<TextView>(R.id.test)
            test?.text = "d" + result
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
}