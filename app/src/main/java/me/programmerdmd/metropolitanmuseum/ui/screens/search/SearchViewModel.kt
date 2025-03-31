package me.programmerdmd.metropolitanmuseum.ui.screens.search

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

data class SearchQuery(
    val query: String,
    val page: Int
)

@SuppressLint("StaticFieldLeak")
class SearchViewModel(
    private val repository: MuseumRepository,
) : ViewModel() {

    private val _queryFlow = MutableStateFlow(SearchQuery("", 0))
    private val _isLoading = MutableStateFlow(false)
    private val _results = MutableStateFlow<List<MuseumObject>>(emptyList())
    private val _isLastPage = MutableStateFlow(false)

    val queryFlow: StateFlow<SearchQuery> = _queryFlow
    val isLoading: StateFlow<Boolean> = _isLoading
    val results: StateFlow<List<MuseumObject>> = _results
    val isLastPage: StateFlow<Boolean> = _isLastPage

    init {
        viewModelScope.launch {
            _queryFlow
                .debounce(500)
                .filterNot { query -> query.query.isEmpty() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    flow {
                        _isLoading.value = true
                        try {
                            val results = repository.search(query.query, query.page)
                            if (results.isEmpty()) _isLastPage.value = true
                            emit(results)
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        } finally {
                            _isLoading.value = false
                        }
                    }
                }.collect { list ->
                    _results.value += list.filterNotNull()
                }
        }
    }

    fun loadMoreItems() {
        if (_isLoading.value || _queryFlow.value.query.isEmpty() || _isLastPage.value) return
        _queryFlow.value = _queryFlow.value.copy(page = _queryFlow.value.page + 1)
    }

    fun search(query: String) {
        _isLastPage.value = false
        _results.value = emptyList()
        _queryFlow.value = SearchQuery(query = query, page = 0)
    }

}