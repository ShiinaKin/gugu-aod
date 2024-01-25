package ski.mashiro.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apache.commons.lang3.StringUtils
import ski.mashiro.BackendMain
import ski.mashiro.common.GlobalBean
import ski.mashiro.component.NotificationComponent
import ski.mashiro.component.TableCell
import ski.mashiro.component.notification

/**
 * @author mashirot
 * 2024/1/25 14:11
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingView() {
    notification()
    Column {
        val titleRowModifier = Modifier.fillMaxWidth().height(44.dp)
        val titleBoxModifier = Modifier.height(44.dp)
        val btnModifier = Modifier.width(44.dp).height(28.dp).align(Alignment.CenterHorizontally)
        val colModifier = Modifier.fillMaxWidth().height(60.dp).padding(0.dp, 5.dp)
        val textModifier = Modifier.fillMaxHeight().weight(1F)
        val switchModifier = Modifier.height(60.dp).weight(0.2F)
        val textFieldModifier = Modifier.height(60.dp).weight(1F)
        val rowItemModifier = Modifier.weight(0.5F).fillMaxHeight().padding(5.dp, 0.dp)
        Column {
            var tempSeasonMode by remember { mutableStateOf(GlobalBean.systemConfig.seasonMode) }
            var tempSeasonResetCoolDown by remember { mutableStateOf(GlobalBean.systemConfig.seasonResetCoolDown) }
            var tempSingleSeasonMusicNum by remember { mutableStateOf(GlobalBean.systemConfig.singleSeasonMusicNum.toString()) }
            var tempLogLevel by remember { mutableStateOf(GlobalBean.systemConfig.logLevel) }
            Row(
                modifier = titleRowModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = titleBoxModifier,
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "功能设置",
                        fontSize = 16.sp
                    )
                }
                TextButton(
                    onClick = {
                        if (!StringUtils.isNumeric(tempSingleSeasonMusicNum) || tempSingleSeasonMusicNum.toInt() <= 0
                            || StringUtils.isBlank(tempLogLevel)
                        ) {
                            NotificationComponent.failed("内容不合法")
                            return@TextButton
                        }
                        GlobalBean.systemConfig.seasonResetCoolDown = tempSeasonResetCoolDown
                        GlobalBean.systemConfig.singleSeasonMusicNum = tempSingleSeasonMusicNum.toInt()
                        GlobalBean.systemConfig.logLevel = tempLogLevel
                        if (!GlobalBean.systemConfig.seasonMode && tempSeasonMode) {
                            BackendMain.resetMusicList()
                            GlobalBean.seasonId = 1
                        }
                        GlobalBean.seasonMode = tempSeasonMode
                        GlobalBean.systemConfig.seasonMode = tempSeasonMode
                        NotificationComponent.success()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.LightGray
                    ),
                    modifier = btnModifier,
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text(
                        text = "保存",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
            Row(
                modifier = colModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = rowItemModifier,
                ) {
                    Box(
                        modifier = textModifier,
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "是否启用赛季模式：",
                        )
                    }
                    Switch(
                        checked = tempSeasonMode,
                        onCheckedChange = {
                            tempSeasonMode = it
                        },
                        modifier = switchModifier
                    )
                }
            }
            Row(
                modifier = colModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = rowItemModifier,
                ) {
                    Box(
                        modifier = textModifier,
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "赛季重置是否重置歌曲冷却：",
                        )
                    }
                    Switch(
                        checked = tempSeasonResetCoolDown,
                        onCheckedChange = {
                            tempSeasonResetCoolDown = it
                        },
                        modifier = switchModifier
                    )
                }
            }
            Row(
                modifier = colModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = rowItemModifier,
                ) {
                    Box(
                        modifier = textModifier,
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "单赛季歌曲数量：",
                        )
                    }
                    TextField(
                        value = tempSingleSeasonMusicNum,
                        onValueChange = {
                            tempSingleSeasonMusicNum = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
                    )
                }
                Row(
                    modifier = rowItemModifier,
                ) {
                    Box(
                        modifier = textModifier,
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "日志等级：",
                        )
                    }
                    TextField(
                        value = tempLogLevel,
                        onValueChange = {
                            tempLogLevel = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
                    )
                }
            }
            Divider(modifier = Modifier.padding(2.dp, 5.dp))
            var tempKeyword by remember { mutableStateOf("") }
            var show by remember { mutableStateOf(false) }
            if (show) {
                AlertDialog(
                    modifier = Modifier.width(300.dp).height(140.dp),
                    text = {
                        Row(
                            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "关键词：",
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                            TextField(
                                value = tempKeyword,
                                onValueChange = {
                                    tempKeyword = it.trim()
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                singleLine = true,
                                textStyle = TextStyle.Default
                            )
                        }
                    },
                    onDismissRequest = {
                        show = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                show = false
                                if (StringUtils.isBlank(tempKeyword)) {
                                    NotificationComponent.failed()
                                    return@TextButton
                                }
                                GlobalBean.keywordBlackList.add(tempKeyword)
                                tempKeyword = ""
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
            Row(
                modifier = titleRowModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = titleBoxModifier,
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "黑名单",
                        fontSize = 16.sp
                    )
                }
                Row {
                    IconButton(
                        onClick = {
                            show = true
                        }
                    ) {
                        Icon(
                            painter = painterResource("icons/add.svg"),
                            contentDescription = "addIcon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    TextButton(
                        onClick = {
                            val distinctList = GlobalBean.keywordBlackList.distinct()
                            GlobalBean.keywordBlackList = distinctList.toMutableStateList()
                            GlobalBean.systemConfig.keywordBlackList = distinctList.toMutableSet()
                            NotificationComponent.success()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray
                        ),
                        modifier = btnModifier,
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(
                            text = "保存",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            LazyColumn() {
                stickyHeader {
                    Row(
                        Modifier.fillMaxWidth().background(Color.White),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TableCell("关键词", 0.85F)
                        TableCell("Ops", 0.15F)
                    }
                    Divider(Modifier.padding(4.dp, 1.dp))
                }
                itemsIndexed(GlobalBean.keywordBlackList) { idx, it ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(36.dp).background(Color.White),
                    ) {
                        TableCell(
                            text = it,
                            0.85F
                        )
                        IconButton(
                            onClick = {
                                GlobalBean.keywordBlackList.removeAt(idx)
                            },
                            modifier = Modifier.weight(0.15F)
                        ) {
                            Icon(
                                painter = painterResource("icons/remove.svg"),
                                contentDescription = "removeIcon",
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}