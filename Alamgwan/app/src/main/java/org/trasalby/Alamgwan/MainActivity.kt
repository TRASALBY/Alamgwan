package org.trasalby.Alamgwan
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

//네트워크사용에서 비동기로 진행해야 한다는건 이해
//비동기는 어떻게 사용해야 하는가
// async 와 코루틴의 사용
//코틀린에서 코루틴 사용법
//크롤링된 데이터들을 구분하는 방법


class MainActivity : AppCompatActivity() {
    lateinit var Breakfast : TextView
    lateinit var Lunch : TextView
    lateinit var Dinner : TextView
    lateinit var Week : TextView
    lateinit var Previous : Button
    lateinit var Next : Button
    lateinit var Day : TextView
    var week : String = ""
    val cal = Calendar.getInstance()

    private val htmlPageUrl = "https://newgh.gnu.ac.kr/dorm/ad/fm/foodmenu/selectFoodMenuView.do"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Breakfast = findViewById(R.id.Breakfast)
        Lunch = findViewById(R.id.Lunch)
        Dinner = findViewById(R.id.Dinner)
        Day = findViewById(R.id.Day)
        Week = findViewById(R.id.week)
        Previous = findViewById(R.id.previous)
        Next = findViewById(R.id.next)
        cal.time = Date()

        val df : DateFormat = SimpleDateFormat("MM월 dd일")
        Day.text = df.format(cal.time)

        val jsoupAsyncTask: JsoupAsyncTask = JsoupAsyncTask()
        jsoupAsyncTask.execute()
        while (true){
            Next.setOnClickListener {
                cal.add(Calendar.DATE,+1)
                Day.text = df.format(cal.time)
            }
            Previous.setOnClickListener {
                cal.add(Calendar.DATE,-1)
                Day.text = df.format(cal.time)
            }
            Day.setOnClickListener{
                cal.time = Date()
                Day.text = df.format(cal.time)
            }
            if(!week.equals("")){
                Week.text = week
                break
            }
        }
    }
    private inner class JsoupAsyncTask :
        AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        protected override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val doc: Document = Jsoup.connect(htmlPageUrl).get()
                val week_day = doc.select("span.txt_p")
                week = Html.fromHtml(week_day.toString()).toString();



                val elements = doc.select("div.foodLst_wrap").select("tr")[1]


                Log.d("Alamgwan","test"+ elements.text() )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {

        }
    }

}