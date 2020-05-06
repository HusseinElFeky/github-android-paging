package com.husseinelfeky.githubpaging.common.paging.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.husseinelfeky.githubpaging.common.paging.state.NetworkState
import com.husseinelfeky.githubpaging.common.paging.state.PagedListState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class PagingViewModel<Entity> : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private var pagedListDisposable: Disposable? = null

    protected val _pagedListState = MutableLiveData<PagedListState>()
    val pagedListState: LiveData<PagedListState>
        get() = _pagedListState

    protected val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    protected val _refreshState = MutableLiveData<NetworkState>()
    val refreshState: LiveData<NetworkState>
        get() = _refreshState

    var currentPage = 1

    abstract fun loadInitialPage(callback: ItemsLoadedCallback<Entity>): Disposable

    abstract fun loadNextPage(callback: ItemsLoadedCallback<Entity>): Disposable

    abstract fun invalidateDataSource(callback: ItemsLoadedCallback<Entity>): Disposable

    fun retryFetchingNextPage(callback: ItemsLoadedCallback<Entity>) {
        fetchNextPage(callback, true)
    }

    fun fetchInitialPage(callback: ItemsLoadedCallback<Entity>) {
        pagedListDisposable?.dispose()
        pagedListDisposable = loadInitialPage(callback)
    }

    private fun fetchNextPageInternal(callback: ItemsLoadedCallback<Entity>) {
        pagedListDisposable?.dispose()
        pagedListDisposable = loadNextPage(callback)
    }

    fun refresh(callback: ItemsLoadedCallback<Entity>) {
        pagedListDisposable?.dispose()
        pagedListDisposable = invalidateDataSource(callback)
    }

    /**
     *  Do not fetch next page if it is already fetching or there is an error
     *  unless it is a forced fetch.
     */
    fun fetchNextPage(
        callback: ItemsLoadedCallback<Entity>,
        forceFetch: Boolean = false
    ): Boolean {
        if (forceFetch || !(_networkState.value is NetworkState.Loading || _networkState.value is NetworkState.Error)) {
            fetchNextPageInternal(callback)
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        pagedListDisposable?.dispose()
        compositeDisposable.clear()
    }
}
