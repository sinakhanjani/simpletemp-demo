package com.simpletempco.simpletemp.ui.pages.clinic.home

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.AdapterWeekDayBinding
import com.simpletempco.simpletemp.util.AppConfig.GIFT_ITEMS_COUNT
import com.simpletempco.simpletemp.util.ContextUtils.getResColor

class WeekDaysAdapter constructor(val callback: (date: String?) -> Unit) :
    RecyclerView.Adapter<WeekDaysAdapter.DayViewHolder>() {

    private var currentItem = 0

    private var list: List<WeekDay> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<WeekDay>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            AdapterWeekDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class DayViewHolder(
        private val binding: AdapterWeekDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeekDay, position: Int) {

            val ctx = itemView.context

            binding.root.isChecked = (position == currentItem)

            if (position == 0) {
                binding.root.text = itemView.context.getString(R.string.today)
            } else {
                if (position != currentItem) {
                    binding.root.text = SpannableStringBuilder()
                        .color(ctx.getResColor(R.color.colorTextTitle)) {
                            append(item.weekDay)
                        }
                        .color(ctx.getResColor(R.color.colorTextSecondary)) {
                            append("\n").append(item.monthDate)
                        }
                } else {
                    binding.root.text = String.format("%s\n%s", item.weekDay, item.monthDate)
                }

            }

            itemView.setOnClickListener {
                callback.invoke(item.date)

                val oldItem = currentItem
                currentItem = position
                notifyItemChanged(oldItem)
                notifyItemChanged(currentItem)
            }
        }

    }

    data class WeekDay(val date: String?, val weekDay: String?, val monthDate: String?)
}