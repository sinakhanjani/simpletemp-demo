package com.simpletempco.simpletemp.ui.pages.clinic.money

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.databinding.AdapterUnpaidInvoiceCardBinding
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_DISPLAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.InvoiceAdapterCallback
import com.simpletempco.simpletemp.util.loadCircularImage

class InvoicePagerAdapter : RecyclerView.Adapter<InvoicePagerAdapter.MoneyViewHolder>() {

    private var currentItem = 0

    private var list: List<Invoice> = emptyList()

    private var callback: InvoiceAdapterCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Invoice>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun addCallback(invoiceAdapterCallback: InvoiceAdapterCallback) {
        callback = invoiceAdapterCallback
    }

    fun setCurrentItem(itemPosition: Int) {
        val toNext = itemPosition >= currentItem
        currentItem = itemPosition
        if (toNext) {
            notifyItemRangeChanged(itemPosition - 1, itemPosition + 1)
        } else {
            notifyItemRangeChanged(itemPosition, itemPosition + 2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyViewHolder {
        return MoneyViewHolder(
            AdapterUnpaidInvoiceCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoneyViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class MoneyViewHolder(
        private val binding: AdapterUnpaidInvoiceCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Invoice, position: Int) {

            binding.root.setCardBackgroundColor(
                if (currentItem == position) {
                    itemView.context.getResColor(R.color.colorBlueLight)
                } else {
                    itemView.context.getResColor(R.color.colorLightBg)
                }
            )

            item.dentalTemp?.let { info ->
                binding.tvName.text = info.fullname
                binding.tvUserType.text = info.userType?.replaceFirstChar { it.uppercase() }
                binding.ivProfile.loadCircularImage(info.photoURL)
            }

            item.shiftDate?.let { date ->
                binding.tvDateOfShift.text =
                    changeStringDateFormat(
                        date,
                        pattern = DATE_PATTERN_LONG,
                        toFormat = DATE_PATTERN_DISPLAY_LONG
                    )
            }

            binding.tvTimeOfShift.text = String.format(
                "%s - %s",
                convertDateToTime(item.arrivalTime ?: ""),
                convertDateToTime(item.endTime ?: "")
            )

            binding.tvHourlyPayment.text = String.format("£%d /hr", item.preferredPrice ?: 0)

            binding.tvTotal.text = item.totalPrice

            binding.btnPayNow.setOnClickListener {
                callback?.onPayNowClicked(item)
            }

            binding.btnPayManually.setOnClickListener {
                callback?.onPayManuallyClicked(item)
            }

        }
    }

}