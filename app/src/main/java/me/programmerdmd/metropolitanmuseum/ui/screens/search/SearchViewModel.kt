package me.programmerdmd.metropolitanmuseum.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

data class SearchResult(
    val query: String = "",
    val result: List<MuseumObject> = emptyList(),
    val searching: Boolean = false
)

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchResult = MutableStateFlow(SearchResult())
    val searchResult = _searchResult.asStateFlow()

    private var searchJob: Job? = null

    fun search(query: String) {
        searchJob = viewModelScope.launch {
            _searchResult.update { it.copy(
                searching = true
            ) }

            val result = searchRepository.search(query)
            _searchResult.update {
                it.copy(
                    query = query,
                    result = result,
                    searching = false
                )
            }
        }
    }

    fun clear() {
        searchJob?.cancel()
        searchJob = null
        _searchResult.value = SearchResult()
    }

}