package ski.mashiro.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import ski.mashiro.common.GlobalBean
import ski.mashiro.component.TableCell
import ski.mashiro.entity.music.NeteaseCloudMusic
import ski.mashiro.service.impl.NeteaseCloudMusicServiceImpl

/**
 * @author mashirot
 * 2024/1/3 22:24
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ManualSongRequestView() {
    Column(modifier = Modifier.fillMaxSize()) {

        var showFailedDialog by remember { mutableStateOf(false) }
        if (showFailedDialog) {
            AlertDialog(
                modifier = Modifier.width(250.dp).height(120.dp),
                text = {
                    Text(
                        text = "请求失败，可能是apiUrl不正确或网络问题",
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

        var searchResult by remember { mutableStateOf<List<NeteaseCloudMusic>>(emptyList()) }

        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(50.dp).fillMaxHeight().align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "搜索：",
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxHeight().weight(1F, true),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var searchContent by remember { mutableStateOf("") }
                TextField(
                    value = searchContent,
                    onValueChange = {
                        searchContent = it
                    },
                    modifier = Modifier.weight(1F, true),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if (StringUtils.isNotBlank(searchContent)) {
                            GlobalBean.IO_SCOPE.launch {
                                runCatching {
                                    searchResult = emptyList()
                                    searchResult = NeteaseCloudMusicServiceImpl.listMusicByKeyword(searchContent)
                                }.getOrElse {
                                    showFailedDialog = true
                                }
                            }
                        }
                    },
                    modifier = Modifier.width(54.dp)
                ) {
                    Icon(
                        painter = painterResource("icons/search.svg"),
                        contentDescription = "searchIcon"
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(0.dp, 5.dp), color = Color.LightGray)
        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1f, true)
        ) {
            val musicNameWeight = 0.4F
            val singerWeight = 0.25F
            val durationWeight = 0.1F
            val operationWeight = 0.2F
            val colFontSize = 14.sp
            val colHorizontalPadding = 2.dp
            val colVerticalPadding = 4.dp

            LazyColumn(Modifier.fillMaxSize()) {
                stickyHeader {
                    Row(
                        Modifier.fillMaxWidth().background(Color.White),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TableCell("歌名", musicNameWeight)
                        TableCell("歌手", singerWeight)
                        TableCell("时长", durationWeight)
                        TableCell("操作", operationWeight)
                    }
                    Divider(Modifier.padding(2.dp, 1.dp))
                }
                items(searchResult) { music ->
                    var isHover by remember { mutableStateOf(false) }
                    var isAdded by remember { mutableStateOf(false) }
                    Row(
                        Modifier.fillMaxWidth()
                            .height(36.dp)
                            .background(color = if (isHover) Color.LightGray else Color.White)
                            .onPointerEvent(PointerEventType.Enter) {
                                isHover = true
                            }
                            .onPointerEvent(PointerEventType.Exit) {
                                isHover = false
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(
                            text = music.name,
                            musicNameWeight,
                            colHorizontalPadding,
                            colVerticalPadding,
                            colFontSize
                        )
                        TableCell(
                            text = music.singer,
                            singerWeight,
                            colHorizontalPadding,
                            colVerticalPadding,
                            colFontSize
                        )
                        TableCell(
                            text = music.durationStr,
                            durationWeight,
                            colHorizontalPadding,
                            colVerticalPadding,
                            colFontSize
                        )
                        TextButton(
                            onClick = {
                                GlobalBean.musicList.add("Admin" to music)
                                isAdded = true
                            },
                            modifier = Modifier.weight(operationWeight).padding(10.dp, 0.dp),
                            contentPadding = (PaddingValues(2.dp)),
                            enabled = !isAdded
                        ) {
                            val btnFontSize = 12.sp
                            if (!isAdded) {
                                Text(
                                    text = "添加",
                                    Modifier
                                        .padding(colHorizontalPadding, colVerticalPadding)
                                        .align(Alignment.CenterVertically),
                                    textAlign = TextAlign.Center,
                                    fontSize = btnFontSize,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text(
                                    text = "已添加",
                                    Modifier
                                        .padding(colHorizontalPadding, colVerticalPadding)
                                        .align(Alignment.CenterVertically),
                                    textAlign = TextAlign.Center,
                                    fontSize = btnFontSize,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}