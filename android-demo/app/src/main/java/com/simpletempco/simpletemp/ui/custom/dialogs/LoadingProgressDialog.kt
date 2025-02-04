package com.simpletempco.simpletemp.ui.custom.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.DialogProgressBinding


class LoadingProgressDialog(context: Context) : Dialog(context) {

    private lateinit var binding: DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_dialog_enter)
        anim.duration = 500
        binding.root.startAnimation(anim)

        // add animations
        window?.apply {
            attributes.windowAnimations = R.style.LoadingDialogStyle
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

}