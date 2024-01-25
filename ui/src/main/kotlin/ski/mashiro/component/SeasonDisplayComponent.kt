package ski.mashiro.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ski.mashiro.common.GlobalBean

/**
 * @author mashirot
 * 2024/1/25 18:37
 */
@Composable
fun SeasonDisplayComponent() {
    LaunchedEffect(GlobalBean.seasonInProgress) {
        if (!GlobalBean.seasonInProgress) {
            GlobalBean.seasonId++
        }
    }
    if (GlobalBean.seasonMode) {
        Column {
            Row {
                Box {
                    Text(
                        text = "第${GlobalBean.seasonId}赛季",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}