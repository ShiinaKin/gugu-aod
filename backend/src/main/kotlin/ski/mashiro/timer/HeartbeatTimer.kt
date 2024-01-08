package ski.mashiro.timer

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import okhttp3.WebSocket
import ski.mashiro.util.HeartbeatUtils
import kotlin.time.Duration

/**
 * @author mashirot
 */
object HeartbeatTimer {
    private val log = KotlinLogging.logger { this::class.java.name }

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