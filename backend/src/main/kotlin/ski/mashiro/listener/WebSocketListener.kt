package ski.mashiro.listener

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

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println("closed")
        HeartbeatTimer.stop()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("=====ERR=====")
        println(t.stackTraceToString())
        println("=============")
        HeartbeatTimer.stop()
        webSocket.cancel()
        GlobalBean.webSocket = null
        GlobalBean.MAIN_SCOPE.launch {
            WebSocketServiceImpl.reconnect()
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        MessageHandler.handle(bytes)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("Opening")
        webSocket.send(HeartbeatUtils.generateAuthorizeBag())
        HeartbeatTimer.start(webSocket)
    }

}