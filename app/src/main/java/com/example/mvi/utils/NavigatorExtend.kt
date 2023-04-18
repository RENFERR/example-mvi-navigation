package com.example.mvi.utils

import com.example.mvi.destinations.ErrorDialogDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

object NavigatorExtend {

    fun DestinationsNavigator.showErrorDialog(message: String) {
        navigate(
            direction = ErrorDialogDestination.invoke(
                message = message
            )
        )
    }
}