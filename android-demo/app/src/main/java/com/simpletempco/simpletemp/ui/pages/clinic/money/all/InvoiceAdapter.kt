package com.simpletempco.simpletemp.ui.pages.clinic.money.all

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding
import com.simpletempco.simpletemp.databinding.AdapterPaidInvoiceBinding
import com.simpletempco.simpletemp.databinding.AdapterUnpaidInvoiceBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_SMALL
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.InvoiceAdapterCallback
import com.simpletempco.simpletemp.util.loadCircularImage

const val VIEW_TYPE_LOADING = 0
const val VIEW_TYPE_ITEM_PAID = 1
const val VIEW_TYPE_ITEM_UN_PAID = 2

class InvoiceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Invoice?> = mutableListOf()

    private var callback: InvoiceAdapterCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Invoice>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return when {
            item == null -> VIEW_TYPE_LOADING
            item.shift?.status == "paid" -> VIEW_TYPE_ITEM_PAID
            else -> VIEW_TYPE_ITEM_UN_PAID
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
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

    fun addCallback(invoiceAdapterCallback: InvoiceAdapterCallback) {
        callback = invoiceAdapterCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_LOADING -> LoadingViewHolder(
                AdapterLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_ITEM_PAID -> PaidViewHolder(
                AdapterPaidInvoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> UnPaidViewHolder(
                AdapterUnpaidInvoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM_PAID -> {
                (holder as PaidViewHolder).bind(list[position])
            }
            VIEW_TYPE_ITEM_UN_PAID -> {
                (holder as UnPaidViewHolder).bind(list[position], position)
            }
        }
    }

    override fun getItemCount() = list.size

    inner class PaidViewHolder(
        private val binding: AdapterPaidInvoiceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Invoice?) {
            if (item == null) return

            item.dentalTemp?.let { info ->
                binding.tvName.text = info.fullname
                binding.tvUserType.text = info.userType?.replaceFirstChar { it.uppercase() }
                binding.ivProfile.loadCircularImage(info.photoURL)
            }

            item.shiftDate?.let { date ->
                binding.tvDate.text =
                    changeStringDateFormat(
                        date,
                        pattern = DATE_PATTERN_LONG,
                        toFormat = DATE_PATTERN_WEEK_DAY_SMALL
                    )
            }

            binding.tvTime.text = String.format(
                "%s - %s",
                convertDateToTime(item.arrivalTime ?: ""),
                convertDateToTime(item.endTime ?: "")
            )

            binding.tvHourlyRate.text = String.format(
                "Hourly Rate Offer :    %d £/hr",
                item.preferredPrice ?: 0
            )

            binding.tvTotal.text = item.totalPrice

            itemView.setOnClickListener {
                callback?.onPaidItemClicked(item)
            }
        }
    }

    inner class UnPaidViewHolder(
        private val binding: AdapterUnpaidInvoiceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Invoice?, position: Int) {
            if (item == null) return

            item.dentalTemp?.let { info ->
                binding.tvName.text = info.fullname
                binding.tvUserType.text = info.userType?.replaceFirstChar { it.uppercase() }
                binding.ivProfile.loadCircularImage(info.photoURL)
            }

            item.shiftDate?.let { date ->
                binding.tvDate.text =
                    changeStringDateFormat(
                        date,
                        pattern = DATE_PATTERN_LONG,
                        toFormat = DATE_PATTERN_WEEK_DAY_SMALL
                    )
            }

            binding.tvTime.text = String.format(
                "%s - %s",
                convertDateToTime(item.arrivalTime ?: ""),
                convertDateToTime(item.endTime ?: "")
            )

            binding.tvHourlyRate.text = String.format(
                "Hourly Rate Offer :    %d £/hr",
                item.preferredPrice ?: 0
            )

            binding.tvTotalPrice.text = item.totalPrice

            if (item.isExpand) {
                binding.ivExpand.rotation = -90f
                binding.expand.visibility = VISIBLE
            } else {
                binding.ivExpand.rotation = 90f
                binding.expand.visibility = GONE
            }

            binding.root.setOnClickListener {
                item.isExpand = !item.isExpand
                notifyItemChanged(position)
            }

            binding.btnPayNow.setOnClickListener {
                callback?.onPayNowClicked(item)
            }

            binding.btnPayManually.setOnClickListener {
                callback?.onPayManuallyClicked(item)
            }

        }
    }

    inner class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}