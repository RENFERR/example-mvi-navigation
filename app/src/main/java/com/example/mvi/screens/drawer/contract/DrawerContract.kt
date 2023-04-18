package com.example.mvi.screens.drawer.contract

import com.example.mvi.core.ViewEffect
import com.example.mvi.core.ViewIntent
import com.example.mvi.core.ViewState
import com.example.mvi.screens.user.models.User
import java.lang.Exception

object DrawerContract {

    sealed class State : ViewState {
        object Loading : State()
        data class Loaded(val data: User) : State()
        data class Failure(val exception: Exception) : State()
    }

    sealed class Effect : ViewEffect

    sealed class Intent : ViewIntent {
        object Update : Intent()
        object Logout : Intent()
    }
}