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


class MainActivity : AppCompatActivity() {

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
val snhn= """{
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
        TwoMonu.url("https://api.eaglesouq.com/client/profile?&FirstName=monu")
        TwoMonu.setContentType(TwoMonu.application_json_utf_8)
        TwoMonu.headerParameter(
            "Authorization",
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvc2FtYW0uY29tIiwidWlkIjoyNDQsInR5cGUiOiJjbGllbnQiLCJpYXQiOjE2MTEzODI2MzAsImV4cCI6MTY0MjQ4NjYzMH0.h5J2CMRo9MvGCW2ymxqHjxhz7na3AqDNu1HvHRjd570"
        )
//TwoMonu.authenticationHeader("615561","b362869114090dee784ae025d01ff3d2")
        TwoMonu.PostExecute.get { result, error ->
            val test = findViewById<TextView>(R.id.test)
            test?.text = "d" + result
            test?.setOnClickListener {
                QuickImagePicker.singleImageDialog(this,133)
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
            val myImage = QuickImagePicker.getCompressImg(data?.data,data?.extras,"folderName",this)
            val myImageOriginalSize = QuickImagePicker.getWithoutCompressImage(this,data?.data!!)

        }
        if (requestCode == 133 && resultCode == Activity.RESULT_OK) {
            val params: MutableMap<String, String> = HashMap(2)
            params["title"] = "title"
            params["title_en"] = "title"
            params["details"] = "title"
            params["details_en"] = "title"
            params["price"] = "2000"
            params["oldprice"] = "2000"
            params["oldprice"] = "2000"
            params["color"] = "RED"
            params["size"] = "RED"
            params["qty"] = "RED"
            val myImage = QuickImagePicker.getCompressImg(data?.data,data?.extras,"folderName",this)
         GlobalScope.launch (Dispatchers.IO) {
             val getme =  TwoMonu.multipartRequest("https://api.eaglesouq.com/merchant/prod/",params,myImage.toString(),"images[]","image/jpg")
             Log.e("ddsdds", "onActivityResult: "+getme )
         }

        }
    }


}