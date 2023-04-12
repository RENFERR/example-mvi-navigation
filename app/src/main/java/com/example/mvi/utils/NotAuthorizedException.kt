package com.example.mvi.utils

open class NotAuthorizedException : Exception() {
    override val message: String
        get() = "Authorization timed out. Log in again"
}