package com.sti.ppam.momadu.ui.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sti.ppam.momadu.DatabaseHelper
import com.sti.ppam.momadu.R
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_add.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class ScheduleAddActivity : AppCompatActivity() {

    fun addNewSchedule(view : View) {
        val dStart = Calendar.getInstance()
        dStart.isLenient = true;
        dStart.set(dpScheduleAddStart.year, dpScheduleAddStart.month, dpScheduleAddStart.dayOfMonth, tpScheduleAddStart.hour, tpScheduleAddStart.minute)
        val dEnd = Calendar.getInstance()
        dEnd.set(dpScheduleAddEnd.year, dpScheduleAddEnd.month, dpScheduleAddEnd.dayOfMonth, tpScheduleAddEnd.hour, tpScheduleAddEnd.minute)
        val sch = Schedule(-1, mlvAddScheduleText.text.toString(), dStart.timeInMillis, dEnd.timeInMillis)
        //debugText.text = sch.name + ": "+ SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date.from(
        //    Instant.ofEpochMilli(sch.startDate))) + " - " + SimpleDateFormat("dd-MM-yyyy HH:mm").format(dEnd.time)

        val db = DatabaseHelper(this.baseContext)

        val success = db.addSchOne(sch)

        if(success) {
            Toast.makeText(this.baseContext, "Success", Toast.LENGTH_SHORT).show()
            finish()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_add)

    }
}