package com.example.chessapp

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppViewModel(
) : ViewModel() {
    val name = mutableStateOf("")
    val db = Firebase.firestore
    val rows: List<Int> = (8 downTo 1).toList()
    val columns: List<Int> = (1..8).toList()
    var users = listOf<User>()
    val pieces = mutableStateOf(
        listOf(
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 1),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 2),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 3),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 4),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 5),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 6),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 7),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 8),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(1, 1),
                color = "White",
                imageId = R.drawable.ic_rook_white,
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(1, 2),
                color = "White",
                imageId = R.drawable.ic_knight_white
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(1, 3),
                color = "White",
                imageId = R.drawable.ic_bishop_white
            ),
            Figure(
                name = "Queen",
                coordinates = Pair(1, 4),
                color = "White",
                imageId = R.drawable.ic_queen_white
            ),
            Figure(
                name = "King",
                coordinates = Pair(1, 5),
                color = "White",
                imageId = R.drawable.ic_king_white
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(1, 6),
                color = "White",
                imageId = R.drawable.ic_bishop_white
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(1, 7),
                color = "White",
                imageId = R.drawable.ic_knight_white
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(1, 8),
                color = "White",
                imageId = R.drawable.ic_rook_white
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 1),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 2),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 3),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 4),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 5),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 6),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 7),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 8),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(8, 1),
                color = "Black",
                imageId = R.drawable.ic_rook_black
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(8, 2),
                color = "Black",
                imageId = R.drawable.ic_knight_black
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(8, 3),
                color = "Black",
                imageId = R.drawable.ic_bishop_black
            ),
            Figure(
                name = "Queen",
                coordinates = Pair(8, 4),
                color = "Black",
                imageId = R.drawable.ic_queen_black
            ),
            Figure(
                name = "King",
                coordinates = Pair(8, 5),
                color = "Black",
                imageId = R.drawable.ic_king_black
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(8, 6),
                color = "Black",
                imageId = R.drawable.ic_bishop_black
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(8, 7),
                color = "Black",
                imageId = R.drawable.ic_knight_black
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(8, 8),
                color = "Black",
                imageId = R.drawable.ic_rook_black
            ),
        )
    )
    var player: User = User("", "", 0, 0)
    var playerDidWin = false
    var loadedScore = mutableStateOf(Pair(0, 0))
    fun onFigureClick(
        coordinates: Pair<Int, Int>,
        onKingEat: () -> Unit
    ) {
        val selectedPiece = pieces.value.find { it.isSelected }
        var changed = false
        var updatedList = pieces.value.map { piece ->
            if (piece.isVisible && piece.coordinates == coordinates && selectedPiece == null) {
                changed = true
                piece.copy(isSelected = piece.isSelected.not())
            } else {
                if (selectedPiece != null && piece.isVisible && selectedPiece.color != piece.color && piece.coordinates == coordinates) {
                    changed = true
                    piece.copy(
                        name = selectedPiece.name,
                        color = selectedPiece.color,
                        imageId = selectedPiece.imageId,
                        isSelected = false
                    )
                } else {
                    piece
                }
            }
        }
        if (changed) {
            updatedList = updatedList.map {
                if (it.isSelected && selectedPiece != null) {
                    it.copy(isVisible = false, isSelected = false)
                } else {
                    it
                }
            }
        }

        pieces.value = updatedList
        if (pieces.value.count { it.name == "King" && it.color == "White" && it.isVisible } == 0 ||
            pieces.value.count { it.name == "King" && it.color == "Black" && it.isVisible } == 0) {
            onKingEat()
        }
    }

    init {
        getUsersNow()
    }

    fun onFieldClick(coordinates: Pair<Int, Int>) {
        val updatedList = pieces.value.map { piece ->
            if (piece.isVisible && piece.isSelected) {
                piece.copy(coordinates = coordinates, isSelected = false)
            } else {
                piece
            }
        }
        pieces.value = updatedList
    }

    fun resetBoard() {
        pieces.value = listOf(
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 1),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 2),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 3),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 4),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 5),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 6),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 7),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(2, 8),
                color = "White",
                imageId = R.drawable.ic_pawn_white,
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(1, 1),
                color = "White",
                imageId = R.drawable.ic_rook_white,
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(1, 2),
                color = "White",
                imageId = R.drawable.ic_knight_white
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(1, 3),
                color = "White",
                imageId = R.drawable.ic_bishop_white
            ),
            Figure(
                name = "Queen",
                coordinates = Pair(1, 4),
                color = "White",
                imageId = R.drawable.ic_queen_white
            ),
            Figure(
                name = "King",
                coordinates = Pair(1, 5),
                color = "White",
                imageId = R.drawable.ic_king_white
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(1, 6),
                color = "White",
                imageId = R.drawable.ic_bishop_white
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(1, 7),
                color = "White",
                imageId = R.drawable.ic_knight_white
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(1, 8),
                color = "White",
                imageId = R.drawable.ic_rook_white
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 1),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 2),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 3),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 4),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 5),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 6),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 7),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Pawn",
                coordinates = Pair(7, 8),
                color = "Black",
                imageId = R.drawable.ic_pawn_black
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(8, 1),
                color = "Black",
                imageId = R.drawable.ic_rook_black
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(8, 2),
                color = "Black",
                imageId = R.drawable.ic_knight_black
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(8, 3),
                color = "Black",
                imageId = R.drawable.ic_bishop_black
            ),
            Figure(
                name = "Queen",
                coordinates = Pair(8, 4),
                color = "Black",
                imageId = R.drawable.ic_queen_black
            ),
            Figure(
                name = "King",
                coordinates = Pair(8, 5),
                color = "Black",
                imageId = R.drawable.ic_king_black
            ),
            Figure(
                name = "Bishop",
                coordinates = Pair(8, 6),
                color = "Black",
                imageId = R.drawable.ic_bishop_black
            ),
            Figure(
                name = "Knight",
                coordinates = Pair(8, 7),
                color = "Black",
                imageId = R.drawable.ic_knight_black
            ),
            Figure(
                name = "Rook",
                coordinates = Pair(8, 8),
                color = "Black",
                imageId = R.drawable.ic_rook_black
            ),
        )
    }

    fun getUsersNow(uName: String = "") {
        viewModelScope.launch {
            async {
                users = getUsers()
                delay(250)
            }.invokeOnCompletion {
                Log.d("sdgvs",player.toString())
                if (uName.isNotBlank()) {
                    player = users.find { it.name == uName }!!
                }
            }
        }
    }

    fun createUserNow(name: String) {
        viewModelScope.launch {
            createUser(name, 0, 0)
        }
    }

    fun updateUserNow(isWin: Boolean) {
        viewModelScope.launch {
            async {
                if (player.id.isNotBlank()) {
                    if (isWin) {
                        updateWinsOrLosses(player.id, player.wins + 1, player.losses)
                    } else {
                        updateWinsOrLosses(player.id, player.wins, player.losses + 1)
                    }
                }
            }.invokeOnCompletion {
                getUsersNow()
            }
        }
    }

    private suspend fun getUsers(): List<User> {
        val users = db.collection("users")
        return try {
            val querySnapshot = users.get().await()

            querySnapshot.documents.map { document ->
                val userId = document.id
                val name = document.getString("name") ?: ""
                val wins = document.getDouble("wins") ?: 0
                val losses = document.getLong("losses") ?: 0

                User(userId, name, wins.toInt(), losses.toInt())
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun createUser(name: String, wins: Int, losses: Int) {
        val users = db.collection("users")
        users.add(
            mapOf(
                "name" to name,
                "wins" to wins,
                "losses" to losses
            )
        ).await()
    }

    private suspend fun updateWinsOrLosses(userId: String, newWins: Int, newLosses: Int) {
        val userDocRef = db.collection("users").document(userId)

        try {
            userDocRef.update(
                mapOf(
                    "wins" to newWins,
                    "losses" to newLosses
                )
            ).await()
        } catch (e: Exception) {
        }
    }
}