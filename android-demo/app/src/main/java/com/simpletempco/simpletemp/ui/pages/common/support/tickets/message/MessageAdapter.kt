package com.simpletempco.simpletemp.ui.pages.common.support.tickets.message

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Message
import com.simpletempco.simpletemp.databinding.AdapterMessageAdminBinding
import com.simpletempco.simpletemp.databinding.AdapterMessageUserBinding
import com.simpletempco.simpletemp.util.DateUtil.convertUtcToLocal

const val VIEW_TYPE_USER = 0
const val VIEW_TYPE_ADMIN = 1

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Message> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Message>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun addItem(message: Message) {
        list.add(message)
        notifyItemInserted(list.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].sender?.lowercase() == "user")
            VIEW_TYPE_USER
        else
            VIEW_TYPE_ADMIN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            MessageUserViewHolder(
                AdapterMessageUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            MessageAdminViewHolder(
                AdapterMessageAdminBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_USER) {
            (holder as MessageUserViewHolder).bind(list[position])
        } else {
            (holder as MessageAdminViewHolder).bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    inner class MessageUserViewHolder(
        private val binding: AdapterMessageUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message) {
            binding.tvMessage.text = item.message
            item.createdAt?.let { date -> binding.tvDate.text = convertUtcToLocal(date) }
        }
    }

    inner class MessageAdminViewHolder(
        private val binding: AdapterMessageAdminBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Message) {
            binding.tvMessage.text = item.message
            item.createdAt?.let { date -> binding.tvDate.text = convertUtcToLocal(date) }
        }
    }

}