package com.simpletempco.simpletemp.util

import com.simpletempco.simpletemp.ui.pages.clinic.home.WeekDaysAdapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

object DateUtil {

    const val TIME_PATTERN_NORMAL = "HH:mm"
    const val TIME_PATTERN_12 = "hh:mm a"
    const val DATE_PATTERN = "yyyy-MM-dd"
    const val DATE_PATTERN_LONG = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_PATTERN_DISPLAY = "MMM d'th', yyyy"
    const val DATE_PATTERN_DISPLAY_LONG = "MMMM d'th', yyyy"
    const val DATE_PATTERN_WEEK_DAY = "EEE MMMM d'th', yyyy"
    const val DATE_PATTERN_WEEK_DAY_SMALL = "EEEE MMM d'th', yyyy"
    const val DATE_PATTERN_WEEK_DAY_LONG = "EEEE MMMM d'th', yyyy"

    fun convertUtcToLocal(
        date: String,
        toFormat: String = "dd, MM, yyyy"
    ): String? {
        return try {
            val df = SimpleDateFormat(toFormat, Locale.getDefault())
            df.timeZone = TimeZone.getDefault()

            val utcDf = SimpleDateFormat(DATE_PATTERN_LONG, Locale.getDefault())
            utcDf.timeZone = TimeZone.getTimeZone("UTC")

            val utcDate = utcDf.parse(date)
            df.format(utcDate!!)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun currentDate(
        toFormat: String = "dd, MM, yyyy"
    ): String? {
        return try {
            val df = SimpleDateFormat(toFormat, Locale.getDefault())

            val currentDate = Calendar.getInstance().time
            df.format(currentDate)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun currentTime(): String {
        return SimpleDateFormat("hh : mm,a", Locale.getDefault())
            .format(Calendar.getInstance().time).lowercase()
    }

    fun changeStringDateFormat(
        date: String,
        pattern: String = DATE_PATTERN,
        toFormat: String = DATE_PATTERN_DISPLAY
    ): String? {
        val inDf = SimpleDateFormat(pattern, Locale.getDefault())
        val outDf = SimpleDateFormat(toFormat, Locale.getDefault())
        return try {
            outDf.format(inDf.parse(date)!!)
        } catch (e: Exception) {
            null
        }
    }

    // date format: yyyy-MM-dd
    fun convertDateToRangeDate(
        date: String
    ): Pair<String?, String?> {

        val cal = Calendar.getInstance()
        cal.time = convertStrDateToDate(date)!!

        cal.set(Calendar.DAY_OF_MONTH, 1)
        val from = getDateFromCalendar(cal)

//        val lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
//        cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth)

        cal.add(Calendar.MONTH, 1)
        val to = getDateFromCalendar(cal)

        return Pair(from, to)
    }

    fun changeMonthOfDate(
        date: String,
        value: Int
    ): String? {
        val cal = Calendar.getInstance()
        cal.time = convertStrDateToDate(date)!!
        cal.add(Calendar.MONTH, value)

        return getDateFromCalendar(cal)
    }

    fun getDateFromCalendar(
        cal: Calendar,
        toFormat: String = DATE_PATTERN
    ): String? {
        return try {
            val df = SimpleDateFormat(toFormat, Locale.getDefault())
            df.format(cal.time)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertStrDateToDate(
        date: String,
        pattern: String = DATE_PATTERN
    ): Date? {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertDateToStrDate(
        date: Date,
        toFormat: String = DATE_PATTERN
    ): String {
        val df = SimpleDateFormat(toFormat, Locale.getDefault())
        return df.format(date)
    }

    fun convertDateToTime(
        date: String,
        pattern: String = DATE_PATTERN_LONG,
        toFormat: String = TIME_PATTERN_12
    ): String? {
        val mDate = convertStrDateToDate(date, pattern) ?: return null
        return SimpleDateFormat(toFormat, Locale.getDefault()).format(mDate).lowercase()
    }

    fun getTwoDateDuration(
        firstDate: String?,
        secondDate: String?
    ): Int? {
        return try {
            val first = convertStrDateToDate(firstDate!!, TIME_PATTERN_NORMAL)
            val second = convertStrDateToDate(secondDate!!, TIME_PATTERN_NORMAL)

            val duration = second!!.time - first!!.time
            TimeUnit.MILLISECONDS.toMinutes(duration).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun getTwoDateDurationByHour(
        firstDate: String?,
        secondDate: String?,
        reduceMinutes: Int = 0,
    ): String? {
        return try {
            val duration = max(0, (getTwoDateDuration(firstDate, secondDate) ?: 0) - reduceMinutes)
            val hour = TimeUnit.MINUTES.toHours(duration.toLong()).toInt()
            val min = duration % 60
            String.format("%s:%02d", hour.toString(), min)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun currentTime24Hour(): String {
        return SimpleDateFormat(TIME_PATTERN_NORMAL, Locale.getDefault())
            .format(Calendar.getInstance().time)
    }

    fun convert24HourTo12(hour: Int): Int {
        return if (hour == 0) {
            12
        } else if (hour > 12) {
            hour % 12
        } else {
            hour
        }
    }

    fun getWeekDaysDate(): List<WeekDaysAdapter.WeekDay> {
        val list = mutableListOf<WeekDaysAdapter.WeekDay>()

        val df = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        val wdf = SimpleDateFormat("EEE", Locale.getDefault())
        val mdf = SimpleDateFormat("d MMM", Locale.getDefault())
        val cal = Calendar.getInstance()

        repeat(7) {
            list.add(
                WeekDaysAdapter.WeekDay(
                    date = df.format(cal.time),
                    weekDay = wdf.format(cal.time),
                    monthDate = mdf.format(cal.time)
                )
            )
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        return list
    }

}