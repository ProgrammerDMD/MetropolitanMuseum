package me.programmerdmd.metropolitanmuseum.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

class HomeScreenViewModel(
    private val museumRepository: MuseumRepository,
) : ViewModel() {

    private val _itemsFlow = MutableStateFlow<List<MuseumObject>>(emptyList())
    val itemsFlow: StateFlow<List<MuseumObject>> = _itemsFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0
    private var isLastPage = false

    init {
        loadMoreItems()
    }

    fun loadMoreItems() {
        if (isLoading.value || isLastPage) return

        _isLoading.value = true
        viewModelScope.launch {
            museumRepository.getObjects(currentPage)
                .collect { newItems ->
                    if (newItems.isEmpty()) {
                        isLastPage = true
                    } else {
                        _itemsFlow.value += newItems
                        currentPage++
                    }
                    _isLoading.value = false
                }
        }
    }
}