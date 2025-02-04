package com.simpletempco.simpletemp.ui.pages.common.support.tickets

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.ActivitySupportBinding
import com.simpletempco.simpletemp.ui.base.BaseActivity
import com.simpletempco.simpletemp.ui.pages.common.support.faq.FaqActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportActivity : BaseActivity() {

    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var binding: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        setSupportActionBar(binding.toolbar)

        // init nav host and nav controller
        navHost =
            supportFragmentManager.findFragmentById(R.id.support_nav_host_fragment) as NavHostFragment
        navController = navHost.navController


        setupActionBar()
        setupDestination()
    }

    private fun setupActionBar() {
        setupActionBarWithNavController(
            navController,
            AppBarConfiguration(topLevelDestinationIds = emptySet())
        )
    }

    private fun setupDestination() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
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
                val intent = Intent(this, FaqActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}