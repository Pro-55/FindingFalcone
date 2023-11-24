package com.example.findingfalcone.model

import android.content.res.Resources
import androidx.annotation.StringRes

sealed class UIMessage {
    data class DynamicString(val value: String) : UIMessage()
    class StringResource(
        @StringRes val resId: Int,
        val args: Array<Any> = emptyArray()
    ) : UIMessage()

    fun getMessage(resources: Resources) = when (this) {
        is DynamicString -> value
        is StringResource -> resources.getString(resId, args)
    }
}