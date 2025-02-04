package com.simpletempco.simpletemp.ui.pages.common.support.faq

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.FaqItem
import com.simpletempco.simpletemp.databinding.AdapterFaqBinding
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class FaqAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var expandItem = -1

    private var list: MutableList<FaqItem?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<FaqItem>) {
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
            FaqViewHolder(
                AdapterFaqBinding.inflate(
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
            (holder as FaqViewHolder).bind(list[position], position)
        }
    }

    override fun getItemCount() = list.size

    inner class FaqViewHolder(
        private val binding: AdapterFaqBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private fun onItemClicker(itemPosition: Int) {
            if (itemPosition == expandItem) {
                expandItem = -1
            } else {
                val oldItem = expandItem
                expandItem = itemPosition
                notifyItemChanged(oldItem)
            }
            notifyItemChanged(itemPosition)
        }

        fun bind(item: FaqItem?, itemPosition: Int) {
            if (item == null) return

            binding.tvTitle.text = item.question
            binding.tvDetails.text = item.answer

            if (expandItem == itemPosition) {
                binding.tvDetails.visibility = VISIBLE
                binding.btnExpand.animate().rotation(90F)
            } else {
                binding.tvDetails.visibility = GONE
                binding.btnExpand.animate().rotation(0F)
            }

            binding.btnExpand.setOnClickListener { onItemClicker(itemPosition) }
            binding.containerTitle.setOnClickListener { onItemClicker(itemPosition) }
        }
    }

    inner class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}