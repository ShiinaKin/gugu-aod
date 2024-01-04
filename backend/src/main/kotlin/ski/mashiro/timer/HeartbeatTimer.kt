package ski.mashiro.timer

import kotlinx.coroutines.*
import okhttp3.WebSocket
import ski.mashiro.util.HeartbeatUtils
import kotlin.time.Duration

/**
 * @author mashirot
 */
object HeartbeatTimer {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun start(webSocket: WebSocket) {
        coroutineScope.launch {
            sendHeartbeatBag(webSocket)
        }
    }

    fun stop() {
        println("stop")
        coroutineScope.cancel()
    }

    private suspend fun sendHeartbeatBag(webSocket: WebSocket) {
        withContext(Dispatchers.IO) {
            delay(Duration.parse("2s"))
            while (true) {
                webSocket.send(HeartbeatUtils.generateHeartbeatBag())
                println("Ping!!!")
                delay(Duration.parse("30s"))
            }
        }
    }

}