package com.example.chessapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Board(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    onKingEat: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenWidthDp = configuration.screenWidthDp

    val screenWidthPx = with(LocalDensity.current) {
        screenWidthDp.dp
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7FA1C0)), Alignment.Center
    ) {
        Column(Modifier) {
            var counter = 0
            viewModel.rows.forEach { row ->
                counter++
                Row {
                    viewModel.columns.forEach { column ->
                        val currentFigure = viewModel.pieces.value.find {
                            it.isVisible && it.coordinates == Pair(row, column)
                        }
                        if (currentFigure != null) {
                            Box(
                                modifier = Modifier
                                    .size((screenWidthPx / 8))
                                    .background(
                                        if (counter % 2 == 0) {
                                            Color.LightGray
                                        } else {
                                            Color.White
                                        }
                                    )
                                    .clickable {
                                        if (!currentFigure.isSelected) {
                                            viewModel.onFigureClick(
                                                currentFigure.coordinates
                                            ) { onKingEat() }
                                        } else {
                                            viewModel.onFieldClick(
                                                currentFigure.coordinates
                                            )
                                        }
                                    },
                                Alignment.Center
                            ) {
                                Image(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(
                                            if (currentFigure.isSelected) {
                                                Color.Green
                                            } else {
                                                Color.Transparent
                                            }
                                        ),
                                    painter = painterResource(id = currentFigure.imageId),
                                    contentDescription = null
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size((screenWidthPx / 8))
                                    .background(
                                        if (counter % 2 == 0) {
                                            Color.LightGray
                                        } else {
                                            Color.White
                                        }
                                    )
                                    .clickable {
                                        viewModel.onFieldClick(
                                            Pair(row, column)
                                        )
                                    })
                        }
                        counter++
                    }
                }
            }
        }
    }
}