package com.simpletempco.simpletemp.ui.pages.dentaltemp.money

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.databinding.AdapterFinishedJobBinding
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class MoneyAdapter constructor(val callback: (item: Offer) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Offer?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Offer?>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAllItems() {
        list.clear()
        notifyDataSetChanged()
    }

    fun showLoading() {
        list.add(null)
        notifyItemInserted(list.size - 1)
    }

    fun hideLoading() {
        if (list.isNotEmpty() && list.last() == null) {
            list.removeLast()
            notifyItemRemoved(list.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            JobViewHolder(
                AdapterFinishedJobBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            LoadingViewHolder(
                AdapterLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            (holder as JobViewHolder).bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    inner class JobViewHolder(
        private val binding: AdapterFinishedJobBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Offer?) {
            if (item == null) return

            binding.tvTitle.text = item.shift?.clinic?.fullname ?: ""

            val arrivalTime = convertDateToTime(item.shift?.arrivalTime ?: "")
            val endTime = convertDateToTime(item.shift?.endTime ?: "")
            binding.tvTime.text = String.format("%s - %s", arrivalTime, endTime)

            item.shift?.date?.let { date ->
                binding.tvDate.text = changeStringDateFormat(
                    date,
                    pattern = DATE_PATTERN_LONG,
                    toFormat = DATE_PATTERN_WEEK_DAY_LONG
                )
            }

            item.shift?.clinic?.profile?.accountInformation?.let { info ->
                binding.tvAddress.text = String.format(
                    "%s, %s • %s",
                    info.addressLine1 ?: "",
                    info.city ?: "",
                    item.shift?.clinic?.distance ?: ""
                )
            }

            itemView.setOnClickListener { callback.invoke(item) }
        }
    }

    inner class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}