package com.simpletempco.simpletemp.ui.pages.dentaltemp.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.AdapterPendingShiftBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime

class ShiftAdapter constructor(val callback: (shift: Shift) -> Unit) :
    RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>() {

    private var list: List<Shift> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Shift>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftViewHolder {
        return ShiftViewHolder(
            AdapterPendingShiftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShiftViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ShiftViewHolder(
        private val binding: AdapterPendingShiftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Shift) {

            binding.tvName.text = item.clinic?.fullname


            item.date?.let { date ->
                binding.tvDate.text =
                    changeStringDateFormat(
                        date,
                        pattern = DATE_PATTERN_LONG,
                        toFormat = DATE_PATTERN_WEEK_DAY_LONG
                    )
            }

            binding.tvTime.text = String.format(
                "%s - %s",
                convertDateToTime(item.arrivalTime ?: ""),
                convertDateToTime(item.endTime ?: "")
            )

            itemView.setOnClickListener {
                callback.invoke(item)
            }

        }
    }

}