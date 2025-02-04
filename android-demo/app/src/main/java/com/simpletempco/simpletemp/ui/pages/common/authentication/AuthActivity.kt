package com.simpletempco.simpletemp.ui.pages.common.authentication

import android.content.Intent
import android.os.Bundle
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.ActivityAuthBinding
import com.simpletempco.simpletemp.ui.base.BaseActivity
import com.simpletempco.simpletemp.ui.pages.clinic.ClinicActivity
import com.simpletempco.simpletemp.ui.pages.dentaltemp.DentalTempActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun navigateToDentalTemp() {
        val intent = Intent(this, DentalTempActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
        finish()
    }

    fun navigateToClinic() {
        val intent = Intent(this, ClinicActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
        finish()
    }

}