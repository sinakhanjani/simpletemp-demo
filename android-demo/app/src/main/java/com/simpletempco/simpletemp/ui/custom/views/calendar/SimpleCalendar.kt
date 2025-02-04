package com.simpletempco.simpletemp.ui.custom.views.calendar

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.LayoutSimpleCalendarBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN
import com.simpletempco.simpletemp.util.DateUtil.convertStrDateToDate
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import com.simpletempco.simpletemp.util.DateUtil.getDateFromCalendar
import com.simpletempco.simpletemp.util.SavedState
import com.simpletempco.simpletemp.util.SimpleCalendarCallback
import com.simpletempco.simpletemp.util.SimpleCalendarItemCallback
import java.util.*

class SimpleCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), SimpleCalendarItemCallback {

    private var selectMode = false
    private var selectedDate: String? = null

    private var tags: List<DateTag>? = null

    private val calendarAdapter = CalendarAdapter()
    private var cal = Calendar.getInstance()

    private var callback: SimpleCalendarCallback? = null

    private val _binding =
        LayoutSimpleCalendarBinding.inflate(LayoutInflater.from(context), this, true)
    private val binding get() = _binding

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleCalendar)
        try {
            showDetails(
                typedArray.getBoolean(R.styleable.SimpleCalendar_showDetails, false)
            )

            selectMode = typedArray.getBoolean(R.styleable.SimpleCalendar_select_mode, false)

        } finally {
            typedArray.recycle()
        }

        initViews()
        refreshCalendar()
    }

    private fun initViews() {

        calendarAdapter.addCallback(this)

        binding.rvDays.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = calendarAdapter
        }

        binding.btnNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            refreshCalendar()
            callback?.onChangeMonth(getDateFromCalendar(cal))
        }

        binding.btnPre.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            refreshCalendar()
            callback?.onChangeMonth(getDateFromCalendar(cal))
        }
    }

    private fun refreshCalendar() {

        val list = mutableListOf<SimpleDay>()
        val today = currentDate(DATE_PATTERN)

        cal.set(Calendar.DAY_OF_MONTH, 1)

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
        val weekDayStartMonth = cal.get(Calendar.DAY_OF_WEEK)

        for (day in 1..cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            val date = String.format("%s-%02d-%02d", year, month, day)

            if (selectMode) {
                list.add(
                    SimpleDay(
                        day,
                        tags?.find { it.date?.contains(date) == true }?.tag,
                        selectedDate == date,
                        today == date
                    )
                )
            } else {
                list.add(SimpleDay(day, ""))
            }

        }

        binding.tvTitle.text = String.format("%s %s", monthName, year)
        calendarAdapter.setItems(weekDayStartMonth - 1, list)
    }

    private fun showDetails(show: Boolean) {
        binding.cvDetailsContainer.visibility = if (show) VISIBLE else GONE
    }

    fun setDate(date: String) {
        selectedDate = date
        convertStrDateToDate(date)?.let { cDate ->
            cal.time = cDate
            refreshCalendar()
        }
    }

    fun setTags(tags: List<DateTag>?) {
        this.tags = tags
        refreshCalendar()
    }

    fun addSelectDateCallBack(callback: SimpleCalendarCallback) {
        this.callback = callback
    }

    override fun onDaySelect(day: Int) {
        cal.set(Calendar.DAY_OF_MONTH, day)
        selectedDate = getDateFromCalendar(cal)
        selectedDate?.let { date ->
            callback?.onDateSelect(date)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.date = selectedDate
        ss.tagList = tags
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        ss.date?.let { setDate(it) }
        setTags(ss.tagList)
    }

}