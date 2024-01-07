package ski.mashiro.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ski.mashiro.component.player.GuGuMediaPlayerController
import kotlin.math.roundToInt

/**
 * @author mashirot
 * 2024/1/4 11:10
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPlayerComponent() {

    var musicStatus by remember { mutableStateOf(false) }

    LaunchedEffect(GuGuMediaPlayerController.hasMusic()) {
        musicStatus = GuGuMediaPlayerController.hasMusic()
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier.size(100.dp)
        ) {
            if (musicStatus) {
                AsyncImage(
                    load = {
                        try {
                            loadImageBitmap(GuGuMediaPlayerController.curMusicInfo?.second?.coverImgUrl!!)
                        } catch (e: Exception) {
                            println(e)
                            println("歌曲: ${GuGuMediaPlayerController.curMusicInfo?.second} 封面图获取失败")
                            loadImageBitmap("https://sm.ms/image/BEOaPfZsXLh9H41")
                        }
                    },
                    painterFor = { remember { BitmapPainter(it) } },
                    contentDescription = "musicCoverImg",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource("icon/album.svg"),
                    contentDescription = "defaultMusicCoverImg",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.DarkGray
                )
            }
        }

        /**
         * 音频控件
         */
        Column(
            modifier = Modifier.padding(3.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            /**
             * 歌曲信息
             */
            Row(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .weight(1f, true),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val musicNameModifier = Modifier.weight(0.3F)
                val musicSingerModifier = Modifier.weight(0.3F).padding(5.dp, 0.dp)
                val usernameModifier = Modifier.weight(0.3F)
                if (musicStatus) {
                    Text(
                        text = GuGuMediaPlayerController.curMusicInfo!!.second.name,
                        modifier = musicNameModifier,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = GuGuMediaPlayerController.curMusicInfo!!.second.singer,
                        modifier = musicSingerModifier,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = GuGuMediaPlayerController.curMusicInfo!!.first,
                        modifier = usernameModifier,
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = "暂无歌曲",
                        modifier = musicNameModifier,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "",
                        textAlign = TextAlign.Start,
                        modifier = musicSingerModifier,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "",
                        modifier = usernameModifier,
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            /**
             * 进度条控件
             */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val interactionSource = MutableInteractionSource()
                Slider(
                    value = GuGuMediaPlayerController.progress,
                    onValueChange = {
                        if (musicStatus) {
                            GuGuMediaPlayerController.jump2Position(it)
                        }
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
                        text = if (musicStatus) "${GuGuMediaPlayerController.curTimeStr}/${GuGuMediaPlayerController.durationStr}" else "00:00/00:00",
                        modifier = Modifier.width(56.dp).fillMaxHeight(),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )

                }
            }

            /**
             * 功能控件
             */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(2.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /**
                 * 暂停/播放/后一首 控件
                 */
                Row(
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val btnSize = 32.dp
                    val btnPadding = PaddingValues(0.dp)
                    val iconSize = 20.dp
                    val btnColor = Color.LightGray

                    Button(
                        modifier = Modifier.size(btnSize),
                        contentPadding = btnPadding,
                        onClick = {
                            if (GuGuMediaPlayerController.isPaused()) {
                                GuGuMediaPlayerController.start()
                            } else {
                                GuGuMediaPlayerController.pause()
                            }
                        },
                        enabled = musicStatus,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = btnColor
                        )
                    ) {
                        Icon(
                            painter = if (GuGuMediaPlayerController.isPaused())
                                painterResource("icon/play_arrow.svg")
                            else
                                painterResource("icon/pause.svg"),
                            contentDescription = "play/pause btn",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                    Button(
                        modifier = Modifier.size(btnSize),
                        contentPadding = btnPadding,
                        onClick = {
                            GuGuMediaPlayerController.skipNext()
                        },
                        enabled = musicStatus,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = btnColor
                        )
                    ) {
                        Icon(
                            painter = painterResource("icon/skip_next.svg"),
                            contentDescription = "skip next",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }

                /**
                 * 音量控件
                 */
                Row(
                    modifier = Modifier.width(120.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    var volumeStatus by remember { mutableStateOf(true) }
                    IconButton(
                        onClick = {
                            volumeStatus = GuGuMediaPlayerController.mute()
                        },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            painter = if (volumeStatus) painterResource("icon/volume_up.svg") else painterResource("icon/volume_off.svg"),
                            contentDescription = "volumeIcon"
                        )
                    }

                    var curVolume by remember { mutableStateOf(0.5F) }
                    val interactionSource = MutableInteractionSource()
                    Slider(
                        value = curVolume,
                        onValueChange = {
                            curVolume = it
                            GuGuMediaPlayerController.setVolume(it.times(100).roundToInt())
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
                            .width(100.dp)
                            .fillMaxHeight(),
                        enabled = volumeStatus
                    )
                }
            }
        }
    }
}