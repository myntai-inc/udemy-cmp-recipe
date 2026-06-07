package jp.myntai.udemy.recipe.viewmodel

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException

fun Exception.toUserFriendlyMessage(): String = when (this) {
    // Timeouts are themselves IOExceptions, so they must be matched before the
    // generic IOException branch below.
    is ConnectTimeoutException,
    is SocketTimeoutException,
    is HttpRequestTimeoutException ->
        "Connection timed out. Please try again."
    is ClientRequestException ->
        "Request failed. Please try again."
    is ServerResponseException ->
        "Server error. Please try again later."
    // Ktor surfaces low-level network failures (no connectivity, host unreachable,
    // socket errors) as kotlinx.io.IOException on every engine. On JVM/Android this
    // also covers java.io.UnknownHostException, since IOException is a typealias there.
    is IOException ->
        "No internet connection. Please check your network."
    else ->
        message ?: "An unexpected error occurred."
}
