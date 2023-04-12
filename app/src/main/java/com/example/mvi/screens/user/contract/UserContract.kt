package com.example.mvi.screens.user.contract

import com.example.mvi.core.ViewEffect
import com.example.mvi.core.ViewIntent
import com.example.mvi.core.ViewState
import com.example.mvi.utils.UiText

object UserContract {

    sealed class State : ViewState {
        object Idle : State()
        object Loading : State()
        data class Loaded(val data: String?) : State()
        data class Failure(val message: UiText) : State()
    }

    sealed class Intent : ViewIntent {
        object UpdateScreen : Intent()
    }

    sealed class Effect : ViewEffect {
        data class ShowToast(val message: UiText) : Effect()
        data class ShowSnackBar(val message: UiText) : Effect()
    }
}