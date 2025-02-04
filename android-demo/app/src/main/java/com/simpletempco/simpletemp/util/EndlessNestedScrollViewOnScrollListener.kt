package com.simpletempco.simpletemp.util

import androidx.core.widget.NestedScrollView

abstract class EndlessNestedScrollViewOnScrollListener() : NestedScrollView.OnScrollChangeListener {

    var isLoading = false
    var isLastPage = false

    private val visibleThresholdDistance = 300

    override fun onScrollChange(
        v: NestedScrollView,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {

        // We take the last son in the scrollview
        val view = v.getChildAt(v.childCount - 1)
        val distanceToEnd = view.bottom - (v.height + v.scrollY)

        if (!isLoading && !isLastPage && distanceToEnd <= visibleThresholdDistance) {
            isLoading = true
            onLoadMore()
        }


        /*if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
            val isNotLoadingAndIsNotLastPage = !isLoading && !isLastPage
            if (isNotLoadingAndIsNotLastPage) {

                if (!loadedAllItems) {
                    showUnSentData()
                }
            }
        }*/
    }

    fun resetOnLoadMore() {
        isLastPage = false
        isLoading = true
    }

    abstract fun onLoadMore()

}