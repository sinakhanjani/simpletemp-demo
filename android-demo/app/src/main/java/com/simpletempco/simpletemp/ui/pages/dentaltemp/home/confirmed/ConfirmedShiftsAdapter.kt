package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.confirmed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.AdapterConfirmedShiftBinding
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_DISPLAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.ShiftAdapterCallback

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class ConfirmedShiftsAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Shift?> = mutableListOf()

    private var callback: ShiftAdapterCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Shift?>) {
        this.list.addAll(list)
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

    fun addCallback(callback: ShiftAdapterCallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            ShiftDetailViewHolder(
                AdapterConfirmedShiftBinding.inflate(
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
            (holder as ShiftDetailViewHolder).bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    inner class ShiftDetailViewHolder(
        private val binding: AdapterConfirmedShiftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Shift?) {
            if (item == null) return

            item.clinic?.fullname?.let { name ->
                binding.tvTitle.text = name
            }

            val arrivalTime = convertDateToTime(item.arrivalTime ?: "")
            val endTime = convertDateToTime(item.endTime ?: "")
            binding.tvTime.text = String.format("%s - %s", arrivalTime, endTime)

            item.date?.let { date ->
                binding.tvDate.text = changeStringDateFormat(
                    date,
                    pattern = DATE_PATTERN_LONG,
                    toFormat = DATE_PATTERN_DISPLAY_LONG
                )
            }

            item.clinic?.profile?.accountInformation?.let { info ->
                binding.tvAddress.text = String.format(
                    "%s, %s • %s",
                    info.addressLine1 ?: "",
                    info.city ?: "",
                    item.clinic?.distance ?: ""
                )
            }

            binding.root.setOnClickListener {
                callback?.onItemClick(item)
            }
        }
    }

    inner class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}