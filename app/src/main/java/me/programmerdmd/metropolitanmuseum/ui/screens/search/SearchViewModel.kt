package me.programmerdmd.metropolitanmuseum.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

data class SearchResult(
    private val query: String = "",
    private val result: List<MuseumObject> = listOf()
)

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchResult = MutableStateFlow(SearchResult())
    val searchResult = _searchResult.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            val result = searchRepository.search(query)
            _searchResult.update {
                it.copy(
                    query = query,
                    result = result
                )
            }
        }
    }

}