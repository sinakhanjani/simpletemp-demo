package com.simpletempco.simpletemp.ui.custom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.simpletempco.simpletemp.databinding.LayoutNonDataBinding
import com.simpletempco.simpletemp.ui.custom.views.NonDataLayout.Status.*

class NonDataLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val _binding =
        LayoutNonDataBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    private val binding get() = _binding

    init {

        initViews()
    }

    private fun initViews() {

    }

    fun status(status: Status, message: String? = null) {
        when (status) {
            LOADING -> {
                visibility = VISIBLE
                binding.loading.visibility = VISIBLE
                binding.emptyView.root.visibility = GONE
                binding.errorView.root.visibility = GONE
            }
            ERROR -> {
                visibility = VISIBLE
                binding.errorView.root.visibility = VISIBLE
                binding.emptyView.root.visibility = GONE
                binding.loading.visibility = GONE
            }
            EMPTY -> {
                visibility = VISIBLE
                binding.emptyView.root.visibility = VISIBLE
                binding.errorView.root.visibility = GONE
                binding.loading.visibility = GONE

                message?.let {
                    binding.emptyView.root.text = it
                }
            }
            DATA -> {
                visibility = GONE
            }
        }
    }

    fun onRetry(retry: () -> Unit) {
        binding.errorView.btnRetry.setOnClickListener { retry() }
    }

    enum class Status {
        DATA,
        LOADING,
        ERROR,
        EMPTY
    }

}