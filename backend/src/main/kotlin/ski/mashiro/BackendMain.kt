package ski.mashiro

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.launch
import ski.mashiro.common.GlobalBean
import ski.mashiro.config.LoggerConfig
import ski.mashiro.file.ConfigFileOperation
import ski.mashiro.service.impl.WebSocketServiceImpl
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * @author mashirot
 */
object BackendMain {
    private val log = KotlinLogging.logger { this::class.java.name }

    fun init() {
        LoggerConfig.initLogger()
        ConfigFileOperation.loadConfig()
        GlobalBean.uidCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.parse(GlobalBean.songRequestConfig.eachUserCoolDown).toJavaDuration()).build()
        GlobalBean.musicCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.parse(GlobalBean.songRequestConfig.eachSongCoolDown).toJavaDuration()).build()
    }

    fun onClose() {
        ConfigFileOperation.saveConfig()
        disconnect2Room()
    }

    private fun disconnect2Room() {
        GlobalBean.IO_SCOPE.launch {
            WebSocketServiceImpl.disconnect2Room()
        }
    }
}

