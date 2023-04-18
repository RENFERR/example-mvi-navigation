package com.example.mvi.screens.login.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.repository.LoginStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IsLoggedViewModel @Inject constructor(
    private val repository: LoginStateRepository
) : ViewModel() {

    val isLoggedFlow = repository.isLoggedIn
    val isLogged get() = isLoggedFlow.value
}