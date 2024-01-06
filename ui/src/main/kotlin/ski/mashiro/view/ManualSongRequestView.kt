package ski.mashiro.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManualSongRequestView() {
    Column(modifier = Modifier.fillMaxSize()) {

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
                    maxLines = 1
                )
                IconButton(
                    onClick = {
                        if (StringUtils.isNotBlank(searchContent)) {
                            GlobalBean.IO_SCOPE.launch {
                                searchResult = NeteaseCloudMusicServiceImpl.listMusicByKeyword(searchContent)
                            }
                        }
                    },
                    modifier = Modifier.width(54.dp)
                ) {
                    Icon(
                        painter = painterResource("icon/search.svg"),
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
            val musicNameWeight = 0.35F
            val singerWeight = 0.25F
            val durationWeight = 0.1F
            val operationWeight = 0.25F
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
                    Row(
                        Modifier.fillMaxWidth(),
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
                            },
                            modifier = Modifier.weight(operationWeight)
                        ) {
                            Text(
                                text = "添加",
                                Modifier
                                    .padding(colHorizontalPadding, colVerticalPadding)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                                fontSize = colFontSize,
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