package com.example.chessapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FinishedScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    onPlayAgainClick: () -> Unit,
    onChangePlayerClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 40.dp),
                text = if (viewModel.playerDidWin) {
                    viewModel.updateUserNow(true)
                    "You won!!"
                } else {
                    viewModel.updateUserNow(false)
                    "You lost :("
                },
                fontSize = 40.sp
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = "${viewModel.player.name}'s all time score",
                fontSize = 20.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = if (viewModel.playerDidWin) {
                        "Wins " + (viewModel.player.wins + 1).toString()
                    } else {
                        "Wins " + (viewModel.player.wins).toString()
                    },
                    fontSize = 20.sp
                )
                Text(
                    text = "-----",
                    fontSize = 20.sp
                )
                Text(
                    text = if (!viewModel.playerDidWin) {
                        (viewModel.player.losses + 1).toString() + " losses"
                    } else {
                        (viewModel.player.losses).toString() + " losses"
                    },
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { onPlayAgainClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF32578D), contentColor = Color.White
                    )
                ) {
                    Text(text = "Play again")
                }
                Button(
                    onClick = { onChangePlayerClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF32578D), contentColor = Color.White
                    )
                ) {
                    Text(text = "Change player")
                }
            }
        }
    }
}