package ski.mashiro.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    horizontalPadding: Dp = 2.dp,
    verticalPadding: Dp = 8.dp,
    fontSize: TextUnit = 16.sp
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(horizontalPadding, verticalPadding)
            .align(Alignment.CenterVertically),
        textAlign = TextAlign.Center,
        fontSize = fontSize,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Ellipsis
    )
}