package ski.mashiro.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * @author mashirot
 * 2024/1/4 11:10
 */
@Composable
fun MediaPlayerComponent() {
    Box(modifier = Modifier
        .height(100.dp)
        .fillMaxWidth()
        .border(2.dp, color = Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Text("Player")
    }
}