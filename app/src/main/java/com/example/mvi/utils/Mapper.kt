package com.example.mvi.utils

import com.example.mvi.R
import com.example.mvi.screens.list.contract.ListContract
import com.example.mvi.screens.user.contract.UserContract
import com.example.mvi.screens.user.models.User

object Mapper {

    object ResponseToState {

        fun Result<List<String>?>.toListState(): ListContract.State =
            fold(
                onSuccess = { data ->
                    if (data.isNullOrEmpty()) ListContract.State.Empty
                    else ListContract.State.Loaded(data = data)
                },
                onFailure = { throwable ->
                    ListContract.State.Failure(
                        message = throwable.localizedMessage?.let { message ->
                            UiText.DynamicString(value = message)
                        } ?: UiText.StringResource(resId = R.string.unknown_exception)
                    )
                }
            )

        fun Result<User?>.toUserState(): UserContract.State = fold(
            onSuccess = { data ->
                UserContract.State.Loaded(data = data)
            },
            onFailure = { throwable ->
                UserContract.State.Failure(
                    message = throwable.localizedMessage?.let { message ->
                        UiText.DynamicString(value = message)
                    } ?: UiText.StringResource(resId = R.string.unknown_exception)
                )
            }
        )
    }
}