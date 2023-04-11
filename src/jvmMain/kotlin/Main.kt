@file:JvmName("Main")

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.net.*
import data.net.xmlrpc.*
import view.MainScreen
import viewmodel.MainScreenViewModel

@Composable
@Preview
fun App() {
    val applicationScope = rememberCoroutineScope()
    val viewModel = MainScreenViewModel(parentScope = applicationScope)
    MainScreen(viewModel)
}

fun main() = application {
    Window(
        title = "The Tree Of Time",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
