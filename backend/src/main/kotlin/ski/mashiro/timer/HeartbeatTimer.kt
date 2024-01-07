package ski.mashiro.timer

import kotlinx.coroutines.*
import okhttp3.WebSocket
import ski.mashiro.annotation.Logger
import ski.mashiro.annotation.Logger.Companion.log
import ski.mashiro.util.HeartbeatUtils
import kotlin.time.Duration

/**
 * @author mashirot
 */
@Logger
object HeartbeatTimer {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun start(webSocket: WebSocket) {
        coroutineScope.launch {
            sendHeartbeatBag(webSocket)
        }
    }

    fun stop() {
        coroutineScope.cancel()
        log.debug { "HeartbeatTimer stopped" }
    }

    private suspend fun sendHeartbeatBag(webSocket: WebSocket) {
        delay(Duration.parse("2s"))
        while (true) {
            webSocket.send(HeartbeatUtils.generateHeartbeatBag())
            log.debug { "Ping!!!" }
            delay(Duration.parse("30s"))
        }
    }

}