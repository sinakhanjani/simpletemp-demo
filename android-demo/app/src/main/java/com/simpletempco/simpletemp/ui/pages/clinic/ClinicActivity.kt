package com.simpletempco.simpletemp.ui.pages.clinic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import androidx.navigation.ui.setupActionBarWithNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Version
import com.simpletempco.simpletemp.databinding.ActivityClinicBinding
import com.simpletempco.simpletemp.ui.base.BaseActivity
import com.simpletempco.simpletemp.ui.pages.common.authentication.AuthActivity
import com.simpletempco.simpletemp.ui.pages.common.support.faq.FaqActivity
import com.simpletempco.simpletemp.ui.pages.common.support.tickets.SupportActivity
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.hideView
import com.simpletempco.simpletemp.util.showView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClinicActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController

    private val viewModel: ClinicViewModel by viewModels()

    private lateinit var binding: ActivityClinicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObserve()
    }

    private fun initViews() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        // init nav host and nav controller
        navHost =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        setupBottomNavMenu(navController)
        setupActionBar()
        setupDestination()
    }

    private fun initObserve() {

        viewModel.logoutResult.observe(this) {
            hideLoading()
            navigateToAuth()
        }

        viewModel.updateAvailable.observe(this) { info ->
            showUpdateDialog(info)
        }

    }

    private fun setupActionBar() {
        // Setting Up ActionBar with Navigation Controller
        // Pass the IDs of top-level destinations in AppBarConfiguration
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.home_dest,
                R.id.notif_dest,
                R.id.post_shift_dest,
                R.id.money_dest,
                R.id.profile_dest
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_faq, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_faq -> {
                navigateToFaq()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDestination() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            /*if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                showBottomNavigation()
            } else {
                hideBottomNavigation()
            }*/
        }
    }

    @OptIn(NavigationUiSaveStateControl::class)
    private fun setupBottomNavMenu(navController: NavController) {
//        binding.bottomNavigation.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController, false)
    }

    override fun hideBottomNavigation() {
        if (!this::binding.isInitialized) return
        hideView(binding.bottomNavigation)
    }

    override fun showBottomNavigation() {
        if (!this::binding.isInitialized) return
        showView(binding.bottomNavigation)
    }

    override fun setNotificationBadge(badge: Int) {
        if (badge > 0) {
            binding.bottomNavigation.getOrCreateBadge(R.id.notif_dest).number = badge
        } else {
            binding.bottomNavigation.removeBadge(R.id.notif_dest)
        }
    }

    override fun navItemSelect(navItemId: Int) {
        binding.bottomNavigation.selectedItemId = navItemId
    }

    private fun showUpdateDialog(info: Version?) {
        showMessageDialog(
            title = getString(R.string.update),
            message = info?.description ?: "",
            positiveButtonText = getString(R.string.update_now),
            cancelable = false,
            onPositiveButtonClick = {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(info?.android?.appURL ?: "")))
            }
        )
    }

    fun navigateToFaq() {
        val intent = Intent(this, FaqActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
    }

    fun navigateToSupport() {
        val intent = Intent(this, SupportActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
        finish()
    }

    override fun logout() {
        showLoading()
        viewModel.logout()
    }

}