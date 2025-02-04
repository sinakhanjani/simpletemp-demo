package com.simpletempco.simpletemp.ui.custom.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.DialogRatingBinding
import com.simpletempco.simpletemp.util.DateUtil

class RatingDialog private constructor(
    context: Context,
    private val builder: Builder
) : Dialog(context) {

    companion object {
        lateinit var callback: (dialog: RatingDialog, rate: Int, description: String?) -> Unit
    }

    private lateinit var binding: DialogRatingBinding

    class Builder {

        var shift: Shift? = null

        fun shiftData(shift: Shift?) = apply { this.shift = shift }

        fun build(
            context: Context,
            callback: (dialog: RatingDialog, rate: Int, description: String?) -> Unit
        ): RatingDialog {
            Companion.callback = callback
            return RatingDialog(context, this)
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
        binding = DialogRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        // add animations
        window?.apply {
            attributes.windowAnimations = R.style.LoadingDialogStyle
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initViews() {

        builder.shift?.apply {

            clinic?.fullname?.let { name -> binding.tvName.text = name }

            binding.tvTime.text = String.format(
                "%s - %s",
                DateUtil.convertDateToTime(arrivalTime ?: ""),
                DateUtil.convertDateToTime(endTime ?: "")
            )

            binding.tvTotalDayPay.text = String.format(
                "%s %s",
                context.getString(R.string.total_days_pay_),
                cost ?: ""
            )

            binding.tvAddress.text = String.format(
                "%s, %s • %s",
                clinic?.profile?.accountInformation?.addressLine1 ?: "",
                clinic?.profile?.accountInformation?.city ?: "",
                clinic?.distance ?: ""
            )

            binding.tvMessage.text = String.format(
                "%s %s?\n(%s)",
                context.getString(R.string.how_was_your_experience_at),
                clinic?.fullname,
                context.getString(R.string.anonymous)
            )

        }

        binding.btnSubmit.setOnClickListener {
            callback.invoke(
                this,
                binding.rbRating.getRating(),
                binding.edtReview.text?.toString()
            )
        }

    }

}