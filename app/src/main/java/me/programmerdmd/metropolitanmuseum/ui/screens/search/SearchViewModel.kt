package me.programmerdmd.metropolitanmuseum.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.SearchRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _queryFlow = MutableStateFlow("")
    private val _searching = MutableStateFlow(false)
    private val _results = MutableStateFlow<List<MuseumObject>>(emptyList())

    val queryFlow: StateFlow<String> = _queryFlow
    val searching: StateFlow<Boolean> = _searching
    val results: StateFlow<List<MuseumObject>> = _results

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .filterNot { query -> query.isEmpty() }
                .flatMapLatest { query -> flow {
                    _searching.value = true
                    val results = searchRepository.search(query)
                    emit(results)
                    _searching.value = false
                }
                }.collect { list ->
                    _results.update { list }
                }
        }
    }

    fun search(query: String) {
        _queryFlow.value = query
    }

}