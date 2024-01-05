package ski.mashiro.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ski.mashiro.common.GlobalBean

/**
 * @author mashirot
 * 2024/1/5 1:59
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayListComponent() {
    Text("Music Waiting List")

    val idxWeight = 0.05F
    val musicNameWeight = 0.35F
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
            Row(
                Modifier.fillMaxWidth(),
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
                    text = music.duration,
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
        }
    }
}