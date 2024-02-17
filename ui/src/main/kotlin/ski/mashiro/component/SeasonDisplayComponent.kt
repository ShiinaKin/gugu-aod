package ski.mashiro.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ski.mashiro.BackendMain
import ski.mashiro.common.GlobalBean
import ski.mashiro.util.ObservableAtomicReference

/**
 * @author mashirot
 * 2024/1/25 18:37
 */
@Composable
fun SeasonDisplayComponent() {
    val atomicSeasonId = remember {
        ObservableAtomicReference(0) { atomicInteger, invokeCallBack ->
            val oldValue = atomicInteger.get()
            val newValue = oldValue + 1
            if (atomicInteger.compareAndSet(oldValue, newValue)) {
                invokeCallBack.invoke(newValue)
            }
        }
    }
    var displaySeasonId by remember { mutableStateOf(0) }
    atomicSeasonId.addListener{ newValue ->
        displaySeasonId = newValue
    }
    GlobalBean.seasonInProgress.addListener {
        if (!it) {
            BackendMain.resetMusicCoolDown()
            atomicSeasonId.value = -1
        }
    }
    if (GlobalBean.seasonMode) {
        Column {
            Row {
                Box {
                    Text(
                        text = "第${displaySeasonId}赛季",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}