package com.example.findingfalcone.util.wrappers

import com.example.findingfalcone.R
import com.example.findingfalcone.model.UIMessage
import com.example.findingfalcone.model.network.Response
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import java.net.UnknownHostException

suspend fun <T> safeCall(block: suspend () -> Response<T>): Response<T> {
    return try {
        block.invoke()
    } catch (e: UnknownHostException) {
        Response.UnknownHostException(msg = UIMessage.StringResource(resId = R.string.error_unknown_host))
    } catch (e: RedirectResponseException) {
        Response.InvalidPathException(msg = UIMessage.StringResource(resId = R.string.error_invalid_path))
    } catch (e: ClientRequestException) {
        Response.InvalidRequestException(msg = UIMessage.StringResource(resId = R.string.error_invalid_request))
    } catch (e: ServerResponseException) {
        Response.ServerException(msg = UIMessage.StringResource(resId = R.string.error_server_exception))
    } catch (e: Exception) {
        Response.UnknownException(msg = UIMessage.StringResource(resId = R.string.error_unknown))
    }
}