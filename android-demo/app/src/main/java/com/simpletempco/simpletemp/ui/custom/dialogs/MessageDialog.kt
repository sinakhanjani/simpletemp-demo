package com.simpletempco.simpletemp.ui.custom.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.DialogMessageBinding
import com.simpletempco.simpletemp.util.MessageDialogCallback

class MessageDialog private constructor(
    context: Context,
    private val builder: Builder
) : Dialog(context) {

    companion object {
        var callback: MessageDialogCallback? = null
    }

    private lateinit var binding: DialogMessageBinding

    class Builder {

        var title: String? = null
        var message: String? = null
        var messageSpannable: SpannableStringBuilder? = null
        var positiveButtonText: String? = null
        var negativeButtonText: String? = null
        var isForce: Boolean = false

        fun title(title: String?) = apply { this.title = title }
        fun message(message: String?) = apply { this.message = message }
        fun messageSpannable(
            messageSpannable: SpannableStringBuilder?
        ) = apply { this.messageSpannable = messageSpannable }

        fun force(force: Boolean) = apply { isForce = force }
        fun positiveButtonText(positiveButtonText: String?) =
            apply { this.positiveButtonText = positiveButtonText }

        fun negativeButtonText(negativeButtonText: String?) =
            apply { this.negativeButtonText = negativeButtonText }


        fun build(
            context: Context,
            callback: MessageDialogCallback? = null
        ): MessageDialog {
            Companion.callback = callback
            return MessageDialog(context, this)
        }
    }

    override fun onStart() {
        super.onStart()

        val params = window!!.attributes
        params.width = (context.resources.displayMetrics.widthPixels * 0.8).toInt()
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window!!.attributes = params
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        // add animations
        window?.apply {
            attributes.windowAnimations = R.style.LoadingDialogStyle
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initViews() {
        binding.tvTitle.text = builder.title
        binding.tvMessage.text = if (builder.messageSpannable != null) {
            builder.messageSpannable
        } else {
            builder.message
        }
        binding.btnPositive.text = builder.positiveButtonText
        binding.btnNegative.text = builder.negativeButtonText

        if (builder.title.isNullOrEmpty()) {
            binding.tvTitle.visibility = ViewGroup.GONE
        }

        if (builder.positiveButtonText.isNullOrEmpty()) {
            binding.btnPositive.visibility = ViewGroup.GONE
            binding.divider.visibility = ViewGroup.GONE
        }

        if (builder.negativeButtonText.isNullOrEmpty()) {
            binding.btnNegative.visibility = ViewGroup.GONE
            binding.spacerBtn.visibility = ViewGroup.GONE
        }

        binding.btnPositive.setOnClickListener {
            if (!builder.isForce) dismiss()
            callback?.onDone()
        }
        binding.btnNegative.setOnClickListener {
            dismiss()
            callback?.onCancel()
        }
    }

}