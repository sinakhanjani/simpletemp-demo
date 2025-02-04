package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.AdapterAvailableShiftDetailBinding
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.ShiftAdapterCallback
import com.simpletempco.simpletemp.util.changeColor

class AvailableShiftDetailAdapter :
    RecyclerView.Adapter<AvailableShiftDetailAdapter.ShiftDetailViewHolder>() {

    private var list: List<Shift?> = emptyList()

    private var callback: ShiftAdapterCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Shift>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun addCallback(callback: ShiftAdapterCallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftDetailViewHolder {
        return ShiftDetailViewHolder(
            AdapterAvailableShiftDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShiftDetailViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ShiftDetailViewHolder(
        private val binding: AdapterAvailableShiftDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Shift?) {
            if (item == null) return

            item.clinic?.fullname?.let { name ->
                binding.tvTitle.text = name
            }

            val arrivalTime = convertDateToTime(item.arrivalTime ?: "")
            val endTime = convertDateToTime(item.endTime ?: "")
            binding.tvTime.text = String.format("%s - %s", arrivalTime, endTime)

            item.clinic?.profile?.accountInformation?.let { info ->
                binding.tvAddress.text = String.format(
                    "%s, %s • %s",
                    info.addressLine1 ?: "",
                    info.city ?: "",
                    item.clinic?.distance ?: ""
                )
            }

            when (item.tag?.lowercase()) {
                "green" -> binding.viewState.changeColor(R.color.colorDayGreen)
                "orange" -> binding.viewState.changeColor(R.color.colorDayOrange)
                "blue" -> binding.viewState.changeColor(R.color.colorDayBlue)
                "gray" -> binding.viewState.changeColor(R.color.colorDayGray)
                else -> {
                    binding.viewState.changeColor(android.R.color.transparent)
                }
            }

            binding.root.setOnClickListener {
                callback?.onItemClick(item)
            }
        }
    }

}