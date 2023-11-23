package com.example.findingfalcone.model

import android.content.Context
import androidx.annotation.StringRes

sealed class UIMessage {
    data class DynamicString(val value: String) : UIMessage()
    class StringResource(
        @StringRes val resId: Int,
        val args: Array<Any> = emptyArray()
    ) : UIMessage()

    fun getMessage(context: Context) = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId, args)
    }
}
