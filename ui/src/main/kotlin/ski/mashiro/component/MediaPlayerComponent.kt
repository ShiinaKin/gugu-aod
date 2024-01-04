package ski.mashiro.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent

/**
 * @author mashirot
 * 2024/1/4 11:10
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerComponent() {

    val audioPlayer = remember { AudioPlayerComponent() }
    var isPlaying by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp)
            .border(2.dp, Color.Yellow)
    ) {
        Box(
            modifier = Modifier.size(100.dp)
        ) {
            AsyncImage(
                load = { loadImageBitmap("https://p2.music.126.net/78Chlbwl9fiH8WTsw3arxg==/109951163443253139.jpg") },
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = "musicCoverImg",
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.padding(3.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .weight(1f, true),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("musicName")
                Text("singer")
                Text("username")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val curTime = 0F

                val interactionSource = MutableInteractionSource()
                Slider(
                    value = curTime,
                    onValueChange = {

                    },
                    interactionSource = interactionSource,
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.DarkGray,
                        inactiveTrackColor = Color.Gray,
                        thumbColor = Color.DarkGray
                    ),
                    thumb = {
                        SliderDefaults.Thumb(
                            interactionSource = interactionSource,
                            thumbSize = DpSize(0.dp, 0.dp),
                        )
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f, true),
                    enabled = true
                )
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "00:00/01:00",
                        modifier = Modifier.width(56.dp).fillMaxHeight(),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(2.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                        .border(2.dp, Color.Black),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val btnSize = 32.dp
                    val btnPadding = PaddingValues(0.dp)
                    val iconSize = 20.dp

                    Button(
                        modifier = Modifier.size(btnSize),
                        contentPadding = btnPadding,
                        onClick = {
                            println("skip previous")
                        }
                    ) {
                        Icon(
                            painter = painterResource("icon/skip_previous.svg"),
                            contentDescription = "skip previous",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                    Button(
                        modifier = Modifier.size(btnSize),
                        contentPadding = btnPadding,
                        onClick = {
                            if (isPlaying) {
                                audioPlayer.mediaPlayer().controls().pause()
                            } else {
                                audioPlayer.mediaPlayer().controls().play()
                            }
                            isPlaying = !isPlaying
                        },
                    ) {
                        Icon(
                            painter = if (isPlaying) painterResource("icon/pause.svg") else painterResource("icon/play_arrow.svg"),
                            contentDescription = "play/pause btn",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                    Button(
                        modifier = Modifier.size(btnSize),
                        contentPadding = btnPadding,
                        onClick = {
                            println("skip next")
                        }
                    ) {
                        Icon(
                            painter = painterResource("icon/skip_next.svg"),
                            contentDescription = "skip next",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
    }
}