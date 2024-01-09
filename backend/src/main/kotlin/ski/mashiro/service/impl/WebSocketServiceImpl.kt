package ski.mashiro.service.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ski.mashiro.common.GlobalBean.JSON_MAPPER
import ski.mashiro.common.GlobalBean.roomConfig
import ski.mashiro.common.GlobalBean.webSocket
import ski.mashiro.const.LockConsts
import ski.mashiro.exception.WebSocketException
import ski.mashiro.factory.OkHttpClientFactory
import ski.mashiro.factory.RequestBuilderFactory
import ski.mashiro.listener.WebSocketListener
import ski.mashiro.service.WebSocketService
import ski.mashiro.util.LockUtils

/**
 * @author mashirot
 */
object WebSocketServiceImpl : WebSocketService {
    private val log = KotlinLogging.logger { this::class.java.name }

    private const val MAX_RECONNECT_NUM = 5

    override fun connect2Room() {
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()

        val roomRequest = RequestBuilderFactory.getReqBuilderWithBiliCookieAndUA()
            .url("https://api.live.bilibili.com/room/v1/Room/room_init?id=${roomConfig.roomId}")
            .build()
        runCatching {
            val roomData = okHttpClient.newCall(roomRequest).execute().run {
                val map = JSON_MAPPER.readValue(body!!.string(), HashMap::class.java)
                map["data"] as HashMap<*, *>
            }
            roomConfig.roomId = roomData["room_id"].toString().toLong()
            roomConfig.anchormanUID = roomData["uid"].toString().toLong()
        }.getOrElse {
            log.warn { "roomReq发生错误, msg: ${it.message}" }
            throw WebSocketException("roomReq发生错误")
        }

        val keyRequest = RequestBuilderFactory.getReqBuilderWithBiliCookieAndUA()
            .url("https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id=${roomConfig.roomId}&platform=pc&player=web")
            .build()
        runCatching {
            roomConfig.key = okHttpClient.newCall(keyRequest).execute().run {
                val map = JSON_MAPPER.readValue(body!!.string(), HashMap::class.java)
                val data = map["data"] as HashMap<*, *>
                data["token"] as String
            }
        }.getOrElse {
            log.warn { "keyReq发生错误, msg: ${it.message}" }
            throw WebSocketException("keyReq发生错误")
        }

        val wsRequest = RequestBuilderFactory.getReqBuilderWithUA()
            .url("wss://broadcastlv.chat.bilibili.com/sub")
            .build()
        runCatching {
            webSocket = okHttpClient.newWebSocket(wsRequest, WebSocketListener())
            log.info { "ws 连接成功" }
        }.getOrElse {
            log.warn { "wsReq发生错误, msg: ${it.message}" }
            throw WebSocketException("wsReq发生错误")
        }
    }

    override suspend fun reconnect() {
        if (webSocket != null) {
            return
        }
        withContext(Dispatchers.IO) {
            if (LockUtils.tryLock(LockConsts.RECONNECT_LOCK)) {
                try {
                    for (i in 0 until MAX_RECONNECT_NUM) {
                        log.info { "尝试重连, 次数: ${i + 1}" }
                        connect2Room()
                        if (webSocket != null) {
                            log.info { "重连成功" }
                            return@withContext
                        }
                        delay(kotlin.time.Duration.parse("5s"))
                    }
                    if (webSocket == null) {
                        log.info { "已达最大重连次数, 重连失败" }
                        return@withContext
                    }
                } finally {
                    LockUtils.releaseLock(LockConsts.RECONNECT_LOCK)
                }
                return@withContext
            }
            log.info { "已有线程正在尝试重连" }
        }
    }

    override fun disconnect2Room() {
        if (webSocket == null) {
            return
        }
        webSocket!!.close(1008, "Manual Close")
        webSocket = null
    }
}