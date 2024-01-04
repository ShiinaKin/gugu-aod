package ski.mashiro.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import ski.mashiro.component.MediaPlayerComponent
import ski.mashiro.component.TableCell

/**
 * @author mashirot
 * 2024/1/3 19:29
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeView() {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1f, true)
                .border(2.dp, color = Color.Cyan)
        ) {
            Text("Music Waiting List")

            val idxWeight = 0.1F
            val musicNameWeight = 0.45F
            val singerWeight = 0.25F
            val bookingUsernameWeight = 0.2F
            val colFontSize = 14.sp
            val colHorizontalPadding = 2.dp
            val colVerticalPadding = 4.dp

            LazyColumn(Modifier.fillMaxSize()) {
                stickyHeader {
                    Row(
                        Modifier.fillMaxWidth().background(Color.White),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TableCell("序号", idxWeight)
                        TableCell("歌曲名", musicNameWeight)
                        TableCell("歌手", singerWeight)
                        TableCell("点歌人", bookingUsernameWeight)
                    }
                    Divider(Modifier.padding(2.dp, 1.dp))
                }
                itemsIndexed(GlobalBean.musicList) { idx, pair ->
                    val (username, music) = pair
                    Row(Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
                        TableCell(text = idx.toString(), idxWeight, colHorizontalPadding, colVerticalPadding, colFontSize)
                        TableCell(text = music.name, musicNameWeight, colHorizontalPadding, colVerticalPadding, colFontSize)
                        TableCell(text = music.singer, singerWeight, colHorizontalPadding, colVerticalPadding, colFontSize)
                        TableCell(text = username, bookingUsernameWeight, colHorizontalPadding, colVerticalPadding, colFontSize)
                    }
                }
            }
        }
        Divider(modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp))
        MediaPlayerComponent()
    }
}