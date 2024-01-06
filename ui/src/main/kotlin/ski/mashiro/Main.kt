package ski.mashiro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import moe.tlaster.precompose.PreComposeApp
import ski.mashiro.page.HomePage
import java.awt.Dimension

/**
 * @author mashirot
 * 2024/1/3 15:03
 */
fun main() = application {
    Window(
        title = "GuGu-AOD",
        icon = painterResource("icon.ico"),
        state = rememberWindowState(
            width = 500.dp,
            height = 700.dp,
            position = WindowPosition(Alignment.Center)
        ),
        onCloseRequest = {
            BackendMain.onClose()
            exitApplication()
        }
    ) {
        window.minimumSize = Dimension(600, 400)
        BackendMain.init()
        App()
    }
}

@Composable
@Preview
fun App() {
    PreComposeApp {
        MaterialTheme {
            HomePage()
        }
    }
}