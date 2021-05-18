package com.sti.ppam.momadu.ui.gallery

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.MonthScrollListener
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.sti.ppam.momadu.DatabaseHelper
import com.sti.ppam.momadu.R
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment() {

    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private lateinit var yearMonth:YearMonth
    var currMarkedDate: LocalDate? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scheduleListAdapter = ScheduleListAdapter(mutableListOf(), ivNoSchedule)

        rvScheduleList.adapter = scheduleListAdapter
        rvScheduleList.addItemDecoration(ScheduleListDecoration(resources.getDimension(R.dimen.schedule_item_margin).toInt()))
        rvScheduleList.layoutManager = LinearLayoutManager(this.context)
        class ScheduleCalendarDayViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendarDayText)

            // With ViewBinding
            // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    val a = currMarkedDate
                    if (currMarkedDate != null){
                        currMarkedDate = null
                    }

                    currMarkedDate = day.date
                    cvSchedule.notifyDateChanged(day.date)
                    if (a != null)
                        cvSchedule.notifyDateChanged(a)
                    scheduleListAdapter.clearSchedules()
                    val db = DatabaseHelper(view.context)
                    val cal = Calendar.getInstance()
                    cal.set(day.date.year, day.date.month.ordinal, day.day)
                    val sch = db.getSchForDay(cal)
                    if (sch.isNotEmpty()) {
                        scheduleListAdapter.addSchedules(sch)
                    }
                }
            }
        }
        cvSchedule.dayBinder = object : DayBinder<ScheduleCalendarDayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = ScheduleCalendarDayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: ScheduleCalendarDayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                textView.alpha = if (day.owner == DayOwner.THIS_MONTH) 1f else 0.3f
                if(day.date == LocalDate.now()){
                    textView.setTextColor(ResourcesCompat.getColor(resources, R.color.pink, null))
                }
                if(day.date.dayOfWeek == DayOfWeek.SUNDAY){
                    textView.setTextColor(Color.RED)
                }
                val cal = Calendar.getInstance()
                cal.set(day.date.year, day.date.month.ordinal, day.day)

                when {
                    day.date == currMarkedDate -> {
                        container.view.background = ResourcesCompat.getDrawable(resources, R.drawable.background_circle, null)
                    }
                    DatabaseHelper(cvSchedule.context).getSchForDay(cal).isNotEmpty() -> {
                        container.view.background = ResourcesCompat.getDrawable(resources, R.drawable.item_schedule_bg, null)
                    }
                    else -> {
                        container.view.background = null
                    }
                }
            }
        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.headerTextView)
        }
        cvSchedule.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }
        cvSchedule.monthScrollListener = object : MonthScrollListener {
            override fun invoke(p1: CalendarMonth) {
                val cal = Calendar.getInstance()
                cal.set(p1.year, p1.month, 1)
                //markMonth(cal)
            }

        }
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        cvSchedule.setup(firstMonth, lastMonth, firstDayOfWeek)
        cvSchedule.scrollToMonth(currentMonth)
        btnScheduleAddNew.setOnClickListener{
            val intent = Intent(this.context, ScheduleAddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getSchedulesDay(view: View, year: Int, month: Int, dayOfMMonth: Int) {
        scheduleListAdapter.clearSchedules()
        val db = DatabaseHelper(view.context)
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMMonth)
        val sch = db.getSchForDay(cal)
        if (sch.isNotEmpty()) {
            scheduleListAdapter.addSchedules(sch)
        }
    }

    /*fun markMonth(cal: Calendar) {
        val leftoverDates = ArrayList<DateData>()
        leftoverDates.addAll(cvSchedule.markedDates.all)
        val a = leftoverDates.iterator()
        while (a.hasNext()) {
            cvSchedule.unMarkDate(a.next())
        }
        val c = context
        cvSchedule.setMarkedStyle(MarkStyle.DOT)
        if (c != null){
            val db = DatabaseHelper(c)
            val sch = db.getSchForMonth(cal)
            if (sch.isNotEmpty()) {
                for (s in sch) {
                    cal.time = Date(s.startDate)
                    val r = TimeUnit.DAYS.convert(s.endDate - s.startDate, TimeUnit.MILLISECONDS).toInt()
                    for( i in 0..r){
                        val y = cal.get(Calendar.YEAR)
                        val m = cal.get(Calendar.MONTH)
                        val d = cal.get(Calendar.DAY_OF_MONTH)
                        cvSchedule.markDate(y, m, d)

                        cal.add(Calendar.DAY_OF_MONTH, 1)
                    }
                }
            }
        }
    }*/
}