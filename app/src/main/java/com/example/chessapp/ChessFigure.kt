package com.example.chessapp

data class Figure(
    val name: String,
    val possibleMoves: List<Pair<Int, Int>> = listOf(),
    var coordinates: Pair<Int, Int>,
    val color: String,
    var isVisible: Boolean = true,
    val imageId: Int,
    var isSelected: Boolean = false,
)