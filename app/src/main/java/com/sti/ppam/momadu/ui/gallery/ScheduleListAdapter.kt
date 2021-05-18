package com.sti.ppam.momadu.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sti.ppam.momadu.R
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.text.SimpleDateFormat
import java.util.*

class ScheduleListAdapter (
    private val schedules: MutableList<Schedule>, var emptyView: View
) : RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        emptyView.visibility = View.VISIBLE
        return ScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_schedule,
                parent,
                false
            )
        )
    }

    fun addSchedule(schedule: Schedule) {
        emptyView.visibility = View.INVISIBLE
        schedules.add(schedule)
        notifyItemChanged(schedules.size - 1)
    }

    fun addSchedules(schedule: List<Schedule>) {
        emptyView.visibility = View.INVISIBLE
        schedules.addAll(schedule)
        notifyItemChanged(schedules.size - 1)
    }
    fun clearSchedules() {
        emptyView.visibility = View.VISIBLE
        schedules.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val curSchedule = schedules[position]
        val scheduleStart = Calendar.getInstance()
        scheduleStart.timeInMillis = curSchedule.startDate
        val scheduleEnd = Calendar.getInstance()
        scheduleEnd.timeInMillis = curSchedule.endDate
        val scheduleLength = SimpleDateFormat("HH:mm", Locale.getDefault()).format(scheduleStart.time) + " - " +
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(scheduleEnd.time)
        holder.itemView.apply{
            tvScheduleTitle.text = curSchedule.name
            tvScheduleDate.text = scheduleLength
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }
}