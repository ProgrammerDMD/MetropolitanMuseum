package me.programmerdmd.metropolitanmuseum.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import me.programmerdmd.metropolitanmuseum.objects.database.FavoriteDao

class FavoritesScreenViewModel(
    private val repository: MuseumRepository,
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    private val _itemsFlow = MutableStateFlow<List<MuseumObject>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _isLastPage = MutableStateFlow(false)

    val itemsFlow: StateFlow<List<MuseumObject>> = _itemsFlow
    val isLoading: StateFlow<Boolean> = _isLoading
    val isLastPage: StateFlow<Boolean> = _isLastPage

    private var currentPage = 0

    init {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteDao.getAllFlow().drop(1).collect { data ->
                _isLoading.value = true
                _isLastPage.value = false

                _itemsFlow.value = emptyList()
                currentPage = 0
                _loadMoreItems()

                _isLoading.value = false
            }
        }
    }

    private suspend fun _loadMoreItems() {
        val objects = repository.getObjectsFromLocal(currentPage)
        if (objects.isEmpty()) {
            _isLastPage.value = true
        } else {
            _itemsFlow.value += objects.filterNotNull()
            currentPage++
        }
    }

    fun loadMoreItems() {
        if (_isLoading.value || _isLastPage.value) return

        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _loadMoreItems()
            _isLoading.value = false
        }
    }
}