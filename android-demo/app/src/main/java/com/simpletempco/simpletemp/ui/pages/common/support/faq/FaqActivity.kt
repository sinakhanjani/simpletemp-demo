package com.simpletempco.simpletemp.ui.pages.common.support.faq

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.FaqItem
import com.simpletempco.simpletemp.databinding.ActivityFaqBinding
import com.simpletempco.simpletemp.ui.custom.views.NonDataLayout
import com.simpletempco.simpletemp.ui.pages.common.authentication.AuthActivity
import com.simpletempco.simpletemp.ui.pages.common.support.tickets.SupportActivity
import com.simpletempco.simpletemp.util.AppConfig.FAQ_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.ContextUtils.getResDrawable
import com.simpletempco.simpletemp.util.ContextUtils.isInternetConnected
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.EndlessRecyclerOnScrollListener
import com.simpletempco.simpletemp.util.UiStateFaq
import dagger.hilt.android.AndroidEntryPoint
import okio.IOException

@AndroidEntryPoint
class FaqActivity : AppCompatActivity() {

    private var page = 0

    private lateinit var onScrollListener: EndlessRecyclerOnScrollListener

    private lateinit var faqAdapter: FaqAdapter

    private val viewModel: FaqViewModel by viewModels()

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObserve()
        loadData(page)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadData(currentPage: Int, needLoading: Boolean = true) {
        viewModel.loadFaqData(currentPage, needLoading)
    }

    private fun initViews() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        setUpRecyclerView()

        binding.btnMoreHelp.setOnClickListener {
            navigateToSupport()
        }

    }

    private fun initObserve() {

        viewModel.uiState.observe(this) { actionState ->
            when (actionState) {
                is UiStateFaq.Loading -> showLoading()
                is UiStateFaq.ErrorException -> {
                    hideLoading()
                    showException(actionState.error)
                }
                is UiStateFaq.Message -> {
                    hideLoading()
                    actionState.apply {
                        showMessageDialog(
                            title = title,
                            message = message,
                            positiveButtonText = positiveButton,
                            negativeButtonText = negativeButton,
                            cancelable = cancelable,
                            onPositiveButtonClick = onPositiveButtonClick
                        )
                    }
                }
                UiStateFaq.Logout -> {
                    hideLoading()
                    viewModel.logout()
                }
                UiStateFaq.LogoutSuccess -> {
                    hideLoading()
                    navigateToAuth()
                }
                is UiStateFaq.Data -> {
                    hideLoading()
                    loadedData(actionState.data)
                }
            }

        }
    }

    private fun setUpRecyclerView() {

        faqAdapter = FaqAdapter()

        // scroll listener for recycler view
        onScrollListener = object : EndlessRecyclerOnScrollListener(FAQ_QUERY_PER_PAGE) {
            override fun onLoadMore() {
                isLoading = true
                faqAdapter.showLoading()
                loadData(page, false)
            }
        }

        binding.rvFaq.apply {
            layoutManager = LinearLayoutManager(this@FaqActivity)
            addOnScrollListener(onScrollListener)
            addItemDecoration(
                DividerItemDecoration(
                    this@FaqActivity,
                    DividerItemDecoration.VERTICAL
                ).apply { setDrawable(getResDrawable(R.drawable.divider_items)!!) }
            )
            adapter = faqAdapter
        }
    }

    private fun loadedData(data: List<FaqItem>?) {
        hideLoading()
        faqAdapter.hideLoading()
        onScrollListener.isLoading = false
        if (data == null) return

        if (data.isEmpty()) {
            onScrollListener.isLastPage = true
        } else {
            faqAdapter.setItems(data)
        }

        page++
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
        finish()
    }

    private fun navigateToSupport() {
        val intent = Intent(this, SupportActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
    }

    private fun showLoading() {
        binding.nonDataContainer.status(NonDataLayout.Status.LOADING)
    }

    private fun hideLoading() {
        binding.nonDataContainer.status(NonDataLayout.Status.DATA)
    }

    private fun showException(throwable: Throwable) {
        if (throwable is IOException && !isInternetConnected()) {
            binding.nonDataContainer.status(NonDataLayout.Status.ERROR)
        } else {
            showMessageDialog(
                message = throwable.message ?: "",
                cancelable = false
            )
        }
    }

}