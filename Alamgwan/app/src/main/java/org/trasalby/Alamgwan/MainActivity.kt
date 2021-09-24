package org.trasalby.Alamgwan

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import kotlin.concurrent.thread
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    lateinit var Breakfast : TextView
    lateinit var Lunch : TextView
    lateinit var Dinner : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Breakfast = findViewById(R.id.Breakfast)
        Lunch = findViewById(R.id.Lunch)
        Dinner = findViewById(R.id.Dinner)

        Thread(Runnable {
            val url = "https://naver.com"
            val doc = Jsoup.connect(url).get()
            var elements = doc.select("span.an_txt")


            Log.d("qwer","qwer"+ "test")
        })



    }


}