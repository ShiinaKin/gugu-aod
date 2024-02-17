package ski.mashiro.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import ski.mashiro.common.GlobalBean
import ski.mashiro.router.Router
import ski.mashiro.service.impl.WebSocketServiceImpl
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery

/**
 * @author mashirot
 * 2024/1/3 19:29
 */
private val log = KotlinLogging.logger { }

@Composable
fun MenuComponent() {
    notification()
    Column(
        modifier = Modifier
            .padding(5.dp)
            .width(64.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            val btnModifier = Modifier.size(64.dp)
            IconButton(
                onClick = {
                    Router.navController.navigate("/home")
                },
                modifier = btnModifier,
            ) {
                Image(
                    painter = painterResource("icons/home.svg"),
                    "homeIcon",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(
                onClick = {
                    Router.navController.navigate("/manual_song_req")
                },
                modifier = btnModifier,
            ) {
                Image(
                    painter = painterResource("icons/manage_search.svg"),
                    "searchIcon",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(
                onClick = {
                    Router.navController.navigate("/setting")
                },
                modifier = btnModifier,
            ) {
                Image(
                    painter = painterResource("icons/tune.svg"),
                    "tuneIcon",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(
                onClick = {
                    Router.navController.navigate("/config")
                },
                modifier = btnModifier,
            ) {
                Image(
                    painter = painterResource("icons/setting.svg"),
                    "settingIcon",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(
                onClick = {
                    Router.navController.navigate("/info")
                },
                modifier = btnModifier,
            ) {
                Image(
                    painter = painterResource("icons/info.svg"),
                    "infoIcon",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (NativeDiscovery().discover()) {
            Column(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SeasonDisplayComponent()
                var connectStatus by remember { mutableStateOf(false to "未连接") }
                LaunchedEffect(GlobalBean.webSocket) {
                    connectStatus = if (GlobalBean.webSocket != null) true to "已连接" else false to "未连接"
                    log.debug { "webSocket changed, websocket: ${GlobalBean.webSocket}" }
                    log.debug { "connectStatus: $connectStatus" }
                }
                Button(
                    onClick = {
                        GlobalBean.IO_SCOPE.launch {
                            runCatching {
                                if (connectStatus.first) {
                                    WebSocketServiceImpl.disconnect2Room()
                                } else {
                                    WebSocketServiceImpl.connect2Room()
                                }
                            }.getOrElse {
                                NotificationComponent.failed(it.message!!)
                                log.error { it.message }
                            }
                        }
                    },
                    modifier = Modifier.width(50.dp).height(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.LightGray
                    ),
                    contentPadding = PaddingValues(4.dp, 2.dp)
                ) {
                    Text(
                        text = if (connectStatus.first) "断开" else "连接",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().height(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource("icons/status.svg"),
                        contentDescription = "connectStatusIcon",
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp).align(Alignment.CenterVertically)
                    )
                    Box(Modifier.height(16.dp).align(Alignment.CenterVertically)) {
                        Text(
                            text = " ${connectStatus.second}",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}