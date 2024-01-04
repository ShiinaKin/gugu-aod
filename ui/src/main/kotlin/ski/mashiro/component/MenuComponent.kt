package ski.mashiro.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
                    painter = painterResource("home.svg"),
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
                    painter = painterResource("manage_search.svg"),
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
                    painter = painterResource("setting.svg"),
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
}