package com.simpletempco.simpletemp.ui.pages.clinic.notif

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Notif
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding
import com.simpletempco.simpletemp.databinding.AdapterNotificationBinding
import com.simpletempco.simpletemp.util.ContextUtils.getResColor

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class ClinicNotificationAdapter constructor(val callback: (item: Notif) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Notif?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Notif?>) {
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
            NotificationViewHolder(
                AdapterNotificationBinding.inflate(
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
            (holder as NotificationViewHolder).bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    inner class NotificationViewHolder(
        private val binding: AdapterNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notif?) {
            if (item == null) return

            binding.tvTitle.text = item.notification?.title ?: ""
            binding.tvMessage.text = item.notification?.body ?: ""

            binding.ivInfo.visibility = if (item.data?.meta == null) INVISIBLE else VISIBLE

            binding.root.setCardBackgroundColor(
                itemView.context.getResColor(
                    if (item.isRead == true)
                        R.color.colorLightBg
                    else
                        R.color.colorPrimaryLight
                )
            )

            itemView.setOnClickListener { callback.invoke(item) }
        }
    }

    inner class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}