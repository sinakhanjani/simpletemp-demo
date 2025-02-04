package com.simpletempco.simpletemp.ui.custom.views.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.AdapterDayBinding
import com.simpletempco.simpletemp.ui.custom.views.DayBackgroundDrawable
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.SimpleCalendarItemCallback

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_EMPTY = 1

class CalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentItemSelected = -1

    private var callback: SimpleCalendarItemCallback? = null

    private var list: MutableList<SimpleDay?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(weekDayStartMonth: Int, list: List<SimpleDay>) {
        this.list = list.toMutableList()
        repeat(weekDayStartMonth) { this.list.add(0, null) }
        currentItemSelected = -1
        notifyDataSetChanged()
    }

    fun addCallback(callback: SimpleCalendarItemCallback) {
        this.callback = callback
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            DayViewHolder(
                AdapterDayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            EmptyViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            (holder as DayViewHolder).bind(list[position], position)
        }
    }

    override fun getItemCount() = list.size

    inner class DayViewHolder(
        private val binding: AdapterDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SimpleDay?, position: Int) {
            if (item == null) return

            val ctx = itemView.context
            val bg = DayBackgroundDrawable(ctx)

            binding.tvDay.text = item.day.toString()
            binding.tvDay.setTextColor(ctx.getResColor(R.color.white))
            binding.tvDay.background = bg

            when (item.tag?.lowercase()) {
                "gray" -> bg.setColor(ctx.getResColor(R.color.colorDayGray))
                "blue" -> bg.setColor(ctx.getResColor(R.color.colorDayBlue))
                "green" -> bg.setColor(ctx.getResColor(R.color.colorDayGreen))
                "orange" -> bg.setColor(ctx.getResColor(R.color.colorDayOrange))
                "red" -> bg.setColor(ctx.getResColor(R.color.colorDayRed))
                else -> {
                    if (item.isToday) {
                        bg.setColor(ctx.getResColor(R.color.colorAccentToday))
                    } else {
                        bg.setColor(null)
                    }
                    binding.tvDay.setTextColor(ctx.getResColor(R.color.colorTextGray))
                }
            }

            bg.isSelected(item.isSelect)
            if (item.isSelect) {
                currentItemSelected = position
                bg.isSelected(true)

                if (!item.isToday)
                    binding.tvDay.setTextColor(ctx.getResColor(R.color.white))
            } else {
                bg.isSelected(false)
            }

            binding.tvDay.setOnClickListener {
                callback?.onDaySelect(item.day)
                if (currentItemSelected != -1) {
                    list[currentItemSelected]?.isSelect = false
                    notifyItemChanged(currentItemSelected)
                }
                list[position]?.isSelect = true
                notifyItemChanged(position)
            }
        }
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}