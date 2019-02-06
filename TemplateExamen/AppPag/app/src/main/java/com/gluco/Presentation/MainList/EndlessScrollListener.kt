package com.gluco.Presentation.MainList

import android.widget.AbsListView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    var currentPage: Int = 0
    var visibleThreshold: Int = 5
    var previousTotalItemCount: Int = 0
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var loading: Boolean = false
    var startingPageIndex: Int = 0

    abstract fun onLoadMore(page: Int)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        // Don't take any action on changed
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        totalItemCount = linearLayoutManager.itemCount
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            currentPage++
            loading = true
            onLoadMore(currentPage)
        }
    }
}