package org.trasalby.Alamgwan
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.find
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var Breakfast : TextView
    lateinit var Lunch : TextView
    lateinit var Dinner : TextView
    lateinit var Week : TextView

    private val htmlPageUrl = "https://newgh.gnu.ac.kr/dorm/ad/fm/foodmenu/selectFoodMenuView.do"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Breakfast = findViewById(R.id.Breakfast)
        Lunch = findViewById(R.id.Lunch)
        Dinner = findViewById(R.id.Dinner)
        Week = findViewById(R.id.week)

        val jsoupAsyncTask: JsoupAsyncTask = JsoupAsyncTask()
        jsoupAsyncTask.execute()
    }
    private inner class JsoupAsyncTask :
        AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        protected override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val doc: Document = Jsoup.connect(htmlPageUrl).get()
                val week = doc.select("span.txt_p")
                val test : String = Html.fromHtml(week.toString()).toString();


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