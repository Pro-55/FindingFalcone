package com.example.findingfalcone.util.wrappers

import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.util.extensions.asResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

fun <T> resourceFlow(
    doRetry: Boolean = false,
    block: suspend FlowCollector<Resource<T>>.() -> Unit
): Flow<Resource<T>> = flow(block).asResourceFlow(doRetry)