package me.programmerdmd.metropolitanmuseum.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute

class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val objectId: Int = savedStateHandle.toRoute<DetailRoute>().objectId

}