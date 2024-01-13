package ski.mashiro.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * @author mashirot
 * 2024/1/13 17:27
 */
object NotificationComponent {
    var showNotificationDialog by mutableStateOf(false)
    var notificationMsg by mutableStateOf("")

    fun success(msg: String = "成功") {
        notificationMsg = msg
        showNotificationDialog = true
    }

    fun failed(msg: String = "内容不合法") {
        notificationMsg = msg
        showNotificationDialog = true
    }

    fun error(msg: String = "未知错误，请查看日志") {
        notificationMsg = msg
        showNotificationDialog = true
    }
}

@Composable
fun notification() {
    var show by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }
    LaunchedEffect(NotificationComponent.showNotificationDialog) {
        show = NotificationComponent.showNotificationDialog
        msg = NotificationComponent.notificationMsg
    }
    if (show) {
        AlertDialog(
            modifier = Modifier.width(240.dp).height(120.dp),
            text = {
                Text(
                    text = msg,
                    textAlign = TextAlign.Start
                )
            },
            onDismissRequest = {
                NotificationComponent.showNotificationDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        NotificationComponent.showNotificationDialog = false
                    },
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "OK"
                    )
                }
            }
        )
    }
}