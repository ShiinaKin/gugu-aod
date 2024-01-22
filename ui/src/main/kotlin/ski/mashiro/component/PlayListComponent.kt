package ski.mashiro.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.onClick
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ski.mashiro.common.GlobalBean

/**
 * @author mashirot
 * 2024/1/5 1:59
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PlayListComponent() {
    val idxWeight = 0.1F
    val musicNameWeight = 0.3F
    val singerWeight = 0.25F
    val durationWeight = 0.1F
    val bookingUsernameWeight = 0.25F
    val colFontSize = 14.sp
    val colHorizontalPadding = 2.dp
    val colVerticalPadding = 4.dp

    LazyColumn(Modifier.fillMaxSize()) {
        stickyHeader {
            Row(
                Modifier.fillMaxWidth().background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TableCell("#", idxWeight)
                TableCell("歌名", musicNameWeight)
                TableCell("歌手", singerWeight)
                TableCell("时长", durationWeight)
                TableCell("用户", bookingUsernameWeight)
            }
            Divider(Modifier.padding(2.dp, 1.dp))
        }
        itemsIndexed(GlobalBean.musicList) { idx, pair ->
            val (username, music) = pair
            var isHover by remember { mutableStateOf(false) }
            var showOpsMenu by remember { mutableStateOf(false) }
            if (!showOpsMenu) {
                Row(
                    Modifier.fillMaxWidth()
                        .height(36.dp)
                        .background(color = if (isHover) Color.LightGray else Color.White)
                        .onPointerEvent(PointerEventType.Enter) {
                            isHover = true
                        }
                        .onPointerEvent(PointerEventType.Exit) {
                            isHover = false
                        }
                        .onClick {
                            showOpsMenu = true
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(
                        text = idx.plus(1).toString(),
                        idxWeight,
                        colHorizontalPadding,
                        colVerticalPadding,
                        colFontSize
                    )
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
                    TableCell(
                        text = username,
                        bookingUsernameWeight,
                        colHorizontalPadding,
                        colVerticalPadding,
                        colFontSize
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(36.dp)
                        .onClick {
                            showOpsMenu = false
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            GlobalBean.musicList.removeAt(idx)
                            GlobalBean.musicList.add(0, pair)
                            showOpsMenu = false
                        }
                    ) {
                        Icon(
                            painter = painterResource("icons/pin_to_up.svg"),
                            contentDescription = "pinToUpIcon"
                        )
                    }
                    IconButton(
                        onClick = {
                            GlobalBean.musicList.removeAt(idx)
                            GlobalBean.musicList.add(idx - 1, pair)
                            showOpsMenu = false
                        }
                    ) {
                        Icon(
                            painter = painterResource("icons/arrow_drop_up.svg"),
                            contentDescription = "upIcon"
                        )
                    }
                    IconButton(
                        onClick = {
                            GlobalBean.musicList.removeAt(idx)
                            GlobalBean.musicList.add(idx + 1, pair)
                            showOpsMenu = false
                        }
                    ) {
                        Icon(
                            painter = painterResource("icons/arrow_drop_down.svg"),
                            contentDescription = "downIcon"
                        )
                    }
                    IconButton(
                        onClick = {
                            GlobalBean.musicList.removeAt(idx)
                            showOpsMenu = false
                        }
                    ) {
                        Icon(
                            painter = painterResource("icons/remove.svg"),
                            contentDescription = "deleteIcon"
                        )
                    }
                }
            }
        }
    }
}