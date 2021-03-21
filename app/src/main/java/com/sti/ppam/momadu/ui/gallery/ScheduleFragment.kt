package com.sti.ppam.momadu.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sti.ppam.momadu.R
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment : Fragment() {

    private lateinit var scheduleListAdapter: ScheduleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scheduleListAdapter = ScheduleListAdapter(mutableListOf())

        rvScheduleList.adapter = scheduleListAdapter
        rvScheduleList.addItemDecoration(ScheduleListDecoration(resources.getDimension(R.dimen.schedule_item_margin).toInt()))
        rvScheduleList.layoutManager = LinearLayoutManager(this.context)

        cvSchedule.isClickable = true
        btnAddSchedule.setOnClickListener{
            val schedule = Schedule("Test", cvSchedule.date, cvSchedule.date)
            scheduleListAdapter.addSchedule(schedule)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }
}