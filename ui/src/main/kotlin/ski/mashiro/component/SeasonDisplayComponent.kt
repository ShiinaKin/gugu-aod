package ski.mashiro.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import io.github.oshai.kotlinlogging.KotlinLogging
import ski.mashiro.BackendMain
import ski.mashiro.common.GlobalBean
import ski.mashiro.util.ObservableAtomicReference

/**
 * @author mashirot
 * 2024/1/25 18:37
 */
@Composable
fun SeasonDisplayComponent() {
    val log = KotlinLogging.logger{ }
    val atomicSeasonId = remember {
        ObservableAtomicReference(1) { atomicInteger, _, invokeCallBack ->
            val oldValue = atomicInteger.get()
            val newValue = oldValue + 1
            if (atomicInteger.compareAndSet(oldValue, newValue)) {
                log.debug { "atomicSeasonId update, value: $newValue" }
                invokeCallBack.invoke(newValue)
            }
        }
    }
    var displaySeasonId by remember { mutableStateOf(1) }
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