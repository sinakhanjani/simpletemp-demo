package com.simpletempco.simpletemp.ui.pages.common.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.simpletempco.simpletemp.BuildConfig
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.ActivitySplashBinding
import com.simpletempco.simpletemp.ui.base.BaseActivity
import com.simpletempco.simpletemp.ui.pages.clinic.ClinicActivity
import com.simpletempco.simpletemp.ui.pages.common.authentication.AuthActivity
import com.simpletempco.simpletemp.ui.pages.dentaltemp.DentalTempActivity
import com.simpletempco.simpletemp.util.AppConfig.SPLASH_DELAY
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val viewModel: SplashViewModel by viewModels()

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVersion()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextPage()
        }, SPLASH_DELAY)

    }

    private fun initVersion() {
        binding.tvVersion.text = String.format("Version %s", BuildConfig.VERSION_NAME)
    }

    private fun navigateToNextPage() {
        val intent = if (viewModel.isLogin()) {
            if (viewModel.userType() == "clinic") {
                Intent(this, ClinicActivity::class.java)
            } else {
                Intent(this, DentalTempActivity::class.java)
            }
        } else {
            Intent(this, AuthActivity::class.java)
        }

        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
        finish()
    }
}