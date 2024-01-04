package ski.mashiro.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ski.mashiro.component.AudioPlayerComponent
import ski.mashiro.component.PlayListComponent

/**
 * @author mashirot
 * 2024/1/3 19:29
 */
@Composable
fun HomeView() {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1f, true)
                .border(2.dp, color = Color.Cyan)
        ) {
            PlayListComponent()
        }
        Divider(modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp))
        AudioPlayerComponent()
    }
}