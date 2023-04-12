package com.example.mvi.ui.elements.dialogs.styles

import androidx.compose.ui.window.DialogProperties
import com.ramcosta.composedestinations.spec.DestinationStyle

object ErrorDialogStyle : DestinationStyle.Dialog {
    override val properties: DialogProperties
        get() = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            decorFitsSystemWindows = true,
            usePlatformDefaultWidth = true
        )
}