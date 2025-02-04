package com.simpletempco.simpletemp.ui.pages.clinic.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.AdapterClinicPendingShiftBinding
import com.simpletempco.simpletemp.databinding.AdapterPaidInvoiceBinding
import com.simpletempco.simpletemp.util.DateUtil
import com.simpletempco.simpletemp.util.ShiftsAdapterCallback
import com.simpletempco.simpletemp.util.loadCircularImage

const val VIEW_TYPE_PENDING = 0
const val VIEW_TYPE_CONFIRMED = 1

class ShiftsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isPendingShift = true

    private var list: List<Shift> = mutableListOf()

    private var callback: ShiftsAdapterCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Shift>, isPendingShift: Boolean) {
        this.list = list
        this.isPendingShift = isPendingShift
        notifyDataSetChanged()
    }

    fun addCallback(shiftsAdapterCallback: ShiftsAdapterCallback) {
        callback = shiftsAdapterCallback
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPendingShift) VIEW_TYPE_PENDING else VIEW_TYPE_CONFIRMED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PENDING) {
            PendingShiftViewHolder(
                AdapterClinicPendingShiftBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ConfirmedShiftViewHolder(
                AdapterPaidInvoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_PENDING) {
            (holder as PendingShiftViewHolder).bind(list[position])
        } else {
            (holder as ConfirmedShiftViewHolder).bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    inner class PendingShiftViewHolder(
        private val binding: AdapterClinicPendingShiftBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Shift?) {
            if (item == null) return

            binding.tvUserType.text = item.userType?.replaceFirstChar { it.uppercase() }

            item.date?.let { date ->
                binding.tvDate.text =
                    DateUtil.changeStringDateFormat(
                        date,
                        pattern = DateUtil.DATE_PATTERN_LONG,
                        toFormat = DateUtil.DATE_PATTERN_WEEK_DAY_SMALL
                    )
            }

            binding.tvTime.text = String.format(
                "%s - %s",
                DateUtil.convertDateToTime(item.arrivalTime ?: ""),
                DateUtil.convertDateToTime(item.endTime ?: "")
            )

            binding.tvHourlyRate.text = String.format(
                "Hourly Rate Offer :    %d £/hr",
                item.preferredPrice ?: 0
            )

            binding.tvOffersCount.text = item.countOffers?.toString()

            itemView.setOnClickListener { callback?.onPendingItemClicked(item) }

        }
    }

    inner class ConfirmedShiftViewHolder(
        private val binding: AdapterPaidInvoiceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Shift?) {
            if (item == null) return

            item.dentalTemp?.let { info ->
                binding.tvName.text = info.fullname
                binding.tvUserType.text = info.userType?.replaceFirstChar { it.uppercase() }
                binding.ivProfile.loadCircularImage(info.photoURL)
            }

            item.date?.let { date ->
                binding.tvDate.text =
                    DateUtil.changeStringDateFormat(
                        date,
                        pattern = DateUtil.DATE_PATTERN_LONG,
                        toFormat = DateUtil.DATE_PATTERN_WEEK_DAY_SMALL
                    )
            }

            binding.tvTime.text = String.format(
                "%s - %s",
                DateUtil.convertDateToTime(item.arrivalTime ?: ""),
                DateUtil.convertDateToTime(item.endTime ?: "")
            )

            binding.tvHourlyRate.text = String.format(
                "Hourly Rate Offer :    %d £/hr",
                item.offers?.first()?.preferredPrice ?: 0
            )

            binding.tvTotalPrice.text = item.offers?.first()?.cost

            itemView.setOnClickListener { callback?.onConfirmedItemClicked(item) }

        }
    }

}