package com.example.mvi.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A sealed class representing a text for ViewModels that can be either a [DynamicString] or a [StringResource].
 *
 * @see DynamicString
 * @see StringResource
 */
sealed class UiText {

    /**
     * A data class representing a dynamic string.
     *
     * @property value The actual value of the string.
     */
    data class DynamicString(val value: String) : UiText()

    /**
     * A class representing a string resource.
     *
     * @property resId The resource ID of the string.
     * @property args The optional arguments to format the string.
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    /**
     * Returns the string representation of this [UiText] for use in a Composable function.
     *
     * @return A string representation of this [UiText] for use in a Composable function.
     */
    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id = resId, formatArgs = *args)
    }

    /**
     * Returns the string representation of this [UiText] for use in a non-Composable context.
     *
     * @param context The context used to retrieve the string resource.
     * @return A string representation of this [UiText] for use in a non-Composable context.
     */
    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, *args)
    }
}