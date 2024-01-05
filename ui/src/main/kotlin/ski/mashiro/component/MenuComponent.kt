package ski.mashiro.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ski.mashiro.BackendMain
import ski.mashiro.common.GlobalBean
import ski.mashiro.router.Router

/**
 * @author mashirot
 * 2024/1/3 19:29
 */
@Composable
fun MenuComponent() {
    Column(
        modifier = Modifier
            .padding(10.dp, 5.dp)
            .width(64.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            val btnSize = 60.dp
            val btnDescFontSize = 12.sp

            Button(
                onClick = {
                    Router.navController.navigate("/home")
                },
                modifier = Modifier.padding(0.dp, 5.dp)
                    .size(btnSize),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Column {
                    Image(
                        painter = painterResource("icon/home.svg"),
                        "homeIcon",
                        alignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "主页",
                        textAlign = TextAlign.Center,
                        fontSize = btnDescFontSize
                    )
                }
            }
            Button(
                onClick = {
                    Router.navController.navigate("/manual_song_req")
                },
                modifier = Modifier.padding(0.dp, 5.dp)
                    .size(btnSize),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Column {
                    Image(
                        painter = painterResource("icon/manage_search.svg"),
                        "searchIcon",
                        alignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "点歌",
                        textAlign = TextAlign.Center,
                        fontSize = btnDescFontSize
                    )
                }
            }
            Button(
                onClick = {
                    Router.navController.navigate("/setting")
                },
                modifier = Modifier.padding(0.dp, 5.dp)
                    .size(btnSize),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Column {
                    Image(
                        painter = painterResource("icon/setting.svg"),
                        "settingIcon",
                        alignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "设置",
                        textAlign = TextAlign.Center,
                        fontSize = btnDescFontSize
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            var connectStatus by remember { mutableStateOf(false to "未连接") }
            LaunchedEffect(GlobalBean.webSocket) {
                connectStatus = if (GlobalBean.webSocket == null) false to "未连接" else true to "已连接"
            }
            Button(
                onClick = {
                    if (connectStatus.first) {
                        println("disconnect")
                        BackendMain.disconnect2Room()
                    } else {
                        println("connect")
                        BackendMain.connect2Room()
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
                    painter = painterResource("icon/status.svg"),
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