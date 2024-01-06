package ski.mashiro.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ski.mashiro.common.GlobalBean
import ski.mashiro.component.MediaPlayerComponent
import ski.mashiro.component.PlayListComponent
import ski.mashiro.component.player.GuGuMediaPlayerController
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery

/**
 * @author mashirot
 * 2024/1/3 19:29
 */
@Composable
fun HomeView() {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1f, true)
                .border(2.dp, color = Color.Cyan)
        ) {
            PlayListComponent()
        }
        Divider(modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp))
        if (NativeDiscovery().discover()) {
            MediaPlayerComponent()
        } else {
            Box(
                modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "缺少VLC环境, 请安装VLC",
                    textAlign = TextAlign.Center
                )
            }
        }
        LaunchedEffect(GlobalBean.musicList.size) {
            if (GlobalBean.musicList.isNotEmpty() && !GuGuMediaPlayerController.hasMusic()) {
                GuGuMediaPlayerController.autoPlayNext()
            }
        }
    }
}