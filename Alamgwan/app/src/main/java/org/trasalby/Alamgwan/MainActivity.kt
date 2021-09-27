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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.db.NULL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import android.text.method.ScrollingMovementMethod
import android.widget.Toast


//네트워크사용에서 비동기로 진행해야 한다는건 이해
//비동기는 어떻게 사용해야 하는가
// async 와 코루틴의 사용
//코틀린에서 코루틴 사용법
//크롤링된 데이터들을 구분하는 방법


class MainActivity : AppCompatActivity() {
    lateinit var Breakfast: TextView
    lateinit var Lunch: TextView
    lateinit var Dinner: TextView
    lateinit var Week: TextView
    lateinit var Previous: Button
    lateinit var Next: Button
    lateinit var Day: TextView
    var week: String = ""
    val cal = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Breakfast = findViewById(R.id.Breakfast)
        Breakfast.movementMethod = ScrollingMovementMethod()
        Lunch = findViewById(R.id.Lunch)
        Dinner = findViewById(R.id.Dinner)
        Day = findViewById(R.id.Day)
        Week = findViewById(R.id.week)
        Previous = findViewById(R.id.previous)
        Next = findViewById(R.id.next)
        cal.time = Date()

        val df: DateFormat = SimpleDateFormat("MM월 dd일")
        Day.text = df.format(cal.time)
        var meal_menuList : ArrayList<ArrayList<Element>>

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
                meal_menuList = crawlingMenu()
            }
            var weekNum = cal.get(Calendar.DAY_OF_WEEK)-1
            setMenu(Breakfast, Lunch, Dinner, meal_menuList, weekNum)
            Next.setOnClickListener {
                if(weekNum <6){
                    cal.add(Calendar.DATE, +1)
                    weekNum += 1
                    Day.text = df.format(cal.time)
                    setMenu(Breakfast, Lunch, Dinner, meal_menuList, weekNum)
                }else{
                    Toast.makeText(applicationContext, "마지막 요일입니다.", Toast.LENGTH_SHORT).show()
                }

            }
            Previous.setOnClickListener {
                if(weekNum > 0){
                    cal.add(Calendar.DATE, -1)
                    weekNum -= 1
                    Day.text = df.format(cal.time)
                    setMenu(Breakfast, Lunch, Dinner, meal_menuList, weekNum)
                }else{
                    Toast.makeText(applicationContext, "첫번째 요일입니다.", Toast.LENGTH_SHORT).show()
                }

            }
            Day.setOnClickListener {
                cal.time = Date()
                weekNum = cal.get(Calendar.DAY_OF_WEEK)-1
                Day.text = df.format(cal.time)
                setMenu(Breakfast, Lunch, Dinner, meal_menuList, weekNum)
            }
        }
    }
}

fun setMenu(Breakfast : TextView, Lunch : TextView, Dinner : TextView, meal_menuList : ArrayList<ArrayList<Element>>, weekNum : Int){

    Breakfast.text = Html.fromHtml(meal_menuList[0][weekNum].toString())
    Lunch.text = Html.fromHtml(meal_menuList[1][weekNum].toString())
    Dinner.text = Html.fromHtml(meal_menuList[2][weekNum].toString())
}

fun crawlingMenu() : ArrayList<ArrayList<Element>>{

    val URL : String = "https://newgh.gnu.ac.kr/dorm/ad/fm/foodmenu/selectFoodMenuView.do"
    val doc : Document = Jsoup.connect(URL).get()
    val elements : Elements = doc.select("div.BD_table.scroll_gr table tbody tr td")

    var meal_menuList = ArrayList<ArrayList<Element>>()
    var Break_menuList = ArrayList<Element>()
    var Lunch_menuList = ArrayList<Element>()
    var Dinner_menuList = ArrayList<Element>()


    for (i in 0..2) {
        for(j in 0..6){
            if(i==0){
                Break_menuList.add(elements[j])
            }
            if(i==1){
                Lunch_menuList.add(elements[j+7])
            }
            else{
                Dinner_menuList.add(elements[j+14])
            }
        }
    }
    meal_menuList.add(Break_menuList)
    meal_menuList.add(Lunch_menuList)
    meal_menuList.add(Dinner_menuList)
    return meal_menuList
}
