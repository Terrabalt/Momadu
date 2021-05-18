package com.sti.ppam.momadu

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sti.ppam.momadu.ui.gallery.Schedule
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper(val context : Context) :SQLiteOpenHelper(context, "sch_fin.db", null, 1) {
    private val COL_SCH = "SCHEDULE"
    private val COL_SCH_ID = "ID"
    private val COL_SCH_NAME = "NAME"
    private val COL_SCH_START = "START"
    private val COL_SCH_END = "END"
    private val COL_FIN = "FINANCE"
    private val COL_FIN_ID = "ID"
    private val COL_FIN_AMOUNT = "AMOUNT"
    private val COL_FIN_DATE = "DATE"
    private val COL_FIN_CATEGORY = "CATEGORY"
    private val COL_FIN_NOTES = "NOTES"

    override fun onCreate(db: SQLiteDatabase?) {
        val schTableCreatorStatement = "CREATE TABLE $COL_SCH ($COL_SCH_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_SCH_NAME TEXT, $COL_SCH_START INTEGER, ${COL_SCH_END} INTEGER)"
        val finTableCreatorStatement = "CREATE TABLE $COL_FIN ($COL_FIN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_FIN_AMOUNT INT, $COL_FIN_DATE INTEGER, $COL_FIN_CATEGORY TEXT, $COL_FIN_NOTES TEXT)"

        if (db != null) {
            db.execSQL(schTableCreatorStatement)
            db.execSQL(finTableCreatorStatement)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addSchOne(schedule: Schedule) : Boolean {
        if (schedule.name.isEmpty() || schedule.startDate > schedule.endDate) return false;
        val db = writableDatabase
        val cv : ContentValues = ContentValues()

        cv.put(COL_SCH_NAME, schedule.name)
        cv.put(COL_SCH_START, schedule.startDate)
        cv.put(COL_SCH_END, schedule.endDate)

        val insert = db.insert(COL_SCH, null, cv)
        return insert >= 0
    }

    fun getSchForMonth(calendar: Calendar) : List<Schedule> {
        val list : ArrayList<Schedule> = ArrayList()
        val db = readableDatabase

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
        val startTime = calendar.timeInMillis
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
        val endTime = calendar.timeInMillis

        val query =
            "SELECT * FROM $COL_SCH WHERE $COL_SCH_START < $endTime AND $COL_SCH_END > $startTime"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {
                val schId = cursor.getInt(0)
                val schName = cursor.getString(1)
                val schStart= cursor.getLong(2)
                val schEnd= cursor.getLong(3)

                list.add(Schedule(schId, schName, schStart, schEnd))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
    fun getSchForDay(calendar: Calendar) : List<Schedule> {
        val list : ArrayList<Schedule> = ArrayList()
        val db = readableDatabase

        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND))
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endTime = calendar.timeInMillis

        val query =
            "SELECT * FROM $COL_SCH WHERE $COL_SCH_START < $endTime AND $COL_SCH_END > $startTime"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {
                val schId = cursor.getInt(0)
                val schName = cursor.getString(1)
                val schStart= cursor.getLong(2)
                val schEnd= cursor.getLong(3)

                list.add(Schedule(schId, schName, schStart, schEnd))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}