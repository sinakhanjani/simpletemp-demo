package com.simpletempco.simpletemp.ui.pages.common.support.tickets.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Ticket
import com.simpletempco.simpletemp.databinding.AdapterTicketBinding
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.DateUtil.convertUtcToLocal

class TicketsAdapter(val callback: (ticket: Ticket) -> Unit) :
    RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    private var list: List<Ticket> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Ticket>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        return TicketViewHolder(
            AdapterTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class TicketViewHolder(
        private val binding: AdapterTicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var context: Context = binding.root.context

        fun bind(item: Ticket) {

            binding.tvTitle.text = item.subject
            binding.tvStatus.text = item.state
            item.createdAt?.let { date -> binding.tvDate.text = convertUtcToLocal(date) }

            binding.tvStatus.setTextColor(
                when (item.state?.lowercase()) {
                    "open" -> context.getResColor(R.color.colorGreen)
                    "closed" -> context.getResColor(R.color.colorRed)
                    "pending" -> context.getResColor(R.color.colorOrange)
                    else -> context.getResColor(R.color.colorTextSecondary)
                }
            )

            binding.root.setOnClickListener {
                callback.invoke(item)
            }
        }
    }

}