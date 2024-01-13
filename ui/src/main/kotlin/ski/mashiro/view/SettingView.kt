package ski.mashiro.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import ski.mashiro.common.GlobalBean
import ski.mashiro.component.NotificationComponent
import ski.mashiro.component.notification
import ski.mashiro.exception.NeteaseCouldMusicException
import ski.mashiro.file.ConfigFileOperation
import ski.mashiro.service.impl.NeteaseCloudMusicServiceImpl
import java.util.*
import kotlin.time.Duration

/**
 * @author mashirot
 * 2024/1/3 19:29
 */
@Composable
fun SettingView() {
    notification()
    Column(
        modifier = Modifier.fillMaxSize().padding(5.dp).verticalScroll(rememberScrollState())
    ) {
        val titleRowModifier = Modifier.fillMaxWidth().height(44.dp)
        val titleBoxModifier = Modifier.height(44.dp)
        val btnModifier = Modifier.width(44.dp).height(28.dp).align(Alignment.CenterHorizontally)
        val colModifier = Modifier.fillMaxWidth().height(60.dp).padding(0.dp, 5.dp)
        val textModifier = Modifier.width(72.dp).fillMaxHeight()
        val textFieldModifier = Modifier.height(60.dp).weight(1F)
        val rowItemModifier = Modifier.weight(0.5F).fillMaxHeight().padding(5.dp, 0.dp)
        // roomConfig
        Column {
            var tempRoomId by remember { mutableStateOf(GlobalBean.roomConfig.roomId.toString()) }
            var tempUID by remember { mutableStateOf(GlobalBean.roomConfig.uid.toString()) }
            var tempCookie by remember { mutableStateOf(GlobalBean.roomConfig.cookie) }
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
                        text = "房间设置",
                        fontSize = 16.sp
                    )
                }
                TextButton(
                    onClick = {
                        if (!StringUtils.isNumeric(tempRoomId) || !StringUtils.isNumeric(tempUID)) {
                            NotificationComponent.failed()
                        } else {
                            GlobalBean.roomConfig.roomId = tempRoomId.toLong()
                            GlobalBean.roomConfig.uid = tempUID.toLong()
                            GlobalBean.roomConfig.cookie = tempCookie
                            if (ConfigFileOperation.saveRoomConfig()) {
                                NotificationComponent.success()
                            } else {
                                NotificationComponent.error()
                            }
                        }
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "房间号：",
                        )
                    }
                    TextField(
                        value = tempRoomId,
                        onValueChange = {
                            tempRoomId = it.trim()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UID：",
                        )
                    }
                    TextField(
                        value = tempUID,
                        onValueChange = {
                            tempUID = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cookie：",
                        )
                    }
                    TextField(
                        value = tempCookie,
                        onValueChange = {
                            tempCookie = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
                    )
                }
            }
        }
        // songRequestConfig
        Column {
            var tempPrefix by remember { mutableStateOf(GlobalBean.songRequestConfig.prefix) }
            var tempMedalName by remember { mutableStateOf(GlobalBean.songRequestConfig.medalName ?: "") }
            var tempMedalLevel by remember { mutableStateOf(GlobalBean.songRequestConfig.medalLevel?.toString() ?: "") }
            var tempEachUserCoolDown by remember { mutableStateOf(GlobalBean.songRequestConfig.eachUserCoolDown) }
            var tempEachSongCoolDown by remember { mutableStateOf(GlobalBean.songRequestConfig.eachSongCoolDown) }
            var tempWaitListMaxSize by remember { mutableStateOf(GlobalBean.songRequestConfig.waitListMaxSize.toString()) }
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
                        text = "点歌设置",
                        fontSize = 16.sp
                    )
                }
                TextButton(
                    onClick = {
                        if (StringUtils.isBlank(tempPrefix) || !StringUtils.isNumeric(tempWaitListMaxSize)
                            || StringUtils.isNotBlank(tempMedalName) && !StringUtils.isNumeric(tempMedalLevel)
                            || StringUtils.isBlank(tempEachUserCoolDown) || StringUtils.isBlank(tempEachSongCoolDown)
                            || Objects.isNull(Duration.parseOrNull(tempEachUserCoolDown))
                            || Objects.isNull(Duration.parseOrNull(tempEachSongCoolDown))
                        ) {
                            NotificationComponent.failed()
                        } else {
                            GlobalBean.songRequestConfig.prefix = tempPrefix
                            GlobalBean.songRequestConfig.waitListMaxSize = tempWaitListMaxSize.toInt()
                            GlobalBean.songRequestConfig.eachUserCoolDown = tempEachUserCoolDown
                            GlobalBean.songRequestConfig.eachSongCoolDown = tempEachSongCoolDown
                            if (StringUtils.isNotBlank(tempMedalName)) {
                                GlobalBean.songRequestConfig.medalName
                                GlobalBean.songRequestConfig.medalLevel
                            }
                            if (ConfigFileOperation.saveSongRequestConfig()) {
                                NotificationComponent.success()
                            } else {
                                NotificationComponent.error()
                            }
                        }
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "弹幕前缀：",
                        )
                    }
                    TextField(
                        value = tempPrefix,
                        onValueChange = {
                            tempPrefix = it.trim()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "歌单大小：",
                        )
                    }
                    TextField(
                        value = tempWaitListMaxSize,
                        onValueChange = {
                            tempWaitListMaxSize = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "勋章名称：",
                        )
                    }
                    TextField(
                        value = tempMedalName,
                        onValueChange = {
                            tempMedalName = it.trim()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "勋章等级：",
                        )
                    }
                    TextField(
                        value = tempMedalLevel,
                        onValueChange = {
                            tempMedalLevel = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "用户冷却：",
                        )
                    }
                    TextField(
                        value = tempEachUserCoolDown,
                        onValueChange = {
                            tempEachUserCoolDown = it.trim()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "歌曲冷却：",
                        )
                    }
                    TextField(
                        value = tempEachSongCoolDown,
                        onValueChange = {
                            tempEachSongCoolDown = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
                    )
                }
            }
        }
        // neteaseCloudMusicConfig
        Column {
            var tempPhoneNumber
                    by remember { mutableStateOf(GlobalBean.neteaseCloudMusicConfig.phoneNumber?.toString() ?: "") }
            var tempPassword by remember { mutableStateOf(GlobalBean.neteaseCloudMusicConfig.password ?: "") }
            var tempPasswordMD5 by remember { mutableStateOf(GlobalBean.neteaseCloudMusicConfig.passwordMD5 ?: "") }
            var tempCookie by remember { mutableStateOf(GlobalBean.neteaseCloudMusicConfig.cookie) }
            var tempApiUrl by remember { mutableStateOf(GlobalBean.neteaseCloudMusicConfig.cloudMusicApiUrl) }
            LaunchedEffect(GlobalBean.neteaseCloudMusicConfig.cookie) {
                tempCookie = GlobalBean.neteaseCloudMusicConfig.cookie
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
                        text = "网易云设置",
                        fontSize = 16.sp
                    )
                }
                Box {
                    Text(
                        text = "登录状态: " + if (GlobalBean.neteaseCloudMusicLoginStatus) "已登录" else "未登录",
                        textAlign = TextAlign.Start
                    )
                }
                TextButton(
                    onClick = {
                        GlobalBean.IO_SCOPE.launch {
                            runCatching {
                                NeteaseCloudMusicServiceImpl.login()
                                GlobalBean.neteaseCloudMusicLoginStatus = NeteaseCloudMusicServiceImpl.getLoginStatus()
                                if (ConfigFileOperation.saveNeteaseCloudMusicConfig()) {
                                    NotificationComponent.success()
                                } else {
                                    NotificationComponent.error()
                                }
                            }.getOrElse {
                                val tempErrMsg =
                                    if (it is NeteaseCouldMusicException) it.message else "未知错误，可能是api配置不正确或网络问题"
                                NotificationComponent.error(tempErrMsg)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.LightGray
                    ),
                    modifier = btnModifier,
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text(
                        text = "登录",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
                TextButton(
                    onClick = {
                        if (
                            StringUtils.isNotBlank(tempPhoneNumber)
                            && (!StringUtils.isNumeric(tempPhoneNumber)
                                    || !tempPhoneNumber.matches(Regex("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}\$")))
                            || StringUtils.isBlank(tempApiUrl)
                        ) {
                            NotificationComponent.failed()
                        } else {
                            GlobalBean.neteaseCloudMusicConfig.cookie = tempCookie
                            GlobalBean.neteaseCloudMusicConfig.cloudMusicApiUrl = tempApiUrl
                            if (StringUtils.isNumeric(tempPhoneNumber)) {
                                GlobalBean.neteaseCloudMusicConfig.phoneNumber = tempPhoneNumber.toLong()
                                if (StringUtils.isNotBlank(tempPasswordMD5)) {
                                    GlobalBean.neteaseCloudMusicConfig.passwordMD5 = tempPasswordMD5
                                } else {
                                    GlobalBean.neteaseCloudMusicConfig.password
                                }
                            }
                            if (StringUtils.isNotBlank(GlobalBean.neteaseCloudMusicConfig.cookie)) {
                                GlobalBean.IO_SCOPE.launch {
                                    runCatching {
                                        GlobalBean.neteaseCloudMusicLoginStatus =
                                            NeteaseCloudMusicServiceImpl.getLoginStatus()
                                    }.getOrElse {
                                        val tempErrMsg = it.message!!
                                        NotificationComponent.error(tempErrMsg)
                                    }
                                }
                            }
                            if (ConfigFileOperation.saveNeteaseCloudMusicConfig()) {
                                NotificationComponent.success()
                            } else {
                                NotificationComponent.error()
                            }
                        }
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "手机号：",
                        )
                    }
                    TextField(
                        value = tempPhoneNumber,
                        onValueChange = {
                            tempPhoneNumber = it.trim()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "密码：",
                        )
                    }
                    TextField(
                        value = tempPassword,
                        onValueChange = {
                            tempPassword = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default,
                        visualTransformation = PasswordVisualTransformation()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "密码MD5：",
                        )
                    }
                    TextField(
                        value = tempPasswordMD5,
                        onValueChange = {
                            tempPasswordMD5 = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default,
                        visualTransformation = PasswordVisualTransformation()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cookie：",
                        )
                    }
                    TextField(
                        value = tempCookie,
                        onValueChange = {
                            tempCookie = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ApiUrl：",
                        )
                    }
                    TextField(
                        value = tempApiUrl,
                        onValueChange = {
                            tempApiUrl = it.trim()
                        },
                        modifier = textFieldModifier,
                        singleLine = true,
                        textStyle = TextStyle.Default
                    )
                }
            }
        }
    }
}