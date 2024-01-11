package ski.mashiro.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import ski.mashiro.common.GlobalBean
import ski.mashiro.handler.MessageHandler
import ski.mashiro.service.impl.WebSocketServiceImpl
import ski.mashiro.timer.HeartbeatTimer
import ski.mashiro.util.HeartbeatUtils

/**
 * @author mashirot
 */
class WebSocketListener : WebSocketListener() {
    private val log = KotlinLogging.logger { this::class.java.name }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        log.info { "ws closed" }
        HeartbeatTimer.stop()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        log.warn { "ws error" }
        log.warn { t }
        HeartbeatTimer.stop()
        webSocket.cancel()
        GlobalBean.webSocket = null
        GlobalBean.IO_SCOPE.launch {
            WebSocketServiceImpl.reconnect()
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        MessageHandler.handle(bytes)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        log.debug { "ws opined" }
        webSocket.send(HeartbeatUtils.generateAuthorizeBag())
        HeartbeatTimer.start(webSocket)
    }

}