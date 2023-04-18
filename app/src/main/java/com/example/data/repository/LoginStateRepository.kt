package com.example.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Random

class LoginStateRepository {

    private suspend fun isLogged(): Boolean {
        delay(1000L)
        return Random().nextBoolean()
    }

    private val _isLoggedIn = MutableStateFlow(value = runBlocking { isLogged() })
    val isLoggedIn = _isLoggedIn.asStateFlow()

    suspend fun login() = withContext(Dispatchers.IO) {
        delay(1000L)
        _isLoggedIn.update { true }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        _isLoggedIn.update { false }
    }
}