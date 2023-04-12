package com.example.mvi.utils

import com.example.mvi.R
import com.example.mvi.core.Response
import com.example.mvi.screens.list.contract.ListContract
import com.example.mvi.screens.user.contract.UserContract

object Mapper {

    object ResponseToState {

        fun Response<List<String>>.toListState(): ListContract.State = when (this) {
            is Response.Failure -> ListContract.State.Failure(
                message = this.exception.localizedMessage?.let {
                    UiText.DynamicString(value = it)
                } ?: UiText.StringResource(resId = R.string.unknown_exception)
            )
            is Response.Success -> if (this.data.isNullOrEmpty()) ListContract.State.Empty
            else ListContract.State.Loaded(data = this.data)
        }

        fun Response<String>.toUserState(): UserContract.State = when (this) {
            is Response.Failure -> UserContract.State.Failure(
                message = this.exception.localizedMessage?.let {
                    UiText.DynamicString(value = it)
                } ?: UiText.StringResource(resId = R.string.unknown_exception)
            )
            is Response.Success -> UserContract.State.Loaded(data = this.data)
        }
    }
}