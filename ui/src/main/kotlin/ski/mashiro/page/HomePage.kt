package ski.mashiro.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ski.mashiro.common.GlobalBean
import ski.mashiro.component.MenuComponent
import ski.mashiro.component.player.GuGuMediaPlayerController
import ski.mashiro.router.Router
import ski.mashiro.service.impl.NeteaseCloudMusicServiceImpl

/**
 * @author mashirot
 * 2024/1/3 19:33
 */
@Composable
fun HomePage() {
    var tempFailedMsg by remember { mutableStateOf("") }
    var showFailedDialog by remember { mutableStateOf(false) }
    if (showFailedDialog) {
        AlertDialog(
            modifier = Modifier.width(250.dp).height(120.dp),
            text = {
                Text(
                    text = tempFailedMsg,
                    textAlign = TextAlign.Start
                )
            },
            onDismissRequest = {
                showFailedDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showFailedDialog = false
                    },
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "OK"
                    )
                }
            }
        )
    }
    Row(modifier = Modifier.fillMaxSize()) {
        MenuComponent()
        Divider(
            modifier = Modifier.width(1.5.dp).fillMaxHeight(),
            color = Color.LightGray
        )
        Box(
            modifier = Modifier.padding(10.dp)
                .fillMaxSize()
        ) {
            Router.navController = Router.initNavController()
        }
    }
    GlobalBean.IO_SCOPE.launch {
        runCatching {
            GlobalBean.neteaseCloudMusicLoginStatus = NeteaseCloudMusicServiceImpl.getLoginStatus()
        }.getOrElse {
            tempFailedMsg = it.message!!
            showFailedDialog = true
        }
    }
    LaunchedEffect(GlobalBean.musicList.size) {
        if (GlobalBean.musicList.isNotEmpty() && !GuGuMediaPlayerController.hasMusic()) {
            GuGuMediaPlayerController.autoPlayNext()
        }
    }
}










