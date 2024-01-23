package com.example.chessapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chessapp.ui.theme.ChessAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel = AppViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChessAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("login") {
                            LoginScreen(onContinueClick = { name: String ->
                                if (name.isNotBlank()) {
                                    if (!viewModel.users.any { it.name == name }) {
                                        viewModel.createUserNow(name)
                                    }
                                    viewModel.getUsersNow(name)
                                    navController.navigate("chess")
                                }
                            }, viewModel = viewModel)
                        }
                        composable("chess") {
                            Board(viewModel = viewModel) {
                                viewModel.playerDidWin =
                                    viewModel.pieces.value.count { it.name == "King" && it.isVisible && it.color == "Black" } == 0
                                navController.navigate("winScreen")
                            }
                        }
                        composable("winScreen") {
                            FinishedScreen(
                                viewModel = viewModel,
                                onPlayAgainClick = {
                                    viewModel.getUsersNow(viewModel.name.value)
                                    navController.popBackStack()
                                    viewModel.resetBoard()
                                },
                                onChangePlayerClick = {
                                    viewModel.name.value = ""
                                    viewModel.getUsersNow()
                                    navController.navigate("login") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                    viewModel.resetBoard()
                                    viewModel.player = User("", "", 0, 0)
                                    viewModel.loadedScore.value = Pair(0, 0)
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChessAppTheme {
        Greeting("Android")
    }
}