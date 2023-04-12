package com.example.mvi.utils

import com.example.mvi.destinations.ErrorDialogDestination
import com.example.mvi.destinations.LoginScreenDestination
import com.example.mvi.destinations.LogoutDialogDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

object NavigatorExtend {

    fun DestinationsNavigator.logoutWithDialog(message: String, currentRoute: String) {
        navigate(
            direction = LogoutDialogDestination.invoke(
                message = message,
                currentRoute = currentRoute
            )
        )
    }

    fun DestinationsNavigator.logout(currentRoute: String) {
        navigate(direction = LoginScreenDestination) {
            popUpTo(route = currentRoute) {
                inclusive = true
            }
        }
    }

    fun DestinationsNavigator.showErrorDialog(message: String) {
        navigate(
            direction = ErrorDialogDestination.invoke(
                message = message
            )
        )
    }
}