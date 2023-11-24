package com.example.findingfalcone.model

data class SelectionScreenState(
    val selectedCount: Int,
    val estimatedTime: Int,
    val planets: List<SelectionItem>
)