package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpletempco.simpletemp.data.remote.models.response.Rating
import com.simpletempco.simpletemp.databinding.FragmentMyRatingsBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.AppConfig.RATING_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRatingFragment : BaseFragment<MyRatingViewModel>() {

    private var page = 0

    private lateinit var ratingAdapter: MyRatingAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: MyRatingViewModel by viewModels()

    private var _binding: FragmentMyRatingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRatingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData(page) }
        loadData(page)
    }

    private fun loadData(currentPage: Int, needLoading: Boolean = true) {
        viewModel.getRating(currentPage, needLoading)
    }

    private fun initViews() {

        setUpRecyclerView()

    }

    private fun initObserve() {
        viewModel.ratingResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            setRatingInfo(data)
        }
    }

    private fun setUpRecyclerView() {

        ratingAdapter = MyRatingAdapter()

        // scroll listener for recycler view
        onScrollListener = object : EndlessNestedScrollViewOnScrollListener() {
            override fun onLoadMore() {
                isLoading = true
                ratingAdapter.showLoading()
                loadData(page, false)
            }
        }

        binding.rvRating.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ratingAdapter
        }

        binding.nsv.setOnScrollChangeListener(onScrollListener)

    }

    private fun setRatingHeaderDataInfo(data: Rating) {

        binding.apply {

            tvRating5.text = (data.star5?.count ?: 0).toString()
            rbRating5.rating(data.star5?.avg ?: 0)
            pgbRating5.max = data.ratesCount ?: 0
            pgbRating5.progress = data.star5?.count ?: 0

            tvRating4.text = (data.star4?.count ?: 0).toString()
            rbRating4.rating(data.star4?.avg ?: 0)
            pgbRating4.max = data.ratesCount ?: 0
            pgbRating4.progress = data.star4?.count ?: 0

            tvRating3.text = (data.star3?.count ?: 0).toString()
            rbRating3.rating(data.star3?.avg ?: 0)
            pgbRating3.max = data.ratesCount ?: 0
            pgbRating3.progress = data.star3?.count ?: 0

            tvRating2.text = (data.star2?.count ?: 0).toString()
            rbRating2.rating(data.star2?.avg ?: 0)
            pgbRating2.max = data.ratesCount ?: 0
            pgbRating2.progress = data.star2?.count ?: 0

            tvRating1.text = (data.star1?.count ?: 0).toString()
            rbRating1.rating(data.star1?.avg ?: 0)
            pgbRating1.max = data.ratesCount ?: 0
            pgbRating1.progress = data.star1?.count ?: 0

            rbRatingAvg.rating((data.averages?.rate ?: 0).toInt())
            tvRatingAvg.text = (data.averages?.rate ?: 0).toString()

            rbRatingProfessionalism.rating((data.averages?.professionalism ?: 0).toInt())
            rbRatingTimekeeping.rating((data.averages?.timekeeping ?: 0).toInt())
            rbRatingCompetencyAndSkills.rating(
                (data.averages?.competencyAndSkills ?: 0).toInt()
            )

        }
    }

    private fun setRatingInfo(data: Rating?) {
        ratingAdapter.hideLoading()
        onScrollListener.isLoading = false

        if (page == 0) {
            data?.let { info -> setRatingHeaderDataInfo(info) }
        }

        if (data?.rates.isNullOrEmpty()) {
            onScrollListener.isLastPage = true
        } else {
            if (data?.rates?.size!! < RATING_QUERY_PER_PAGE)
                onScrollListener.isLastPage = true

            ratingAdapter.setItems(data.rates!!)
        }

        page++
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}