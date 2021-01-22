package com.twom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = listOf("category_id" to "2","subcategory_id" to "2")
        TwoM.post("https://maestrosinfotech.com/rushabhbusinesscentre/api/process.php?action=show_product")
        TwoM.bodyParameterList(list)
        TwoM.PostExecute(this).get{
            result, error ->
            Log.e("dmsjgh", "result: $result")
            Log.e("dmsjgh", "error: $error")
        }
    }
}