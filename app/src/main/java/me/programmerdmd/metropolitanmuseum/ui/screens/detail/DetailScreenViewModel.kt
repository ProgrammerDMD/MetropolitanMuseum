package me.programmerdmd.metropolitanmuseum.ui.screens.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import me.programmerdmd.metropolitanmuseum.objects.database.FavoriteDao
import me.programmerdmd.metropolitanmuseum.objects.database.FavoriteObject
import java.io.IOException

class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    museumRepository: MuseumRepository,
    private val context: Context,
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    val objectId: Int = savedStateHandle.toRoute<DetailRoute>().objectId
    val title: String = savedStateHandle.toRoute<DetailRoute>().title

    private val _isFavorite = MutableStateFlow(false)
    private val _museumFlow = MutableStateFlow<MuseumObject?>(null)
    private val _searchingFlow = MutableStateFlow(true)

    val isFavorite: StateFlow<Boolean> = _isFavorite
    val museumFlow: StateFlow<MuseumObject?> = _museumFlow
    val searchingFlow: StateFlow<Boolean> = _searchingFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _searchingFlow.value = true
            try {
                val museumObject = museumRepository.getObject(objectId)
                _museumFlow.value = museumObject

                museumObject?.additionalImages?.map { image ->
                    async {
                        try {
                            val futureTarget = Glide.with(context)
                                .downloadOnly()
                                .load(image)
                                .submit()

                            futureTarget.get()
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    }
                }?.awaitAll()

            } catch (exception: IOException) {
                exception.printStackTrace()
                _searchingFlow.value = false
            }
            _searchingFlow.value = false
        }

        viewModelScope.launch(Dispatchers.IO) {
            favoriteDao.exists(objectId).collectLatest { exists ->
                _isFavorite.value = exists
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteObject = FavoriteObject(objectId)
            if (_isFavorite.value) {
                favoriteDao.delete(favoriteObject)
            } else {
                favoriteDao.insertAll(favoriteObject)
            }

            withContext(Dispatchers.Main) {
                val toast = Toast.makeText(context, if (!_isFavorite.value) "Added to Favorites" else "Removed from Favorites", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

}