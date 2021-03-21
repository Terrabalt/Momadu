package com.sti.ppam.momadu.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sti.ppam.momadu.R
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.util.*

class ScheduleListAdapter (
    private val schedules: MutableList<Schedule>
) : RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_schedule,
                parent,
                false
            )
        )
    }

    fun addSchedule(schedule: Schedule) {
        schedules.add(schedule)
        notifyItemChanged(schedules.size - 1)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val curSchedule = schedules[position]
        val scheduleStart = Calendar.getInstance()
        scheduleStart.timeInMillis = curSchedule.startDate
        val scheduleEnd = Calendar.getInstance()
        scheduleEnd.timeInMillis = curSchedule.endDate
        val scheduleLength = scheduleStart.get(Calendar.HOUR_OF_DAY).toString() +
                ':' + scheduleStart.get(Calendar.MINUTE).toString() + " - " +
                scheduleEnd.get(Calendar.HOUR_OF_DAY).toString() +
                ':' + scheduleEnd.get(Calendar.MINUTE).toString()
        holder.itemView.apply{
            tvScheduleTitle.text = curSchedule.name
            tvScheduleDate.text = scheduleLength
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }
}