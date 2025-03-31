package me.programmerdmd.metropolitanmuseum.ui.screens.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

class TrendingScreenViewModel(
    private val repository: MuseumRepository
) : ViewModel() {

    private val _itemsFlow = MutableStateFlow<List<MuseumObject>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _isLastPage = MutableStateFlow(false)

    val itemsFlow: StateFlow<List<MuseumObject>> = _itemsFlow
    val isLoading: StateFlow<Boolean> = _isLoading
    val isLastPage: StateFlow<Boolean> = _isLastPage

    private var currentPage = 0

    init {
        loadMoreItems()
    }

    fun loadMoreItems() {
        if (_isLoading.value || _isLastPage.value) return

        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val objects = repository.search("Van Gogh", currentPage)
            if (objects.isEmpty()) {
                _isLastPage.value = true
            } else {
                _itemsFlow.value += objects.filterNotNull()
                currentPage++
            }
            _isLoading.value = false
        }
    }

}