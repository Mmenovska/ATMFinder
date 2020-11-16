package com.gsixacademy.android.atmfinder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener ( var layoutManager : LinearLayoutManager ) : RecyclerView.OnScrollListener() {

    abstract fun isLastPage() : Boolean

    abstract fun isLoading() : Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount  = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

        if (isLoading()&& isLastPage()){
            if (visibleItemCount + firstVisiblePosition >= totalItemCount && firstVisiblePosition >= 0){
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()
}