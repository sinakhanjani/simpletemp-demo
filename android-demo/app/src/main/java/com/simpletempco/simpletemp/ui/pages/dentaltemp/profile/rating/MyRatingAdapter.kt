package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.rating

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simpletempco.simpletemp.data.remote.models.response.Rate
import com.simpletempco.simpletemp.databinding.AdapterLoadingBinding
import com.simpletempco.simpletemp.databinding.AdapterReviewBinding
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.convertUtcToLocal

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class MyRatingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Rate?> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Rate>) {
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
        return if (list[position] == null)
            VIEW_TYPE_LOADING
        else
            VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                ReviewViewHolder(
                    AdapterReviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                LoadingViewHolder(
                    AdapterLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            (holder as ReviewViewHolder).bind(list[position] as Rate)
        }
    }

    override fun getItemCount() = list.size

    inner class ReviewViewHolder(
        private val binding: AdapterReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Rate) {
            binding.tvTitle.text = item.clinic?.fullname ?: ""
            binding.tvReview.text = item.description ?: ""
            binding.rbRating.rating(item.rate ?: 0)
            item.createdAt?.let { date ->
                binding.tvDate.text = convertUtcToLocal(date, DATE_PATTERN_WEEK_DAY_LONG)
            }
        }
    }

    class LoadingViewHolder(
        binding: AdapterLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root)

}