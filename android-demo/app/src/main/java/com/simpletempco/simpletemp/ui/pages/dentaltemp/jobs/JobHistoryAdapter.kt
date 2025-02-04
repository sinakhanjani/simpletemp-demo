package com.simpletempco.simpletemp.ui.pages.dentaltemp.jobs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.databinding.AdapterJobHistoryBinding
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class JobHistoryAdapter constructor(val callback: (item: Offer) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Offer?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Offer?>) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            JobViewHolder(
                AdapterJobHistoryBinding.inflate(
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
        private val binding: AdapterJobHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Offer?) {
            if (item == null) return

            binding.tvTitle.text = item.shift?.clinic?.fullname ?: ""
            item.shift?.date?.let { date ->
                binding.tvDate.text = changeStringDateFormat(
                    date,
                    pattern = DATE_PATTERN_LONG,
                    toFormat = DATE_PATTERN_WEEK_DAY_LONG
                )
            }

            itemView.setOnClickListener { callback.invoke(item) }
        }
    }

    inner class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}