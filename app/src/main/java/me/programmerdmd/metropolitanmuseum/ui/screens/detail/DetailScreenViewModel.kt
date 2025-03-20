package me.programmerdmd.metropolitanmuseum.ui.screens.detail

import android.content.Context
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
import kotlinx.coroutines.launch
import me.programmerdmd.metropolitanmuseum.network.repositories.MuseumRepository
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import java.io.IOException

class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    museumRepository: MuseumRepository,
    context: Context
) : ViewModel() {

    val objectId: Int = savedStateHandle.toRoute<DetailRoute>().objectId
    val title: String = savedStateHandle.toRoute<DetailRoute>().title

    private val _museumFlow = MutableStateFlow<MuseumObject?>(null)
    private val _searchingFlow = MutableStateFlow(true)

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
    }

}