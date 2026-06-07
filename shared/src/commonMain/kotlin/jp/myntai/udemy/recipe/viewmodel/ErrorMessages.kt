package jp.myntai.udemy.recipe.viewmodel

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException

fun Exception.toUserFriendlyMessage(): String = when (this) {
    is ConnectTimeoutException,
    is HttpRequestTimeoutException ->
        "Connection timed out. Please try again."
    is ClientRequestException ->
        "Request failed. Please try again."
    is ServerResponseException ->
        "Server error. Please try again later."
    else -> when {
        // KMP ではプラットフォーム固有の例外クラスを直接参照できないため
        // クラス名ベースで判定
        this::class.simpleName == "UnknownHostException" ->
            "No internet connection. Please check your network."
        else ->
            message ?: "An unexpected error occurred."
    }
}
